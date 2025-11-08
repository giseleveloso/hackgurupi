package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.RepositorioDados;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RepositorioDadosRepository implements PanacheRepository<RepositorioDados> {
    
    public List<RepositorioDados> findAtivos() {
        return find("ativo = true ORDER BY nome").list();
    }
    
    public List<RepositorioDados> findByCategoria(String categoria) {
        return find("UPPER(categoriaDados) LIKE ?1 AND ativo = true", "%" + categoria.toUpperCase() + "%").list();
    }
}

