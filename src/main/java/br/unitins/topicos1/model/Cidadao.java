package br.unitins.topicos1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Cidadao extends Usuario {
    
    @Column(length = 9, nullable = false)
    private String cep;
    
    @Column(length = 100)
    private String bairro;
    
    @Column(name = "interesses_areas", length = 255)
    private String interessesAreas;

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getInteressesAreas() {
        return interessesAreas;
    }

    public void setInteressesAreas(String interessesAreas) {
        this.interessesAreas = interessesAreas;
    }
}

