package br.unitins.topicos1.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileService {
    
    @ConfigProperty(name = "app.upload.directory", defaultValue = "uploads")
    String uploadDirectory;
    
    @ConfigProperty(name = "app.upload.max-file-size", defaultValue = "10485760") // 10MB
    Long maxFileSize;
    
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "jpg", "jpeg", "png", "gif",
        "txt", "csv", "zip", "rar"
    );
    
    /**
     * Salva arquivo no sistema de arquivos
     * 
     * @param inputStream Stream do arquivo
     * @param originalFileName Nome original do arquivo
     * @return Caminho relativo do arquivo salvo
     */
    public String saveFile(InputStream inputStream, String originalFileName, Long projetoId) throws IOException {
        // Valida nome do arquivo
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do arquivo não pode ser vazio");
        }
        
        // Valida extensão
        String extension = getFileExtension(originalFileName);
        if (extension == null || extension.isEmpty()) {
            throw new IllegalArgumentException("Arquivo deve ter uma extensão válida (ex: .pdf, .jpg)");
        }
        
        if (!isAllowedExtension(extension)) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido: ." + extension + 
                ". Tipos permitidos: " + String.join(", ", ALLOWED_EXTENSIONS));
        }
        
        // Cria diretório se não existir
        Path uploadPath = Paths.get(uploadDirectory, "projetos", String.valueOf(projetoId));
        Files.createDirectories(uploadPath);
        
        // Gera nome único para o arquivo
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;
        Path filePath = uploadPath.resolve(uniqueFileName);
        
        // Salva o arquivo
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = 0;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                totalBytes += bytesRead;
                
                // Verifica tamanho máximo
                if (totalBytes > maxFileSize) {
                    filePath.toFile().delete();
                    throw new IllegalArgumentException("Arquivo excede o tamanho máximo permitido");
                }
                
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        
        // Retorna caminho relativo
        return "projetos/" + projetoId + "/" + uniqueFileName;
    }
    
    /**
     * Deleta arquivo do sistema
     */
    public void deleteFile(String relativePath) {
        try {
            Path filePath = Paths.get(uploadDirectory, relativePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Erro ao deletar arquivo: " + e.getMessage());
        }
    }
    
    /**
     * Retorna o arquivo como byte array
     */
    public byte[] getFile(String relativePath) throws IOException {
        Path filePath = Paths.get(uploadDirectory, relativePath);
        if (!Files.exists(filePath)) {
            throw new IOException("Arquivo não encontrado");
        }
        return Files.readAllBytes(filePath);
    }
    
    /**
     * Verifica se o arquivo existe
     */
    public boolean fileExists(String relativePath) {
        Path filePath = Paths.get(uploadDirectory, relativePath);
        return Files.exists(filePath);
    }
    
    /**
     * Obtém a extensão do arquivo
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }
        
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1).toLowerCase().trim();
        }
        return "";
    }
    
    /**
     * Verifica se a extensão é permitida
     */
    private boolean isAllowedExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }
    
    /**
     * Retorna o tipo MIME baseado na extensão
     */
    public String getContentType(String fileName) {
        String extension = getFileExtension(fileName);
        return switch (extension.toLowerCase()) {
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls" -> "application/vnd.ms-excel";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt" -> "application/vnd.ms-powerpoint";
            case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "txt" -> "text/plain";
            case "csv" -> "text/csv";
            case "zip" -> "application/zip";
            case "rar" -> "application/x-rar-compressed";
            default -> "application/octet-stream";
        };
    }
}

