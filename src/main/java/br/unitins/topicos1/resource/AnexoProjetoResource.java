package br.unitins.topicos1.resource;

import java.io.InputStream;
import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.unitins.topicos1.dto.AnexoProjetoResponseDTO;
import br.unitins.topicos1.form.AnexoUploadForm;
import br.unitins.topicos1.model.AnexoProjeto;
import br.unitins.topicos1.service.AnexoProjetoService;
import br.unitins.topicos1.service.FileService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/projetos/{projetoId}/anexos")
@Produces(MediaType.APPLICATION_JSON)
public class AnexoProjetoResource {
    
    @Inject
    AnexoProjetoService service;
    
    @Inject
    FileService fileService;
    
    /**
     * GET /projetos/{projetoId}/anexos - Lista anexos do projeto
     */
    @GET
    public Response list(@PathParam("projetoId") Long projetoId) {
        try {
            List<AnexoProjetoResponseDTO> anexos = service.findByProjeto(projetoId).stream()
                .map(AnexoProjetoResponseDTO::valueOf)
                .toList();
            return Response.ok(anexos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * POST /projetos/{projetoId}/anexos - Faz upload de anexo
     * 
     * @param projetoId ID do projeto
     * @param form Formulário com: file (arquivo), fileName (nome.extensão), mimeType (application/pdf)
     * @return AnexoProjetoResponseDTO
     * 
     * IMPORTANTE para Swagger:
     * - fileName: deve incluir extensão (ex: "documento.pdf", não "documento")
     * - mimeType: deve ser completo (ex: "application/pdf", não "pdf")
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@PathParam("projetoId") Long projetoId, 
                          @MultipartForm AnexoUploadForm form) {
        try {
            // Valida arquivo
            if (form.file == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Arquivo não enviado\"}")
                    .build();
            }
            
            // Valida nome do arquivo
            if (form.fileName == null || form.fileName.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Nome do arquivo não pode ser vazio\"}")
                    .build();
            }
            
            // Obtém informações do arquivo
            String fileName = form.fileName.trim();
            String contentType = form.mimeType != null ? form.mimeType.trim() : "application/octet-stream";
            
            // Se mimeType não tem '/' (ex: "pdf" ao invés de "application/pdf"), tenta corrigir
            if (!contentType.contains("/")) {
                contentType = inferMimeType(contentType, fileName);
                System.out.println("⚠️ mimeType incompleto detectado, inferido: " + contentType);
            }
            
            long fileSize = form.file.available();
            
            // Faz upload
            AnexoProjeto anexo = service.upload(projetoId, form.file, fileName, contentType, fileSize);
            
            return Response.status(Response.Status.CREATED)
                .entity(AnexoProjetoResponseDTO.valueOf(anexo))
                .build();
                
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Erro ao fazer upload: " + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * GET /projetos/{projetoId}/anexos/{anexoId}/download - Download do anexo
     */
    @GET
    @Path("/{anexoId}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("projetoId") Long projetoId,
                            @PathParam("anexoId") Long anexoId) {
        try {
            AnexoProjeto anexo = service.findById(anexoId);
            byte[] fileData = service.download(anexoId);
            
            String contentType = fileService.getContentType(anexo.getNomeArquivo());
            
            return Response.ok(fileData)
                .header("Content-Disposition", "attachment; filename=\"" + anexo.getNomeArquivo() + "\"")
                .header("Content-Type", contentType)
                .build();
                
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * DELETE /projetos/{projetoId}/anexos/{anexoId} - Deleta anexo
     */
    @DELETE
    @Path("/{anexoId}")
    public Response delete(@PathParam("projetoId") Long projetoId,
                          @PathParam("anexoId") Long anexoId) {
        try {
            service.delete(anexoId);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * Infere o tipo MIME correto a partir de uma extensão ou tipo incompleto
     */
    private String inferMimeType(String partialType, String fileName) {
        // Se foi passado apenas a extensão (ex: "pdf")
        String extension = partialType.toLowerCase();
        
        // Tenta extrair extensão do fileName se necessário
        if (fileName != null && fileName.contains(".")) {
            String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            if (!fileExt.isEmpty()) {
                extension = fileExt;
            }
        }
        
        // Mapeia extensões para tipos MIME
        return fileService.getContentType("file." + extension);
    }
}

