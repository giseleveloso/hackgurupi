package br.unitins.topicos1.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.unitins.topicos1.model.AreaTematica;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.model.StatusProjeto;

public record ProjetoResponseDTO(
    Long id,
    Long academicoId,
    String academicoNome,
    String titulo,
    String resumoPopular,
    String descricaoCompleta,
    String objetivos,
    String metodologia,
    String resultadosEsperados,
    BigDecimal orcamentoEstimado,
    Integer prazoExecucao,
    String areaTematica,
    String status,
    LocalDateTime dataSubmissao,
    LocalDateTime dataAprovacao,
    Integer totalVotos,
    Integer totalVisualizacoes,
    BigDecimal notaTecnica,
    BigDecimal notaPopular,
    BigDecimal notaFinal
) {
    public static ProjetoResponseDTO valueOf(Projeto projeto) {
        return new ProjetoResponseDTO(
            projeto.getId(),
            projeto.getAcademico().getId(),
            projeto.getAcademico().getNome(),
            projeto.getTitulo(),
            projeto.getResumoPopular(),
            projeto.getDescricaoCompleta(),
            projeto.getObjetivos(),
            projeto.getMetodologia(),
            projeto.getResultadosEsperados(),
            projeto.getOrcamentoEstimado(),
            projeto.getPrazoExecucao(),
            projeto.getAreaTematica().getLabel(),
            projeto.getStatus().getLabel(),
            projeto.getDataSubmissao(),
            projeto.getDataAprovacao(),
            projeto.getTotalVotos(),
            projeto.getTotalVisualizacoes(),
            projeto.getNotaTecnica(),
            projeto.getNotaPopular(),
            projeto.getNotaFinal()
        );
    }
}

