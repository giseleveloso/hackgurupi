package br.unitins.topicos1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Academico extends Usuario {
    
    @Column(length = 150)
    private String instituicao;
    
    @Column(length = 100)
    private String curso;
    
    @Column(length = 200)
    private String lattes;
    
    @Column(name = "area_atuacao", length = 100)
    private String areaAtuacao;
    
    @Column(name = "vinculo_professor")
    private Boolean vinculoProfessor = false;

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getLattes() {
        return lattes;
    }

    public void setLattes(String lattes) {
        this.lattes = lattes;
    }

    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    public Boolean getVinculoProfessor() {
        return vinculoProfessor;
    }

    public void setVinculoProfessor(Boolean vinculoProfessor) {
        this.vinculoProfessor = vinculoProfessor;
    }
}

