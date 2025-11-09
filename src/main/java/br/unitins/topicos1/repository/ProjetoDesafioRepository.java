package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.ProjetoDesafio;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjetoDesafioRepository implements PanacheRepository<ProjetoDesafio> {
    
    public List<ProjetoDesafio> findByDesafio(Long desafioId) {
        return find("desafio.id = ?1", desafioId).list();
    }
    
    public Long countByDesafio(Long desafioId) {
        return count("desafio.id", desafioId);
    }
}

