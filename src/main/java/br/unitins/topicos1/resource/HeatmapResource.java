package br.unitins.topicos1.resource;

import java.util.List;

import br.unitins.topicos1.dto.HeatPointDTO;
import br.unitins.topicos1.service.HeatmapService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/heatmap")
@Produces(MediaType.APPLICATION_JSON)
public class HeatmapResource {
    
    @Inject
    HeatmapService heatmapService;
    
    /**
     * GET /heatmap - Retorna dados do mapa de calor geral (todos os votos)
     * 
     * @param precision Precisão da agregação (3-5, padrão 3)
     * @return Lista de pontos para o mapa de calor
     */
    @GET
    public Response getGeneralHeatmap(
        @QueryParam("precision") Integer precision
    ) {
        try {
            int prec = precision != null && precision >= 3 && precision <= 5 ? precision : 3;
            List<HeatPointDTO> heatPoints = heatmapService.generateHeatmap(null, prec);
            return Response.ok(heatPoints).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    /**
     * GET /heatmap/projeto/{projetoId} - Retorna mapa de calor para um projeto específico
     * 
     * @param projetoId ID do projeto
     * @param precision Precisão da agregação (3-5, padrão 3)
     * @return Lista de pontos para o mapa de calor
     */
    @GET
    @Path("/projeto/{projetoId}")
    public Response getProjetoHeatmap(
        @PathParam("projetoId") Long projetoId,
        @QueryParam("precision") Integer precision
    ) {
        try {
            int prec = precision != null && precision >= 3 && precision <= 5 ? precision : 3;
            List<HeatPointDTO> heatPoints = heatmapService.generateHeatmap(projetoId, prec);
            return Response.ok(heatPoints).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
}

