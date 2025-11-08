package br.unitins.topicos1.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.model.AreaTematica;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/areas-tematicas")
@Produces(MediaType.APPLICATION_JSON)
public class AreaTematicaResource {
    
    @GET
    public Response findAll() {
        List<AreaTematicaDTO> areas = Arrays.stream(AreaTematica.values())
            .map(area -> new AreaTematicaDTO(area.getId(), area.getLabel()))
            .collect(Collectors.toList());
        return Response.ok(areas).build();
    }
    
    public record AreaTematicaDTO(Integer id, String label) {}
}

