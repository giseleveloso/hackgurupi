package br.unitins.topicos1.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.model.StatusProjeto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/status-projetos")
@Produces(MediaType.APPLICATION_JSON)
public class StatusProjetoResource {

    @GET
    public Response findAll() {
        List<StatusProjetoDTO> status = Arrays.stream(StatusProjeto.values())
            .map(item -> new StatusProjetoDTO(item.getId(), item.getLabel()))
            .collect(Collectors.toList());
        return Response.ok(status).build();
    }

    public record StatusProjetoDTO(Integer id, String label) {}
}

