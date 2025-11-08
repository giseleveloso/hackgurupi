package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.VotoDTO;
import br.unitins.topicos1.dto.VotoResponseDTO;
import br.unitins.topicos1.model.Voto;
import br.unitins.topicos1.service.VotoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/votos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotoResource {
    
    @Inject
    VotoService service;
    
    @Context
    UriInfo uriInfo;
    
    @GET
    @Path("/projeto/{projetoId}")
    public Response findByProjeto(@PathParam("projetoId") Long projetoId) {
        List<VotoResponseDTO> votos = service.findByProjeto(projetoId).stream()
            .map(VotoResponseDTO::valueOf)
            .toList();
        return Response.ok(votos).build();
    }
    
    @GET
    @Path("/cidadao/{cidadaoId}")
    public Response findByCidadao(@PathParam("cidadaoId") Long cidadaoId) {
        List<VotoResponseDTO> votos = service.findByCidadao(cidadaoId).stream()
            .map(VotoResponseDTO::valueOf)
            .toList();
        return Response.ok(votos).build();
    }
    
    @POST
    public Response votar(VotoDTO dto) {
        try {
            String ipAddress = "127.0.0.1"; // Simplificado para MVP
            Voto voto = service.votar(dto, ipAddress);
            return Response.status(Response.Status.CREATED)
                .entity(VotoResponseDTO.valueOf(voto))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @DELETE
    @Path("/projeto/{projetoId}/cidadao/{cidadaoId}")
    public Response removerVoto(@PathParam("projetoId") Long projetoId, @PathParam("cidadaoId") Long cidadaoId) {
        try {
            service.removerVoto(projetoId, cidadaoId);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
}

