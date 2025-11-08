package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.GestorPrefeituraDTO;
import br.unitins.topicos1.model.GestorPrefeitura;
import br.unitins.topicos1.model.TipoUsuario;
import br.unitins.topicos1.repository.GestorPrefeituraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class GestorPrefeituraService {
    
    @Inject
    GestorPrefeituraRepository repository;
    
    public List<GestorPrefeitura> findAll() {
        return repository.listAll();
    }
    
    public GestorPrefeitura findById(Long id) {
        GestorPrefeitura gestor = repository.findById(id);
        if (gestor == null)
            throw new NotFoundException("Gestor não encontrado");
        return gestor;
    }
    
    public GestorPrefeitura findByEmail(String email) {
        return repository.findByEmail(email);
    }
    
    public List<GestorPrefeitura> findAtivos() {
        return repository.findAtivos();
    }
    
    @Transactional
    public GestorPrefeitura create(GestorPrefeituraDTO dto) {
        // Validar se email já existe
        if (repository.findByEmail(dto.email()) != null)
            throw new IllegalArgumentException("Email já cadastrado");
        
        // Validar se CPF já existe
        if (repository.findByCpf(dto.cpf()) != null)
            throw new IllegalArgumentException("CPF já cadastrado");
        
        GestorPrefeitura gestor = new GestorPrefeitura();
        gestor.setNome(dto.nome());
        gestor.setEmail(dto.email());
        gestor.setCpf(dto.cpf());
        gestor.setSenha(dto.senha());
        gestor.setDataNascimento(dto.dataNascimento());
        gestor.setSecretaria(dto.secretaria());
        gestor.setCargo(dto.cargo());
        gestor.setNivelAcesso(dto.nivelAcesso());
        gestor.setTipoUsuario(TipoUsuario.GESTOR_PREFEITURA);
        gestor.setPontuacao(0);
        gestor.setDataCadastro(LocalDateTime.now());
        gestor.setAtivo(true);
        
        repository.persist(gestor);
        return gestor;
    }
    
    @Transactional
    public GestorPrefeitura update(Long id, GestorPrefeituraDTO dto) {
        GestorPrefeitura gestor = findById(id);
        
        gestor.setNome(dto.nome());
        gestor.setDataNascimento(dto.dataNascimento());
        gestor.setSecretaria(dto.secretaria());
        gestor.setCargo(dto.cargo());
        gestor.setNivelAcesso(dto.nivelAcesso());
        
        return gestor;
    }
    
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

