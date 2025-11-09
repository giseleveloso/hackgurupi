package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.AprovarAvaliacaoDTO;
import br.unitins.topicos1.dto.AvaliacaoTecnicaDTO;
import br.unitins.topicos1.dto.AvaliacaoTecnicaResponseDTO;
import br.unitins.topicos1.dto.RejeitarAvaliacaoDTO;
import br.unitins.topicos1.dto.SolicitarAnaliseIADTO;
import br.unitins.topicos1.model.AvaliacaoTecnica;
import br.unitins.topicos1.service.AvaliacaoTecnicaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
    
    /**
     * POST /avaliacoes/solicitar-analise-ia - Solicita análise de projeto por IA
     */
    @POST
    @Path("/solicitar-analise-ia")
    public Response solicitarAnaliseIA(SolicitarAnaliseIADTO dto) {
        try {
            AvaliacaoTecnica avaliacao = service.solicitarAnaliseIA(dto.projetoId(), dto.gestorId());
            return Response.status(Response.Status.CREATED)
                .entity(AvaliacaoTecnicaResponseDTO.valueOf(avaliacao))
                .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Erro ao solicitar análise de IA: " + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * GET /avaliacoes/pendentes - Lista avaliações pendentes de aprovação
     */
    @GET
    @Path("/pendentes")
    public Response findPendentes() {
        try {
            List<AvaliacaoTecnicaResponseDTO> avaliacoes = service.findPendentes().stream()
                .map(AvaliacaoTecnicaResponseDTO::valueOf)
                .toList();
            return Response.ok(avaliacoes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * GET /avaliacoes/pendentes/count - Conta avaliações pendentes
     */
    @GET
    @Path("/pendentes/count")
    public Response countPendentes() {
        try {
            Long count = service.countPendentes();
            return Response.ok("{\"count\": " + count + "}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * PUT /avaliacoes/{id}/aprovar - Aprova uma avaliação gerada por IA
     */
    @PUT
    @Path("/{id}/aprovar")
    public Response aprovar(@PathParam("id") Long id, AprovarAvaliacaoDTO dto) {
        try {
            AvaliacaoTecnica avaliacao = service.aprovarAvaliacaoIA(id, dto.gestorId());
            return Response.ok(AvaliacaoTecnicaResponseDTO.valueOf(avaliacao)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * PUT /avaliacoes/{id}/rejeitar - Rejeita uma avaliação gerada por IA
     */
    @PUT
    @Path("/{id}/rejeitar")
    public Response rejeitar(@PathParam("id") Long id, RejeitarAvaliacaoDTO dto) {
        try {
            AvaliacaoTecnica avaliacao = service.rejeitarAvaliacaoIA(id, dto.gestorId(), dto.motivo());
            return Response.ok(AvaliacaoTecnicaResponseDTO.valueOf(avaliacao)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * GET /avaliacoes/{id} - Busca avaliação por ID
     */
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            AvaliacaoTecnica avaliacao = service.findById(id);
            return Response.ok(AvaliacaoTecnicaResponseDTO.valueOf(avaliacao)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
}

