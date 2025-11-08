package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.AcademicoDTO;
import br.unitins.topicos1.dto.AcademicoResponseDTO;
import br.unitins.topicos1.model.Academico;
import br.unitins.topicos1.service.AcademicoService;
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

@Path("/academicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AcademicoResource {
    
    @Inject
    AcademicoService service;
    
    @GET
    public Response findAll() {
        List<AcademicoResponseDTO> academicos = service.findAll().stream()
            .map(AcademicoResponseDTO::valueOf)
            .toList();
        return Response.ok(academicos).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            Academico academico = service.findById(id);
            return Response.ok(AcademicoResponseDTO.valueOf(academico)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    public Response create(AcademicoDTO dto) {
        try {
            Academico academico = service.create(dto);
            return Response.status(Response.Status.CREATED)
                .entity(AcademicoResponseDTO.valueOf(academico))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /*
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, AcademicoDTO dto) {
        try {
            Academico academico = service.update(id, dto);
            return Response.ok(AcademicoResponseDTO.valueOf(academico)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
     */
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

