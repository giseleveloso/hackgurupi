package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.DesafioDTO;
import br.unitins.topicos1.model.AreaTematica;
import br.unitins.topicos1.model.Desafio;
import br.unitins.topicos1.model.StatusDesafio;
import br.unitins.topicos1.repository.DesafioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DesafioService {
    
    @Inject
    DesafioRepository repository;
    
    public List<Desafio> findAll() {
        return repository.listAll();
    }
    
    public List<Desafio> findAtivos() {
        return repository.findAtivos();
    }
    
    public Desafio findById(Long id) {
        Desafio desafio = repository.findById(id);
        if (desafio == null)
            throw new NotFoundException("Desafio não encontrado");
        return desafio;
    }
    
    @Transactional
    public Desafio create(DesafioDTO dto) {
        Desafio desafio = new Desafio();
        desafio.setTitulo(dto.titulo());
        desafio.setDescricao(dto.descricao());
        desafio.setAreaTematica(AreaTematica.valueOf(dto.areaTematica()));
        desafio.setStatus(StatusDesafio.PLANEJAMENTO);
        desafio.setOrcamentoDisponivel(dto.orcamentoDisponivel());
        desafio.setDataInicio(dto.dataInicio());
        desafio.setDataFim(dto.dataFim());
        desafio.setPrioridade(dto.prioridade());
        
        repository.persist(desafio);
        return desafio;
    }
    
    @Transactional
    public Desafio update(Long id, DesafioDTO dto) {
        Desafio desafio = findById(id);
        
        desafio.setTitulo(dto.titulo());
        desafio.setDescricao(dto.descricao());
        desafio.setAreaTematica(AreaTematica.valueOf(dto.areaTematica()));
        desafio.setOrcamentoDisponivel(dto.orcamentoDisponivel());
        desafio.setDataInicio(dto.dataInicio());
        desafio.setDataFim(dto.dataFim());
        desafio.setPrioridade(dto.prioridade());
        
        return desafio;
    }
    
    @Transactional
    public void publicar(Long id) {
        Desafio desafio = findById(id);
        desafio.setStatus(StatusDesafio.ATIVO);
    }
    
    @Transactional
    public void encerrar(Long id) {
        Desafio desafio = findById(id);
        desafio.setStatus(StatusDesafio.ENCERRADO);
    }
    
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

