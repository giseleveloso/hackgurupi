package br.unitins.topicos1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.ProjetoDTO;
import br.unitins.topicos1.model.Academico;
import br.unitins.topicos1.model.AreaTematica;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.model.StatusProjeto;
import br.unitins.topicos1.repository.ProjetoRepository;
import br.unitins.topicos1.repository.VotoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ProjetoService {
    
    @Inject
    ProjetoRepository repository;
    
    @Inject
    AcademicoService academicoService;
    
    @Inject
    VotoRepository votoRepository;
    
    @Transactional
    public List<Projeto> findAll() {
        List<Projeto> projetos = repository.listAll();
        projetos.forEach(this::sincronizarVotos);
        return projetos;
    }
    
    @Transactional
    public Projeto findById(Long id) {
        Projeto projeto = repository.findById(id);
        if (projeto == null)
            throw new NotFoundException("Projeto não encontrado");
        sincronizarVotos(projeto);
        return projeto;
    }
    
    public void sincronizarVotos(Projeto projeto) {
        Long totalVotosReal = votoRepository.countByProjeto(projeto.getId());
        projeto.setTotalVotos(totalVotosReal.intValue());
    }
    
    @Transactional
    public void sincronizarTodosProjetos() {
        List<Projeto> projetos = repository.listAll();
        projetos.forEach(this::sincronizarVotos);
    }
    
    @Transactional
    public List<Projeto> findPublicos() {
        List<Projeto> projetos = repository.findPublicos();
        projetos.forEach(this::sincronizarVotos);
        return projetos;
    }
    
    @Transactional
    public List<Projeto> findByAcademico(Long academicoId) {
        List<Projeto> projetos = repository.findByAcademico(academicoId);
        projetos.forEach(this::sincronizarVotos);
        return projetos;
    }
    
    @Transactional
    public List<Projeto> findByStatus(StatusProjeto status) {
        List<Projeto> projetos = repository.findByStatus(status);
        projetos.forEach(this::sincronizarVotos);
        return projetos;
    }
    
    @Transactional
    public List<Projeto> findTopVotados(int limit) {
        List<Projeto> projetos = repository.findTopVotados(limit);
        projetos.forEach(this::sincronizarVotos);
        return projetos;
    }
    
    @Transactional
    public Projeto create(ProjetoDTO dto) {
        Academico academico = academicoService.findById(dto.academicoId());
        
        // Validar resumo popular (máximo 280 caracteres)
        if (dto.resumoPopular().length() > 280)
            throw new IllegalArgumentException("Resumo popular deve ter no máximo 280 caracteres");
        
        Projeto projeto = new Projeto();
        projeto.setAcademico(academico);
        projeto.setTitulo(dto.titulo());
        projeto.setResumoPopular(dto.resumoPopular());
        projeto.setDescricaoCompleta(dto.descricaoCompleta());
        projeto.setObjetivos(dto.objetivos());
        projeto.setMetodologia(dto.metodologia());
        projeto.setResultadosEsperados(dto.resultadosEsperados());
        projeto.setOrcamentoEstimado(dto.orcamentoEstimado());
        projeto.setPrazoExecucao(dto.prazoExecucao());
        projeto.setAreaTematica(AreaTematica.valueOf(dto.areaTematica()));
        projeto.setStatus(StatusProjeto.RASCUNHO);
        projeto.setTotalVotos(0);
        projeto.setTotalVisualizacoes(0);
        
        repository.persist(projeto);
        return projeto;
    }
    
    @Transactional
    public Projeto update(Long id, ProjetoDTO dto) {
        Projeto projeto = findById(id);
        
        // Só permite editar se estiver em rascunho
        if (!projeto.getStatus().equals(StatusProjeto.RASCUNHO))
            throw new IllegalArgumentException("Projeto não pode ser editado após submissão");
        
        if (dto.resumoPopular().length() > 280)
            throw new IllegalArgumentException("Resumo popular deve ter no máximo 280 caracteres");
        
        projeto.setTitulo(dto.titulo());
        projeto.setResumoPopular(dto.resumoPopular());
        projeto.setDescricaoCompleta(dto.descricaoCompleta());
        projeto.setObjetivos(dto.objetivos());
        projeto.setMetodologia(dto.metodologia());
        projeto.setResultadosEsperados(dto.resultadosEsperados());
        projeto.setOrcamentoEstimado(dto.orcamentoEstimado());
        projeto.setPrazoExecucao(dto.prazoExecucao());
        projeto.setAreaTematica(AreaTematica.valueOf(dto.areaTematica()));
        
        return projeto;
    }
    
    @Transactional
    public void submeter(Long id) {
        Projeto projeto = findById(id);
        
        if (!projeto.getStatus().equals(StatusProjeto.RASCUNHO))
            throw new IllegalArgumentException("Projeto já foi submetido");
        
        projeto.setStatus(StatusProjeto.AGUARDANDO_APROVACAO);
        projeto.setDataSubmissao(LocalDateTime.now());
    }
    
    @Transactional
    public void aprovar(Long id) {
        Projeto projeto = findById(id);
        
        if (!projeto.getStatus().equals(StatusProjeto.AGUARDANDO_APROVACAO))
            throw new IllegalArgumentException("Projeto não está aguardando aprovação");
        
        projeto.setStatus(StatusProjeto.APROVADO);
        projeto.setDataAprovacao(LocalDateTime.now());
    }
    
    @Transactional
    public void rejeitar(Long id, String motivo) {
        Projeto projeto = findById(id);
        
        if (!projeto.getStatus().equals(StatusProjeto.AGUARDANDO_APROVACAO))
            throw new IllegalArgumentException("Projeto não está aguardando aprovação");
        
        projeto.setStatus(StatusProjeto.REJEITADO);
        // TODO: Enviar notificação ao acadêmico com o motivo
    }
    
    @Transactional
    public void incrementarVisualizacoes(Long id) {
        Projeto projeto = findById(id);
        projeto.setTotalVisualizacoes(projeto.getTotalVisualizacoes() + 1);
    }
    
    @Transactional
    public void calcularNotaFinal(Long id) {
        Projeto projeto = findById(id);
        
        if (projeto.getNotaTecnica() != null && projeto.getNotaPopular() != null) {
            // 50% técnica + 50% popular
            BigDecimal notaFinal = projeto.getNotaTecnica()
                .multiply(new BigDecimal("0.5"))
                .add(projeto.getNotaPopular().multiply(new BigDecimal("0.5")))
                .setScale(2, RoundingMode.HALF_UP);
            
            projeto.setNotaFinal(notaFinal);
        }
    }
    
    @Transactional
    public void atualizarTotalVotos(Long id, Integer total) {
        Projeto projeto = findById(id);
        projeto.setTotalVotos(total);
        
        // Calcular nota popular baseada nos votos (simples: escala de 0-10)
        // Pode ser melhorado com algoritmo mais sofisticado
        BigDecimal notaPopular = new BigDecimal(Math.min(total * 0.1, 10.0))
            .setScale(2, RoundingMode.HALF_UP);
        projeto.setNotaPopular(notaPopular);
        
        calcularNotaFinal(id);
    }
    
    @Transactional
    public void delete(Long id) {
        Projeto projeto = findById(id);
        
        // Só permite deletar se estiver em rascunho
        if (!projeto.getStatus().equals(StatusProjeto.RASCUNHO))
            throw new IllegalArgumentException("Projeto não pode ser deletado após submissão");
        
        repository.delete(projeto);
    }
}

