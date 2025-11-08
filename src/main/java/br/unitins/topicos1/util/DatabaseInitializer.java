package br.unitins.topicos1.util;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;

@ApplicationScoped
public class DatabaseInitializer {

    @Inject
    EntityManager em;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        try {
            System.out.println("🔧 Corrigindo sequences do banco de dados...");
            
            // Detectar o tipo de banco de dados usando Hibernate Session
            String databaseProductName = em.unwrap(Session.class)
                .doReturningWork(connection -> connection.getMetaData().getDatabaseProductName());
            
            System.out.println("📊 Banco de dados detectado: " + databaseProductName);
            
            if (databaseProductName.toLowerCase().contains("postgres")) {
                // PostgreSQL
                fixSequencesPostgreSQL();
            } else if (databaseProductName.toLowerCase().contains("mysql") || 
                       databaseProductName.toLowerCase().contains("mariadb")) {
                // MySQL/MariaDB
                fixSequencesMySQL();
            } else {
                System.out.println("⚠️ Banco de dados não suportado para correção automática de IDs");
            }
            
            System.out.println("✅ Sequences/Auto-increment corrigidos com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro ao corrigir sequences: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void fixSequencesPostgreSQL() {
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('usuario', 'id'), COALESCE((SELECT MAX(id) FROM Usuario) + 1, 1))").getSingleResult();
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('projeto', 'id'), COALESCE((SELECT MAX(id) FROM Projeto) + 1, 1))").getSingleResult();
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('voto', 'id'), COALESCE((SELECT MAX(id) FROM Voto) + 1, 1))").getSingleResult();
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('avaliacaotecnica', 'id'), COALESCE((SELECT MAX(id) FROM AvaliacaoTecnica) + 1, 1))").getSingleResult();
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('comentario', 'id'), COALESCE((SELECT MAX(id) FROM Comentario) + 1, 1))").getSingleResult();
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('desafio', 'id'), COALESCE((SELECT MAX(id) FROM Desafio) + 1, 1))").getSingleResult();
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('repositoriodados', 'id'), COALESCE((SELECT MAX(id) FROM RepositorioDados) + 1, 1))").getSingleResult();
        em.createNativeQuery("SELECT setval(pg_get_serial_sequence('pontuacao', 'id'), COALESCE((SELECT MAX(id) FROM Pontuacao) + 1, 1))").getSingleResult();
    }
    
    private void fixSequencesMySQL() {
        fixAutoIncrementMySQL("Usuario");
        fixAutoIncrementMySQL("Academico");
        fixAutoIncrementMySQL("Cidadao");
        fixAutoIncrementMySQL("GestorPrefeitura");
        fixAutoIncrementMySQL("Projeto");
        fixAutoIncrementMySQL("Voto");
        fixAutoIncrementMySQL("AvaliacaoTecnica");
        fixAutoIncrementMySQL("Comentario");
        fixAutoIncrementMySQL("Desafio");
        fixAutoIncrementMySQL("RepositorioDados");
        fixAutoIncrementMySQL("Pontuacao");
    }
    
    private void fixAutoIncrementMySQL(String tableName) {
        try {
            Object result = em.createNativeQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM " + tableName).getSingleResult();
            Long nextId = ((Number) result).longValue();
            em.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = " + nextId).executeUpdate();
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao corrigir " + tableName + ": " + e.getMessage());
        }
    }
}

