package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.AvaliacaoTecnicaDTO;
import br.unitins.topicos1.dto.AvaliacaoTecnicaResponseDTO;
import br.unitins.topicos1.model.AvaliacaoTecnica;
import br.unitins.topicos1.service.AvaliacaoTecnicaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/avaliacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AvaliacaoTecnicaResource {
    
    @Inject
    AvaliacaoTecnicaService service;
    
    @GET
    @Path("/projeto/{projetoId}")
    public Response findByProjeto(@PathParam("projetoId") Long projetoId) {
        List<AvaliacaoTecnicaResponseDTO> avaliacoes = service.findByProjeto(projetoId).stream()
            .map(AvaliacaoTecnicaResponseDTO::valueOf)
            .toList();
        return Response.ok(avaliacoes).build();
    }
    
    @POST
    public Response avaliar(AvaliacaoTecnicaDTO dto) {
        try {
            AvaliacaoTecnica avaliacao = service.avaliar(dto);
            return Response.status(Response.Status.CREATED)
                .entity(AvaliacaoTecnicaResponseDTO.valueOf(avaliacao))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
}

