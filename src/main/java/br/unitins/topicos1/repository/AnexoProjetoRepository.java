package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.AnexoProjeto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnexoProjetoRepository implements PanacheRepository<AnexoProjeto> {
    
    public List<AnexoProjeto> findByProjeto(Long projetoId) {
        return find("projeto.id = ?1 ORDER BY dataUpload DESC", projetoId).list();
    }
}

