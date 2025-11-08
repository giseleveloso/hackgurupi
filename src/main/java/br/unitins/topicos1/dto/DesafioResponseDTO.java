package br.unitins.topicos1.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.unitins.topicos1.model.Desafio;

public record DesafioResponseDTO(
    Long id,
    String titulo,
    String descricao,
    String areaTematica,
    String status,
    BigDecimal orcamentoDisponivel,
    LocalDate dataInicio,
    LocalDate dataFim,
    Integer prioridade
) {
    public static DesafioResponseDTO valueOf(Desafio desafio) {
        return new DesafioResponseDTO(
            desafio.getId(),
            desafio.getTitulo(),
            desafio.getDescricao(),
            desafio.getAreaTematica() != null ? desafio.getAreaTematica().getLabel() : null,
            desafio.getStatus().getLabel(),
            desafio.getOrcamentoDisponivel(),
            desafio.getDataInicio(),
            desafio.getDataFim(),
            desafio.getPrioridade()
        );
    }
}

