package br.unitins.topicos1.repository;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.model.Voto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VotoRepository implements PanacheRepository<Voto> {
    
    public List<Voto> findByProjeto(Long projetoId) {
        return find("projeto.id = ?1 ORDER BY dataVoto DESC", projetoId).list();
    }
    
    public List<Voto> findByCidadao(Long cidadaoId) {
        return find("cidadao.id = ?1 ORDER BY dataVoto DESC", cidadaoId).list();
    }
    
    public Voto findByProjetoAndCidadao(Long projetoId, Long cidadaoId) {
        return find("projeto.id = ?1 AND cidadao.id = ?2", projetoId, cidadaoId).firstResult();
    }
    
    public Long countByProjeto(Long projetoId) {
        return count("projeto.id", projetoId);
    }
    
    public Long countFromDate(LocalDateTime dataInicial) {
        return count("dataVoto >= ?1", dataInicial);
    }
}