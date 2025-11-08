package br.unitins.topicos1.form;

import java.io.InputStream;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Formulário para upload de arquivos via multipart/form-data
 * 
 * IMPORTANTE para usar no Swagger:
 * - file: Selecione o arquivo
 * - fileName: Digite o nome COM EXTENSÃO (ex: "documento.pdf", não apenas "documento")
 * - mimeType: Digite o tipo MIME completo (ex: "application/pdf", não apenas "pdf")
 */
public class AnexoUploadForm {
    
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream file;
    
    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    public String fileName;
    
    @FormParam("mimeType")
    @PartType(MediaType.TEXT_PLAIN)
    public String mimeType;
}

