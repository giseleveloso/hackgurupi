package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.RepositorioDadosDTO;
import br.unitins.topicos1.model.RepositorioDados;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RepositorioDadosService {
    
    @Inject
    br.unitins.topicos1.repository.RepositorioDadosRepository repository;
    
    public List<RepositorioDados> findAll() {
        return repository.listAll();
    }
    
    public List<RepositorioDados> findAtivos() {
        return repository.findAtivos();
    }
    
    public RepositorioDados findById(Long id) {
        RepositorioDados repo = repository.findById(id);
        if (repo == null)
            throw new NotFoundException("Repositório não encontrado");
        return repo;
    }
    
    @Transactional
    public RepositorioDados create(RepositorioDadosDTO dto) {
        RepositorioDados repo = new RepositorioDados();
        repo.setNome(dto.nome());
        repo.setDescricao(dto.descricao());
        repo.setUrl(dto.url());
        repo.setFonte(dto.fonte());
        repo.setCategoriaDados(dto.categoriaDados());
        repo.setUltimaAtualizacao(dto.ultimaAtualizacao());
        repo.setAtivo(true);
        
        repository.persist(repo);
        return repo;
    }
    
    @Transactional
    public RepositorioDados update(Long id, RepositorioDadosDTO dto) {
        RepositorioDados repo = findById(id);
        
        repo.setNome(dto.nome());
        repo.setDescricao(dto.descricao());
        repo.setUrl(dto.url());
        repo.setFonte(dto.fonte());
        repo.setCategoriaDados(dto.categoriaDados());
        repo.setUltimaAtualizacao(dto.ultimaAtualizacao());
        
        return repo;
    }
    
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

