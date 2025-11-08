package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Comentario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ComentarioRepository implements PanacheRepository<Comentario> {
    
    public List<Comentario> findByProjeto(Long projetoId) {
        return find("projeto.id = ?1 ORDER BY dataComentario DESC", projetoId).list();
    }
    
    public List<Comentario> findByCidadao(Long cidadaoId) {
        return find("cidadao.id = ?1 ORDER BY dataComentario DESC", cidadaoId).list();
    }
    
    public Long countByProjeto(Long projetoId) {
        return count("projeto.id", projetoId);
    }
}

