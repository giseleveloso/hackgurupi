package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.AvaliacaoTecnica;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AvaliacaoTecnicaRepository implements PanacheRepository<AvaliacaoTecnica> {
    
    public List<AvaliacaoTecnica> findByProjeto(Long projetoId) {
        return find("projeto.id = ?1 ORDER BY dataAvaliacao DESC", projetoId).list();
    }
    
    public List<AvaliacaoTecnica> findByGestor(Long gestorId) {
        return find("gestor.id = ?1 ORDER BY dataAvaliacao DESC", gestorId).list();
    }
    
    public AvaliacaoTecnica findByProjetoAndGestor(Long projetoId, Long gestorId) {
        return find("projeto.id = ?1 AND gestor.id = ?2", projetoId, gestorId).firstResult();
    }
}

