package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.ComentarioDTO;
import br.unitins.topicos1.model.Cidadao;
import br.unitins.topicos1.model.Comentario;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.repository.ComentarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ComentarioService {
    
    @Inject
    ComentarioRepository repository;
    
    @Inject
    ProjetoService projetoService;
    
    @Inject
    CidadaoService cidadaoService;
    
    public List<Comentario> findByProjeto(Long projetoId) {
        return repository.findByProjeto(projetoId);
    }
    
    public Comentario findById(Long id) {
        Comentario comentario = repository.findById(id);
        if (comentario == null)
            throw new NotFoundException("Comentário não encontrado");
        return comentario;
    }
    
    @Transactional
    public Comentario criar(ComentarioDTO dto) {
        Projeto projeto = projetoService.findById(dto.projetoId());
        Cidadao cidadao = cidadaoService.findById(dto.cidadaoId());
        
        Comentario comentario = new Comentario();
        comentario.setProjeto(projeto);
        comentario.setCidadao(cidadao);
        comentario.setTexto(dto.texto());
        comentario.setDataComentario(LocalDateTime.now());
        comentario.setEditado(false);
        
        repository.persist(comentario);
        
        // Adicionar pontos ao cidadão
        cidadaoService.adicionarPontos(dto.cidadaoId(), 2);
        
        return comentario;
    }
    
    @Transactional
    public Comentario editar(Long id, String novoTexto) {
        Comentario comentario = findById(id);
        
        comentario.setTexto(novoTexto);
        comentario.setEditado(true);
        
        return comentario;
    }
    
    @Transactional
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}

