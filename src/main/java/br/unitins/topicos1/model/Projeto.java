package br.unitins.topicos1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Projeto extends DefaultEntity {
    
    @ManyToOne
    @JoinColumn(name = "academico_id", nullable = false)
    private Academico academico;
    
    @Column(length = 200, nullable = false)
    private String titulo;
    
    @Column(name = "resumo_popular", length = 280, nullable = false)
    private String resumoPopular;
    
    @Column(name = "descricao_completa", columnDefinition = "TEXT")
    private String descricaoCompleta;
    
    @Column(columnDefinition = "TEXT")
    private String objetivos;
    
    @Column(columnDefinition = "TEXT")
    private String metodologia;
    
    @Column(name = "resultados_esperados", columnDefinition = "TEXT")
    private String resultadosEsperados;
    
    @Column(name = "orcamento_estimado", precision = 12, scale = 2)
    private BigDecimal orcamentoEstimado;
    
    @Column(name = "prazo_execucao")
    private Integer prazoExecucao;
    
    @Column(name = "area_tematica", nullable = false)
    private Integer areaTematica;
    
    @Column(nullable = false)
    private Integer status = StatusProjeto.RASCUNHO.getId();
    
    @Column(name = "data_submissao")
    private LocalDateTime dataSubmissao;
    
    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;
    
    @Column(name = "total_votos")
    private Integer totalVotos = 0;
    
    @Column(name = "total_visualizacoes")
    private Integer totalVisualizacoes = 0;
    
    @Column(name = "nota_tecnica", precision = 4, scale = 2)
    private BigDecimal notaTecnica;
    
    @Column(name = "nota_popular", precision = 4, scale = 2)
    private BigDecimal notaPopular;
    
    @Column(name = "nota_final", precision = 4, scale = 2)
    private BigDecimal notaFinal;

    public Academico getAcademico() {
        return academico;
    }

    public void setAcademico(Academico academico) {
        this.academico = academico;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumoPopular() {
        return resumoPopular;
    }

    public void setResumoPopular(String resumoPopular) {
        this.resumoPopular = resumoPopular;
    }

    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }

    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getMetodologia() {
        return metodologia;
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public String getResultadosEsperados() {
        return resultadosEsperados;
    }

    public void setResultadosEsperados(String resultadosEsperados) {
        this.resultadosEsperados = resultadosEsperados;
    }

    public BigDecimal getOrcamentoEstimado() {
        return orcamentoEstimado;
    }

    public void setOrcamentoEstimado(BigDecimal orcamentoEstimado) {
        this.orcamentoEstimado = orcamentoEstimado;
    }

    public Integer getPrazoExecucao() {
        return prazoExecucao;
    }

    public void setPrazoExecucao(Integer prazoExecucao) {
        this.prazoExecucao = prazoExecucao;
    }

    public AreaTematica getAreaTematica() {
        return AreaTematica.valueOf(areaTematica);
    }

    public void setAreaTematica(AreaTematica areaTematica) {
        this.areaTematica = areaTematica.getId();
    }

    public StatusProjeto getStatus() {
        return StatusProjeto.valueOf(status);
    }

    public void setStatus(StatusProjeto status) {
        this.status = status.getId();
    }

    public LocalDateTime getDataSubmissao() {
        return dataSubmissao;
    }

    public void setDataSubmissao(LocalDateTime dataSubmissao) {
        this.dataSubmissao = dataSubmissao;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public Integer getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(Integer totalVotos) {
        this.totalVotos = totalVotos;
    }

    public Integer getTotalVisualizacoes() {
        return totalVisualizacoes;
    }

    public void setTotalVisualizacoes(Integer totalVisualizacoes) {
        this.totalVisualizacoes = totalVisualizacoes;
    }

    public BigDecimal getNotaTecnica() {
        return notaTecnica;
    }

    public void setNotaTecnica(BigDecimal notaTecnica) {
        this.notaTecnica = notaTecnica;
    }

    public BigDecimal getNotaPopular() {
        return notaPopular;
    }

    public void setNotaPopular(BigDecimal notaPopular) {
        this.notaPopular = notaPopular;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }
}

