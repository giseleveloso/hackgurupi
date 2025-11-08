package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.CidadaoDTO;
import br.unitins.topicos1.dto.CidadaoResponseDTO;
import br.unitins.topicos1.model.Cidadao;
import br.unitins.topicos1.service.CidadaoService;
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

@Path("/cidadaos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CidadaoResource {
    
    @Inject
    CidadaoService service;
    
    @GET
    public Response findAll() {
        List<CidadaoResponseDTO> cidadaos = service.findAll().stream()
            .map(CidadaoResponseDTO::valueOf)
            .toList();
        return Response.ok(cidadaos).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            Cidadao cidadao = service.findById(id);
            return Response.ok(CidadaoResponseDTO.valueOf(cidadao)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    public Response create(CidadaoDTO dto) {
        try {
            Cidadao cidadao = service.create(dto);
            return Response.status(Response.Status.CREATED)
                .entity(CidadaoResponseDTO.valueOf(cidadao))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, CidadaoDTO dto) {
        try {
            Cidadao cidadao = service.update(id, dto);
            return Response.ok(CidadaoResponseDTO.valueOf(cidadao)).build();
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

