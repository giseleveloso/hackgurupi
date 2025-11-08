package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.LoginDTO;
import br.unitins.topicos1.dto.SolicitarCodigoDTO;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    @Inject
    AuthService authService;
    
    @POST
    @Path("/solicitar-codigo")
    public Response solicitarCodigo(SolicitarCodigoDTO dto) {
        try {
            authService.solicitarCodigo(dto.email());
            return Response.ok()
                .entity("{\"message\": \"Código enviado para o email\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @POST
    @Path("/login")
    public Response login(LoginDTO dto) {
        try {
            Usuario usuario = authService.autenticar(dto.email(), dto.codigo());
            return Response.ok()
                .entity("{\"message\": \"Login realizado com sucesso\", \"usuarioId\": " + usuario.getId() + ", \"tipoUsuario\": \"" + usuario.getTipoUsuario().getLabel() + "\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }
}
