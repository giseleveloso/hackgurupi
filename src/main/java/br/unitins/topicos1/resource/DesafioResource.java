package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.DesafioDTO;
import br.unitins.topicos1.dto.DesafioResponseDTO;
import br.unitins.topicos1.model.Desafio;
import br.unitins.topicos1.service.DesafioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/desafios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DesafioResource {
    
    @Inject
    DesafioService service;
    
    @GET
    public Response findAll() {
        List<DesafioResponseDTO> desafios = service.findAll().stream()
            .map(DesafioResponseDTO::valueOf)
            .toList();
        return Response.ok(desafios).build();
    }
    
    @GET
    @Path("/ativos")
    public Response findAtivos() {
        List<DesafioResponseDTO> desafios = service.findAtivos().stream()
            .map(DesafioResponseDTO::valueOf)
            .toList();
        return Response.ok(desafios).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            Desafio desafio = service.findById(id);
            return Response.ok(DesafioResponseDTO.valueOf(desafio)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    public Response create(DesafioDTO dto) {
        try {
            Desafio desafio = service.create(dto);
            return Response.status(Response.Status.CREATED)
                .entity(DesafioResponseDTO.valueOf(desafio))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, DesafioDTO dto) {
        try {
            Desafio desafio = service.update(id, dto);
            return Response.ok(DesafioResponseDTO.valueOf(desafio)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    @Path("/{id}/publicar")
    public Response publicar(@PathParam("id") Long id) {
        try {
            service.publicar(id);
            return Response.ok("{\"message\": \"Desafio publicado\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    @Path("/{id}/encerrar")
    public Response encerrar(@PathParam("id") Long id) {
        try {
            service.encerrar(id);
            return Response.ok("{\"message\": \"Desafio encerrado\"}").build();
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
}

