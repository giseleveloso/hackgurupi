package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Comentario;

public record ComentarioResponseDTO(
    Long id,
    Long projetoId,
    Long cidadaoId,
    String cidadaoNome,
    String texto,
    LocalDateTime dataComentario,
    Boolean editado
) {
    public static ComentarioResponseDTO valueOf(Comentario comentario) {
        return new ComentarioResponseDTO(
            comentario.getId(),
            comentario.getProjeto().getId(),
            comentario.getCidadao().getId(),
            comentario.getCidadao().getNome(),
            comentario.getTexto(),
            comentario.getDataComentario(),
            comentario.getEditado()
        );
    }
}

