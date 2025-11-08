package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.GestorPrefeituraDTO;
import br.unitins.topicos1.dto.GestorPrefeituraResponseDTO;
import br.unitins.topicos1.model.GestorPrefeitura;
import br.unitins.topicos1.service.GestorPrefeituraService;
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

@Path("/gestores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestorPrefeituraResource {
    
    @Inject
    GestorPrefeituraService service;
    
    @GET
    public Response findAll() {
        List<GestorPrefeituraResponseDTO> gestores = service.findAll().stream()
            .map(GestorPrefeituraResponseDTO::valueOf)
            .toList();
        return Response.ok(gestores).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            GestorPrefeitura gestor = service.findById(id);
            return Response.ok(GestorPrefeituraResponseDTO.valueOf(gestor)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    public Response create(GestorPrefeituraDTO dto) {
        try {
            GestorPrefeitura gestor = service.create(dto);
            return Response.status(Response.Status.CREATED)
                .entity(GestorPrefeituraResponseDTO.valueOf(gestor))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, GestorPrefeituraDTO dto) {
        try {
            GestorPrefeitura gestor = service.update(id, dto);
            return Response.ok(GestorPrefeituraResponseDTO.valueOf(gestor)).build();
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

