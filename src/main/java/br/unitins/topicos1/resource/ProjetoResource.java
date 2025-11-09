package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.ProjetoDTO;
import br.unitins.topicos1.dto.ProjetoResponseDTO;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.service.ProjetoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/projetos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjetoResource {
    
    @Inject
    ProjetoService service;
    
    @GET
    public Response findAll() {
        List<ProjetoResponseDTO> projetos = service.findAll().stream()
            .map(ProjetoResponseDTO::valueOf)
            .toList();
        return Response.ok(projetos).build();
    }
    
    @GET
    @Path("/publicos")
    public Response findPublicos() {
        List<ProjetoResponseDTO> projetos = service.findPublicos().stream()
            .map(ProjetoResponseDTO::valueOf)
            .toList();
        return Response.ok(projetos).build();
    }
    
    @GET
    @Path("/top-votados")
    public Response findTopVotados(@QueryParam("limit") Integer limit) {
        int limitValue = limit != null ? limit : 10;
        List<ProjetoResponseDTO> projetos = service.findTopVotados(limitValue).stream()
            .map(ProjetoResponseDTO::valueOf)
            .toList();
        return Response.ok(projetos).build();
    }
    
    @GET
    @Path("/academico/{academicoId}")
    public Response findByAcademico(
        @PathParam("academicoId") Long academicoId,
        @QueryParam("statusId") Integer statusId,
        @QueryParam("areaId") Integer areaId,
        @QueryParam("q") String q,
        @QueryParam("order") String order
    ) {
        List<ProjetoResponseDTO> projetos = service
            .findByAcademicoWithFilters(academicoId, statusId, areaId, q, order).stream()
            .map(ProjetoResponseDTO::valueOf)
            .toList();
        return Response.ok(projetos).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            Projeto projeto = service.findById(id);
            service.incrementarVisualizacoes(id);
            return Response.ok(ProjetoResponseDTO.valueOf(projeto)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    public Response create(ProjetoDTO dto) {
        try {
            Projeto projeto = service.create(dto);
            return Response.status(Response.Status.CREATED)
                .entity(ProjetoResponseDTO.valueOf(projeto))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ProjetoDTO dto) {
        try {
            Projeto projeto = service.update(id, dto);
            return Response.ok(ProjetoResponseDTO.valueOf(projeto)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    @Path("/{id}/submeter")
    public Response submeter(@PathParam("id") Long id) {
        try {
            service.submeter(id);
            return Response.ok("{\"message\": \"Projeto submetido com sucesso\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    @Path("/{id}/aprovar")
    public Response aprovar(@PathParam("id") Long id) {
        try {
            service.aprovar(id);
            return Response.ok("{\"message\": \"Projeto aprovado com sucesso\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    @Path("/{id}/rejeitar")
    public Response rejeitar(@PathParam("id") Long id, @QueryParam("motivo") String motivo) {
        try {
            service.rejeitar(id, motivo);
            return Response.ok("{\"message\": \"Projeto rejeitado\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            service.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    @Path("/sincronizar-votos")
    public Response sincronizarVotos() {
        try {
            service.sincronizarTodosProjetos();
            return Response.ok("{\"message\": \"Votos sincronizados com sucesso\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
}

