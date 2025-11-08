package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.CidadaoDTO;
import br.unitins.topicos1.model.Cidadao;
import br.unitins.topicos1.model.TipoUsuario;
import br.unitins.topicos1.repository.CidadaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CidadaoService {
    
    @Inject
    CidadaoRepository repository;
    
    public List<Cidadao> findAll() {
        return repository.listAll();
    }
    
    public Cidadao findById(Long id) {
        Cidadao cidadao = repository.findById(id);
        if (cidadao == null)
            throw new NotFoundException("Cidadão não encontrado");
        return cidadao;
    }
    
    public Cidadao findByEmail(String email) {
        return repository.findByEmail(email);
    }
    
    public List<Cidadao> findAtivos() {
        return repository.findAtivos();
    }
    
    @Transactional
    public Cidadao create(CidadaoDTO dto) {
        // Validar se email já existe
        if (repository.findByEmail(dto.email()) != null)
            throw new IllegalArgumentException("Email já cadastrado");
        
        // Validar se CPF já existe
        if (repository.findByCpf(dto.cpf()) != null)
            throw new IllegalArgumentException("CPF já cadastrado");
        
        Cidadao cidadao = new Cidadao();
        cidadao.setNome(dto.nome());
        cidadao.setEmail(dto.email());
        cidadao.setCpf(dto.cpf());
        cidadao.setSenha(dto.senha());
        cidadao.setDataNascimento(dto.dataNascimento());
        cidadao.setBairro(dto.bairro());
        cidadao.setInteressesAreas(dto.interessesAreas());
        cidadao.setTipoUsuario(TipoUsuario.CIDADAO);
        cidadao.setPontuacao(0);
        cidadao.setDataCadastro(LocalDateTime.now());
        cidadao.setAtivo(true);
        
        repository.persist(cidadao);
        return cidadao;
    }
    
    @Transactional
    public Cidadao update(Long id, CidadaoDTO dto) {
        Cidadao cidadao = findById(id);
        
        cidadao.setNome(dto.nome());
        cidadao.setDataNascimento(dto.dataNascimento());
        cidadao.setBairro(dto.bairro());
        cidadao.setInteressesAreas(dto.interessesAreas());
        
        return cidadao;
    }
    
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    @Transactional
    public void adicionarPontos(Long id, Integer pontos) {
        Cidadao cidadao = findById(id);
        cidadao.setPontuacao(cidadao.getPontuacao() + pontos);
    }
}

