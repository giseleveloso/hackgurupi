package br.unitins.topicos1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AvaliacaoTecnica extends DefaultEntity {
    
    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
    
    @ManyToOne
    @JoinColumn(name = "gestor_id", nullable = false)
    private GestorPrefeitura gestor;
    
    @Column(precision = 4, scale = 2)
    private BigDecimal nota;
    
    @Column(name = "criterio_viabilidade", precision = 4, scale = 2)
    private BigDecimal criterioViabilidade;
    
    @Column(name = "criterio_impacto", precision = 4, scale = 2)
    private BigDecimal criterioImpacto;
    
    @Column(name = "criterio_inovacao", precision = 4, scale = 2)
    private BigDecimal criterioInovacao;
    
    @Column(name = "criterio_orcamento", precision = 4, scale = 2)
    private BigDecimal criterioOrcamento;
    
    @Column(columnDefinition = "TEXT")
    private String justificativa;
    
    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;
    
    // Campos para avaliação por IA
    @Column(name = "gerada_por_ia", nullable = false)
    private Boolean geradaPorIA = false;
    
    @Column(name = "analise_ia", columnDefinition = "TEXT")
    private String analiseIA;
    
    @Column(name = "status_avaliacao", nullable = false)
    private Integer statusAvaliacao = StatusAvaliacao.APROVADA.getId();
    
    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;
    
    @Column(name = "motivo_rejeicao", columnDefinition = "TEXT")
    private String motivoRejeicao;

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public GestorPrefeitura getGestor() {
        return gestor;
    }

    public void setGestor(GestorPrefeitura gestor) {
        this.gestor = gestor;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public BigDecimal getCriterioViabilidade() {
        return criterioViabilidade;
    }

    public void setCriterioViabilidade(BigDecimal criterioViabilidade) {
        this.criterioViabilidade = criterioViabilidade;
    }

    public BigDecimal getCriterioImpacto() {
        return criterioImpacto;
    }

    public void setCriterioImpacto(BigDecimal criterioImpacto) {
        this.criterioImpacto = criterioImpacto;
    }

    public BigDecimal getCriterioInovacao() {
        return criterioInovacao;
    }

    public void setCriterioInovacao(BigDecimal criterioInovacao) {
        this.criterioInovacao = criterioInovacao;
    }

    public BigDecimal getCriterioOrcamento() {
        return criterioOrcamento;
    }

    public void setCriterioOrcamento(BigDecimal criterioOrcamento) {
        this.criterioOrcamento = criterioOrcamento;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public Boolean getGeradaPorIA() {
        return geradaPorIA;
    }

    public void setGeradaPorIA(Boolean geradaPorIA) {
        this.geradaPorIA = geradaPorIA;
    }

    public String getAnaliseIA() {
        return analiseIA;
    }

    public void setAnaliseIA(String analiseIA) {
        this.analiseIA = analiseIA;
    }

    public StatusAvaliacao getStatusAvaliacao() {
        return StatusAvaliacao.valueOf(statusAvaliacao);
    }

    public void setStatusAvaliacao(StatusAvaliacao statusAvaliacao) {
        this.statusAvaliacao = statusAvaliacao.getId();
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }
}

