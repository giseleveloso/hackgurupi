package br.unitins.topicos1.resource;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/init")
@Produces(MediaType.APPLICATION_JSON)
public class InitResource {
    
    @Inject
    EntityManager em;
    
    @POST
    @Path("/fix-sequences")
    @Transactional
    public Response fixSequences() {
        try {
            // Ajustar sequences para o próximo ID disponível
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('usuario', 'id'), COALESCE((SELECT MAX(id) FROM Usuario), 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('projeto', 'id'), COALESCE((SELECT MAX(id) FROM Projeto), 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('voto', 'id'), COALESCE((SELECT MAX(id) FROM Voto), 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('avaliacaotecnica', 'id'), COALESCE((SELECT MAX(id) FROM AvaliacaoTecnica), 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('comentario', 'id'), COALESCE((SELECT MAX(id) FROM Comentario), 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('desafio', 'id'), COALESCE((SELECT MAX(id) FROM Desafio), 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('repositoriodados', 'id'), COALESCE((SELECT MAX(id) FROM RepositorioDados), 1))").getSingleResult();
            em.createNativeQuery("SELECT setval(pg_get_serial_sequence('pontuacao', 'id'), COALESCE((SELECT MAX(id) FROM Pontuacao), 1))").getSingleResult();
            
            return Response.ok()
                .entity("{\"message\": \"✅ Sequences ajustadas com sucesso! Agora você pode criar novos registros sem conflitos de ID.\", \"success\": true}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\", \"success\": false}")
                .build();
        }
    }
}

