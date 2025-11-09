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
    
    public List<AvaliacaoTecnica> findPendentes() {
        return find("statusAvaliacao = ?1 ORDER BY dataAvaliacao DESC", 1).list(); // 1 = PENDENTE_APROVACAO
    }
    
    public List<AvaliacaoTecnica> findByStatus(Integer statusId) {
        return find("statusAvaliacao = ?1 ORDER BY dataAvaliacao DESC", statusId).list();
    }
    
    public List<AvaliacaoTecnica> findGeradasPorIA() {
        return find("geradaPorIA = true ORDER BY dataAvaliacao DESC").list();
    }
    
    public Long countPendentes() {
        return count("statusAvaliacao = ?1", 1); // 1 = PENDENTE_APROVACAO
    }
}

