package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.ComentarioDTO;
import br.unitins.topicos1.dto.ComentarioResponseDTO;
import br.unitins.topicos1.model.Comentario;
import br.unitins.topicos1.service.ComentarioService;
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

@Path("/comentarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComentarioResource {
    
    @Inject
    ComentarioService service;
    
    @GET
    @Path("/projeto/{projetoId}")
    public Response findByProjeto(@PathParam("projetoId") Long projetoId) {
        List<ComentarioResponseDTO> comentarios = service.findByProjeto(projetoId).stream()
            .map(ComentarioResponseDTO::valueOf)
            .toList();
        return Response.ok(comentarios).build();
    }
    
    @POST
    public Response criar(ComentarioDTO dto) {
        try {
            Comentario comentario = service.criar(dto);
            return Response.status(Response.Status.CREATED)
                .entity(ComentarioResponseDTO.valueOf(comentario))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response editar(@PathParam("id") Long id, @QueryParam("texto") String texto) {
        try {
            Comentario comentario = service.editar(id, texto);
            return Response.ok(ComentarioResponseDTO.valueOf(comentario)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        try {
            service.deletar(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
}

