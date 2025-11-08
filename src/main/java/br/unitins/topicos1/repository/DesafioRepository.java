package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Desafio;
import br.unitins.topicos1.model.StatusDesafio;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DesafioRepository implements PanacheRepository<Desafio> {
    
    public List<Desafio> findByStatus(StatusDesafio status) {
        return find("status = ?1 ORDER BY prioridade DESC, dataInicio DESC", status.getId()).list();
    }
    
    public List<Desafio> findAtivos() {
        return find("status = ?1 ORDER BY prioridade DESC", StatusDesafio.ATIVO.getId()).list();
    }
}

