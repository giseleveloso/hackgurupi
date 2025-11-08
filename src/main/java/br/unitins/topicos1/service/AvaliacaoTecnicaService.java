package br.unitins.topicos1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.AvaliacaoTecnicaDTO;
import br.unitins.topicos1.model.AvaliacaoTecnica;
import br.unitins.topicos1.model.GestorPrefeitura;
import br.unitins.topicos1.model.Projeto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AvaliacaoTecnicaService {
    
    @Inject
    br.unitins.topicos1.repository.AvaliacaoTecnicaRepository repository;
    
    @Inject
    ProjetoService projetoService;
    
    @Inject
    GestorPrefeituraService gestorService;
    
    public List<AvaliacaoTecnica> findByProjeto(Long projetoId) {
        return repository.findByProjeto(projetoId);
    }
    
    @Transactional
    public AvaliacaoTecnica avaliar(AvaliacaoTecnicaDTO dto) {
        Projeto projeto = projetoService.findById(dto.projetoId());
        GestorPrefeitura gestor = gestorService.findById(dto.gestorId());
        
        // Verificar se gestor já avaliou este projeto
        AvaliacaoTecnica avaliacaoExistente = repository.findByProjetoAndGestor(dto.projetoId(), dto.gestorId());
        if (avaliacaoExistente != null)
            throw new IllegalArgumentException("Gestor já avaliou este projeto");
        
        AvaliacaoTecnica avaliacao = new AvaliacaoTecnica();
        avaliacao.setProjeto(projeto);
        avaliacao.setGestor(gestor);
        avaliacao.setCriterioViabilidade(dto.criterioViabilidade());
        avaliacao.setCriterioImpacto(dto.criterioImpacto());
        avaliacao.setCriterioInovacao(dto.criterioInovacao());
        avaliacao.setCriterioOrcamento(dto.criterioOrcamento());
        avaliacao.setJustificativa(dto.justificativa());
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        
        // Calcular nota final da avaliação (média dos critérios)
        BigDecimal nota = dto.criterioViabilidade()
            .add(dto.criterioImpacto())
            .add(dto.criterioInovacao())
            .add(dto.criterioOrcamento())
            .divide(new BigDecimal("4"), 2, RoundingMode.HALF_UP);
        avaliacao.setNota(nota);
        
        repository.persist(avaliacao);
        
        // Atualizar nota técnica do projeto (média de todas as avaliações)
        calcularNotaTecnicaProjeto(dto.projetoId());
        
        return avaliacao;
    }
    
    @Transactional
    public void calcularNotaTecnicaProjeto(Long projetoId) {
        List<AvaliacaoTecnica> avaliacoes = repository.findByProjeto(projetoId);
        
        if (!avaliacoes.isEmpty()) {
            BigDecimal soma = BigDecimal.ZERO;
            for (AvaliacaoTecnica av : avaliacoes) {
                soma = soma.add(av.getNota());
            }
            
            BigDecimal media = soma.divide(new BigDecimal(avaliacoes.size()), 2, RoundingMode.HALF_UP);
            
            Projeto projeto = projetoService.findById(projetoId);
            projeto.setNotaTecnica(media);
            
            projetoService.calcularNotaFinal(projetoId);
        }
    }
}

