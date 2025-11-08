package br.unitins.topicos1.dto;

import java.time.LocalDate;

public record RepositorioDadosDTO(
    String nome,
    String descricao,
    String url,
    String fonte,
    String categoriaDados,
    LocalDate ultimaAtualizacao
) {}

