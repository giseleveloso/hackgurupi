package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Voto;

public record VotoResponseDTO(
    Long id,
    Long projetoId,
    String projetoTitulo,
    Long cidadaoId,
    String cidadaoNome,
    LocalDateTime dataVoto
) {
    public static VotoResponseDTO valueOf(Voto voto) {
        return new VotoResponseDTO(
            voto.getId(),
            voto.getProjeto().getId(),
            voto.getProjeto().getTitulo(),
            voto.getCidadao().getId(),
            voto.getCidadao().getNome(),
            voto.getDataVoto()
        );
    }
}

