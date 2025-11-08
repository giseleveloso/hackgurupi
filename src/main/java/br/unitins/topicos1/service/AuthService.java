package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.util.Random;

import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Transactional
    public void solicitarCodigo(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null)
            throw new IllegalArgumentException("Usuário não encontrado");
        
        // Gerar código de 6 dígitos
        String codigo = String.format("%06d", new Random().nextInt(999999));
        
        usuario.setCodigoAutenticacao(codigo);
        usuario.setCodigoValidade(LocalDateTime.now().plusMinutes(15));
        
        // TODO: Enviar email com o código
        System.out.println("Código de autenticação: " + codigo + " para " + email);
    }
    
    @Transactional
    public Usuario autenticar(String email, String codigo) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        if (usuario == null)
            throw new IllegalArgumentException("Usuário não encontrado");
        
        if (usuario.getCodigoAutenticacao() == null)
            throw new IllegalArgumentException("Código não solicitado");
        
        if (usuario.getCodigoValidade().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Código expirado");
        
        if (!usuario.getCodigoAutenticacao().equals(codigo))
            throw new IllegalArgumentException("Código inválido");
        
        // Limpar código
        usuario.setCodigoAutenticacao(null);
        usuario.setCodigoValidade(null);
        usuario.setUltimoAcesso(LocalDateTime.now());
        
        return usuario;
    }
}
