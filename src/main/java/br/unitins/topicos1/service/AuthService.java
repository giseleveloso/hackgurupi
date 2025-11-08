package br.unitins.topicos1.service;

import java.time.LocalDateTime;

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
    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        if (usuario == null)
            throw new IllegalArgumentException("Usuário não encontrado");
        
        if (!usuario.getSenha().equals(senha))
            throw new IllegalArgumentException("Senha incorreta");
        
        if (!usuario.getAtivo())
            throw new IllegalArgumentException("Usuário inativo");
        
        usuario.setUltimoAcesso(LocalDateTime.now());
        
        return usuario;
    }
}
