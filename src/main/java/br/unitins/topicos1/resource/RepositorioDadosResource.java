package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.RepositorioDadosDTO;
import br.unitins.topicos1.dto.RepositorioDadosResponseDTO;
import br.unitins.topicos1.model.RepositorioDados;
import br.unitins.topicos1.service.RepositorioDadosService;
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

@Path("/repositorios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RepositorioDadosResource {
    
    @Inject
    RepositorioDadosService service;
    
    @GET
    public Response findAll() {
        List<RepositorioDadosResponseDTO> repositorios = service.findAtivos().stream()
            .map(RepositorioDadosResponseDTO::valueOf)
            .toList();
        return Response.ok(repositorios).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            RepositorioDados repositorio = service.findById(id);
            return Response.ok(RepositorioDadosResponseDTO.valueOf(repositorio)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    public Response create(RepositorioDadosDTO dto) {
        try {
            RepositorioDados repositorio = service.create(dto);
            return Response.status(Response.Status.CREATED)
                .entity(RepositorioDadosResponseDTO.valueOf(repositorio))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, RepositorioDadosDTO dto) {
        try {
            RepositorioDados repositorio = service.update(id, dto);
            return Response.ok(RepositorioDadosResponseDTO.valueOf(repositorio)).build();
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

