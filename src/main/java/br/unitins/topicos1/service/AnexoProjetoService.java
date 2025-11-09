package br.unitins.topicos1.service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.model.AnexoProjeto;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.repository.AnexoProjetoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AnexoProjetoService {
    
    @Inject
    AnexoProjetoRepository repository;
    
    @Inject
    ProjetoService projetoService;
    
    @Inject
    FileService fileService;
    
    public List<AnexoProjeto> findByProjeto(Long projetoId) {
        return repository.findByProjeto(projetoId);
    }
    
    public AnexoProjeto findById(Long id) {
        AnexoProjeto anexo = repository.findById(id);
        if (anexo == null) {
            throw new NotFoundException("Anexo não encontrado");
        }
        return anexo;
    }
    
    @Transactional
    public AnexoProjeto upload(Long projetoId, InputStream fileInputStream, 
                               String fileName, String contentType, long fileSize) {
        // Log para debug
        System.out.println("📤 Iniciando upload:");
        System.out.println("  - Projeto ID: " + projetoId);
        System.out.println("  - Nome arquivo: " + fileName);
        System.out.println("  - Content-Type: " + contentType);
        System.out.println("  - Tamanho: " + fileSize + " bytes");
        
        // Busca projeto
        Projeto projeto = projetoService.findById(projetoId);
        
        try {
            // Salva arquivo no sistema
            String relativePath = fileService.saveFile(fileInputStream, fileName, projetoId);
            
            // Cria registro no banco
            AnexoProjeto anexo = new AnexoProjeto();
            anexo.setProjeto(projeto);
            anexo.setNomeArquivo(fileName);
            anexo.setUrlArquivo(relativePath);
            anexo.setTipoArquivo(contentType);
            anexo.setTamanho((int) fileSize);
            anexo.setDataUpload(LocalDateTime.now());
            
            repository.persist(anexo);
            
            return anexo;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        AnexoProjeto anexo = findById(id);
        
        // Deleta arquivo do sistema
        if (anexo.getUrlArquivo() != null) {
            fileService.deleteFile(anexo.getUrlArquivo());
        }
        
        // Deleta registro do banco
        repository.delete(anexo);
    }
    
    public byte[] download(Long id) {
        AnexoProjeto anexo = findById(id);
        
        try {
            return fileService.getFile(anexo.getUrlArquivo());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar arquivo: " + e.getMessage(), e);
        }
    }
}

