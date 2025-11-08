package br.unitins.topicos1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProjetoDesafio extends DefaultEntity {
    
    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
    
    @ManyToOne
    @JoinColumn(name = "desafio_id", nullable = false)
    private Desafio desafio;
    
    @Column(name = "data_vinculacao", nullable = false)
    private LocalDateTime dataVinculacao;

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }

    public LocalDateTime getDataVinculacao() {
        return dataVinculacao;
    }

    public void setDataVinculacao(LocalDateTime dataVinculacao) {
        this.dataVinculacao = dataVinculacao;
    }
}

