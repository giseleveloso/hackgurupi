package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.AcademicoDTO;
import br.unitins.topicos1.model.Academico;
import br.unitins.topicos1.model.TipoUsuario;
import br.unitins.topicos1.repository.AcademicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AcademicoService {
    
    @Inject
    AcademicoRepository repository;
    
    public List<Academico> findAll() {
        return repository.listAll();
    }
    
    public Academico findById(Long id) {
        Academico academico = repository.findById(id);
        if (academico == null)
            throw new NotFoundException("Acadêmico não encontrado");
        return academico;
    }
    
    public Academico findByEmail(String email) {
        return repository.findByEmail(email);
    }
    
    public List<Academico> findAtivos() {
        return repository.findAtivos();
    }
    
    @Transactional
    public Academico create(AcademicoDTO dto) {
        // Validar se email já existe
        if (repository.findByEmail(dto.email()) != null)
            throw new IllegalArgumentException("Email já cadastrado");
        
        // Validar se CPF já existe
        if (repository.findByCpf(dto.cpf()) != null)
            throw new IllegalArgumentException("CPF já cadastrado");
        
        Academico academico = new Academico();
        academico.setNome(dto.nome());
        academico.setEmail(dto.email());
        academico.setCpf(dto.cpf());
        academico.setSenha(dto.senha());
        academico.setDataNascimento(dto.dataNascimento());
        academico.setInstituicao(dto.instituicao());
        academico.setCurso(dto.curso());
        academico.setLattes(dto.lattes());
        academico.setAreaAtuacao(dto.areaAtuacao());
        academico.setVinculoProfessor(dto.vinculoProfessor());
        academico.setTipoUsuario(TipoUsuario.ACADEMICO);
        academico.setPontuacao(0);
        academico.setDataCadastro(LocalDateTime.now());
        academico.setAtivo(true);
        
        repository.persist(academico);
        return academico;
    }
    
    @Transactional
    public Academico update(Long id, AcademicoDTO dto) {
        Academico academico = findById(id);
        
        academico.setNome(dto.nome());
        academico.setDataNascimento(dto.dataNascimento());
        academico.setInstituicao(dto.instituicao());
        academico.setCurso(dto.curso());
        academico.setLattes(dto.lattes());
        academico.setAreaAtuacao(dto.areaAtuacao());
        academico.setVinculoProfessor(dto.vinculoProfessor());
        
        return academico;
    }
    
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    @Transactional
    public void adicionarPontos(Long id, Integer pontos) {
        Academico academico = findById(id);
        academico.setPontuacao(academico.getPontuacao() + pontos);
    }
}

