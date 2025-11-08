package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.AnexoProjeto;

public record AnexoProjetoResponseDTO(
    Long id,
    Long projetoId,
    String nomeArquivo,
    String urlArquivo,
    String tipoArquivo,
    Integer tamanho,
    LocalDateTime dataUpload
) {
    public static AnexoProjetoResponseDTO valueOf(AnexoProjeto anexo) {
        return new AnexoProjetoResponseDTO(
            anexo.getId(),
            anexo.getProjeto().getId(),
            anexo.getNomeArquivo(),
            anexo.getUrlArquivo(),
            anexo.getTipoArquivo(),
            anexo.getTamanho(),
            anexo.getDataUpload()
        );
    }
}

