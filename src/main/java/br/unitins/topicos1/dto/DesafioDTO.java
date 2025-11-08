package br.unitins.topicos1.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DesafioDTO(
    String titulo,
    String descricao,
    Integer areaTematica,
    BigDecimal orcamentoDisponivel,
    LocalDate dataInicio,
    LocalDate dataFim,
    Integer prioridade
) {}

