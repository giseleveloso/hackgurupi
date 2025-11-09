package br.unitins.topicos1.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.unitins.topicos1.model.AvaliacaoTecnica;

public record AvaliacaoTecnicaResponseDTO(
    Long id,
    Long projetoId,
    String projetoTitulo,
    Long gestorId,
    String gestorNome,
    BigDecimal nota,
    BigDecimal criterioViabilidade,
    BigDecimal criterioImpacto,
    BigDecimal criterioInovacao,
    BigDecimal criterioOrcamento,
    String justificativa,
    LocalDateTime dataAvaliacao,
    Boolean geradaPorIA,
    String analiseIA,
    String statusAvaliacao,
    LocalDateTime dataAprovacao,
    String motivoRejeicao
) {
    public static AvaliacaoTecnicaResponseDTO valueOf(AvaliacaoTecnica avaliacao) {
        return new AvaliacaoTecnicaResponseDTO(
            avaliacao.getId(),
            avaliacao.getProjeto().getId(),
            avaliacao.getProjeto().getTitulo(),
            avaliacao.getGestor().getId(),
            avaliacao.getGestor().getNome(),
            avaliacao.getNota(),
            avaliacao.getCriterioViabilidade(),
            avaliacao.getCriterioImpacto(),
            avaliacao.getCriterioInovacao(),
            avaliacao.getCriterioOrcamento(),
            avaliacao.getJustificativa(),
            avaliacao.getDataAvaliacao(),
            avaliacao.getGeradaPorIA(),
            avaliacao.getAnaliseIA(),
            avaliacao.getStatusAvaliacao().getLabel(),
            avaliacao.getDataAprovacao(),
            avaliacao.getMotivoRejeicao()
        );
    }
}

