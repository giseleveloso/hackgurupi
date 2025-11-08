package br.unitins.topicos1.util;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DatabaseInitializer {

    @Inject
    EntityManager em;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        try {
            System.out.println("🔧 Corrigindo sequences do banco de dados...");
            
            // Ajustar sequences para o próximo ID disponível
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('usuario', 'id'), COALESCE((SELECT MAX(id) FROM Usuario) + 1, 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('projeto', 'id'), COALESCE((SELECT MAX(id) FROM Projeto) + 1, 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('voto', 'id'), COALESCE((SELECT MAX(id) FROM Voto) + 1, 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('avaliacaotecnica', 'id'), COALESCE((SELECT MAX(id) FROM AvaliacaoTecnica) + 1, 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('comentario', 'id'), COALESCE((SELECT MAX(id) FROM Comentario) + 1, 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('desafio', 'id'), COALESCE((SELECT MAX(id) FROM Desafio) + 1, 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('repositoriodados', 'id'), COALESCE((SELECT MAX(id) FROM RepositorioDados) + 1, 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('pontuacao', 'id'), COALESCE((SELECT MAX(id) FROM Pontuacao) + 1, 1))").getSingleResult();
            
            System.out.println("✅ Sequences corrigidas com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro ao corrigir sequences: " + e.getMessage());
        }
    }
}

