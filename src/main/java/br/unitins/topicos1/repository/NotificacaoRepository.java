package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Notificacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificacaoRepository implements PanacheRepository<Notificacao> {
    
    public List<Notificacao> findByUsuario(Long usuarioId) {
        return find("usuario.id = ?1 ORDER BY dataEnvio DESC", usuarioId).list();
    }
    
    public List<Notificacao> findNaoLidas(Long usuarioId) {
        return find("usuario.id = ?1 AND lida = false ORDER BY dataEnvio DESC", usuarioId).list();
    }
    
    public Long countNaoLidas(Long usuarioId) {
        return count("usuario.id = ?1 AND lida = false", usuarioId);
    }
}

