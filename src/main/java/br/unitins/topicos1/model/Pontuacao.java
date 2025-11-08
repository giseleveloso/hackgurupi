package br.unitins.topicos1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Pontuacao extends DefaultEntity {
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private Integer pontos;
    
    @Column(name = "tipo_acao", length = 50)
    private String tipoAcao;
    
    @Column(length = 255)
    private String descricao;
    
    @Column(name = "data_obtencao", nullable = false)
    private LocalDateTime dataObtencao;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public String getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(String tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataObtencao() {
        return dataObtencao;
    }

    public void setDataObtencao(LocalDateTime dataObtencao) {
        this.dataObtencao = dataObtencao;
    }
}

