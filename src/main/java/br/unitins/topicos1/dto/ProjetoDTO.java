package br.unitins.topicos1.dto;

import java.math.BigDecimal;

public record ProjetoDTO(
    Long academicoId,
    String titulo,
    String resumoPopular,
    String descricaoCompleta,
    String objetivos,
    String metodologia,
    String resultadosEsperados,
    BigDecimal orcamentoEstimado,
    Integer prazoExecucao,
    Integer areaTematica
) {}

