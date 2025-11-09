package br.unitins.topicos1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.AvaliacaoTecnicaDTO;
import br.unitins.topicos1.model.AvaliacaoTecnica;
import br.unitins.topicos1.model.GestorPrefeitura;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.model.StatusAvaliacao;
import br.unitins.topicos1.service.GeminiAnalysisService.AIAnalysisResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AvaliacaoTecnicaService {

    @Inject
    br.unitins.topicos1.repository.AvaliacaoTecnicaRepository repository;

    @Inject
    ProjetoService projetoService;

    @Inject
    GestorPrefeituraService gestorService;

    @Inject
    GeminiAnalysisService geminiService;

    public AvaliacaoTecnica findById(Long id) {
        AvaliacaoTecnica avaliacao = repository.findById(id);
        if (avaliacao == null) {
            throw new NotFoundException("Avaliação não encontrada");
        }
        return avaliacao;
    }

    public List<AvaliacaoTecnica> findByProjeto(Long projetoId) {
        return repository.findByProjeto(projetoId);
    }

    public List<AvaliacaoTecnica> findPendentes() {
        return repository.findPendentes();
    }

    public List<AvaliacaoTecnica> findByStatus(StatusAvaliacao status) {
        return repository.findByStatus(status.getId());
    }

    public Long countPendentes() {
        return repository.countPendentes();
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
        
        // Mudar status do projeto para AGUARDANDO_APROVACAO após avaliação
        projeto.setStatus(br.unitins.topicos1.model.StatusProjeto.AGUARDANDO_APROVACAO);
        

        return avaliacao;
    }

    /*
     * @Transactional
     * public void calcularNotaTecnicaProjeto(Long projetoId) {
     * List<AvaliacaoTecnica> avaliacoes = repository.findByProjeto(projetoId);
     * 
     * if (!avaliacoes.isEmpty()) {
     * BigDecimal soma = BigDecimal.ZERO;
     * for (AvaliacaoTecnica av : avaliacoes) {
     * soma = soma.add(av.getNota());
     * }
     * 
     * BigDecimal media = soma.divide(new BigDecimal(avaliacoes.size()), 2,
     * RoundingMode.HALF_UP);
     * 
     * Projeto projeto = projetoService.findById(projetoId);
     * projeto.setNotaTecnica(media);
     * 
     * projetoService.calcularNotaFinal(projetoId);
     * }
     * }
     * 
     */
    /**
     * Solicita análise de um projeto por IA
     */
    @Transactional
    public AvaliacaoTecnica solicitarAnaliseIA(Long projetoId, Long gestorId) {
        System.out.println("🤖 Solicitando análise por IA para projeto " + projetoId);

        Projeto projeto = projetoService.findById(projetoId);
        GestorPrefeitura gestor = gestorService.findById(gestorId);

        // Verificar se já existe avaliação de IA pendente para este projeto
        List<AvaliacaoTecnica> avaliacoesPendentes = repository.findByProjeto(projetoId).stream()
                .filter(a -> a.getGeradaPorIA() && a.getStatusAvaliacao() == StatusAvaliacao.PENDENTE_APROVACAO)
                .toList();

        if (!avaliacoesPendentes.isEmpty()) {
            throw new IllegalArgumentException("Já existe uma avaliação de IA pendente para este projeto");
        }

        try {
            // Chama serviço de IA do Google Gemini
            AIAnalysisResult aiResult = geminiService.analyzeProject(projeto);
            System.out.println("✅ Análise concluída com Google Gemini");

            // Cria avaliação com dados da IA
            AvaliacaoTecnica avaliacao = new AvaliacaoTecnica();
            avaliacao.setProjeto(projeto);
            avaliacao.setGestor(gestor);
            avaliacao.setCriterioViabilidade(aiResult.getCriterioViabilidade());
            avaliacao.setCriterioImpacto(aiResult.getCriterioImpacto());
            avaliacao.setCriterioInovacao(aiResult.getCriterioInovacao());
            avaliacao.setCriterioOrcamento(aiResult.getCriterioOrcamento());
            avaliacao.setJustificativa(aiResult.getJustificativa());
            avaliacao.setAnaliseIA(aiResult.getAnaliseCompleta());
            avaliacao.setDataAvaliacao(LocalDateTime.now());
            avaliacao.setGeradaPorIA(true);
            avaliacao.setStatusAvaliacao(StatusAvaliacao.PENDENTE_APROVACAO);

            // Calcula nota (média dos critérios)
            BigDecimal nota = aiResult.getCriterioViabilidade()
                    .add(aiResult.getCriterioImpacto())
                    .add(aiResult.getCriterioInovacao())
                    .add(aiResult.getCriterioOrcamento())
                    .divide(new BigDecimal("4"), 2, RoundingMode.HALF_UP);
            avaliacao.setNota(nota);

            repository.persist(avaliacao);

            System.out.println("✅ Avaliação de IA criada e aguardando aprovação");

            return avaliacao;

        } catch (Exception e) {
            System.err.println("❌ Erro ao solicitar análise de IA: " + e.getMessage());
            throw new RuntimeException("Erro ao solicitar análise de IA: " + e.getMessage(), e);
        }
    }

    /**
     * Aprova uma avaliação gerada por IA
     */
    @Transactional
    public AvaliacaoTecnica aprovarAvaliacaoIA(Long avaliacaoId, Long gestorId) {
        AvaliacaoTecnica avaliacao = findById(avaliacaoId);

        if (!avaliacao.getGeradaPorIA()) {
            throw new IllegalArgumentException("Esta avaliação não foi gerada por IA");
        }

        if (avaliacao.getStatusAvaliacao() != StatusAvaliacao.PENDENTE_APROVACAO) {
            throw new IllegalArgumentException("Esta avaliação já foi processada");
        }

        // Verifica se o gestor pode aprovar
        if (!avaliacao.getGestor().getId().equals(gestorId)) {
            throw new IllegalArgumentException("Apenas o gestor responsável pode aprovar esta avaliação");
        }

        // Aprova
        avaliacao.setStatusAvaliacao(StatusAvaliacao.APROVADA);
        avaliacao.setDataAprovacao(LocalDateTime.now());

        // Atualiza nota técnica do projeto
        calcularNotaTecnicaProjeto(avaliacao.getProjeto().getId());

        System.out.println("✅ Avaliação de IA aprovada pelo gestor");

        return avaliacao;
    }

    /**
     * Rejeita uma avaliação gerada por IA
     */
    @Transactional
    public AvaliacaoTecnica rejeitarAvaliacaoIA(Long avaliacaoId, Long gestorId, String motivo) {
        AvaliacaoTecnica avaliacao = findById(avaliacaoId);

        if (!avaliacao.getGeradaPorIA()) {
            throw new IllegalArgumentException("Esta avaliação não foi gerada por IA");
        }

        if (avaliacao.getStatusAvaliacao() != StatusAvaliacao.PENDENTE_APROVACAO) {
            throw new IllegalArgumentException("Esta avaliação já foi processada");
        }

        // Verifica se o gestor pode rejeitar
        if (!avaliacao.getGestor().getId().equals(gestorId)) {
            throw new IllegalArgumentException("Apenas o gestor responsável pode rejeitar esta avaliação");
        }

        // Rejeita
        avaliacao.setStatusAvaliacao(StatusAvaliacao.REJEITADA);
        avaliacao.setDataAprovacao(LocalDateTime.now());
        avaliacao.setMotivoRejeicao(motivo);

        System.out.println("❌ Avaliação de IA rejeitada pelo gestor");

        return avaliacao;
    }

    /**
     * Calcula nota técnica considerando apenas avaliações aprovadas
     */
    @Transactional
    public void calcularNotaTecnicaProjeto(Long projetoId) {
        List<AvaliacaoTecnica> avaliacoes = repository.findByProjeto(projetoId).stream()
                .filter(a -> a.getStatusAvaliacao() == StatusAvaliacao.APROVADA)
                .toList();

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
