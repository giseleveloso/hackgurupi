package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.DashboardResponseDTO;
import br.unitins.topicos1.service.DashboardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {
    
    @Inject
    DashboardService service;
    
    @GET
    public Response getDashboard() {
        DashboardResponseDTO dashboard = service.getDashboard();
        return Response.ok(dashboard).build();
    }
}

