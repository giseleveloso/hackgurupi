package br.unitins.topicos1.dto;

public record ComentarioDTO(
    Long projetoId,
    Long cidadaoId,
    String texto
) {}

