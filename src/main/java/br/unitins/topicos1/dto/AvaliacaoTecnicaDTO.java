package br.unitins.topicos1.dto;

import java.math.BigDecimal;

public record AvaliacaoTecnicaDTO(
    Long projetoId,
    Long gestorId,
    BigDecimal criterioViabilidade,
    BigDecimal criterioImpacto,
    BigDecimal criterioInovacao,
    BigDecimal criterioOrcamento,
    String justificativa
) {}

