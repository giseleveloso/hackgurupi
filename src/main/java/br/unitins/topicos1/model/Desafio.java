package br.unitins.topicos1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Desafio extends DefaultEntity {
    
    @Column(length = 200, nullable = false)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "area_tematica")
    private Integer areaTematica;
    
    @Column(nullable = false)
    private Integer status = StatusDesafio.PLANEJAMENTO.getId();
    
    @Column(name = "orcamento_disponivel", precision = 12, scale = 2)
    private BigDecimal orcamentoDisponivel;
    
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    
    @Column(name = "data_fim")
    private LocalDate dataFim;
    
    @Column
    private Integer prioridade = 1;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public AreaTematica getAreaTematica() {
        return AreaTematica.valueOf(areaTematica);
    }

    public void setAreaTematica(AreaTematica areaTematica) {
        this.areaTematica = areaTematica.getId();
    }

    public StatusDesafio getStatus() {
        return StatusDesafio.valueOf(status);
    }

    public void setStatus(StatusDesafio status) {
        this.status = status.getId();
    }

    public BigDecimal getOrcamentoDisponivel() {
        return orcamentoDisponivel;
    }

    public void setOrcamentoDisponivel(BigDecimal orcamentoDisponivel) {
        this.orcamentoDisponivel = orcamentoDisponivel;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }
}

