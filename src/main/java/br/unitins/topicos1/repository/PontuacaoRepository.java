package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Pontuacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PontuacaoRepository implements PanacheRepository<Pontuacao> {
    
    public List<Pontuacao> findByUsuario(Long usuarioId) {
        return find("usuario.id = ?1 ORDER BY dataObtencao DESC", usuarioId).list();
    }
    
    public Long sumByUsuario(Long usuarioId) {
        return find("SELECT SUM(p.pontos) FROM Pontuacao p WHERE p.usuario.id = ?1", usuarioId)
            .project(Long.class)
            .firstResult();
    }
}

