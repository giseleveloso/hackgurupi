package br.unitins.topicos1.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unitins.topicos1.client.GeminiClient;
import br.unitins.topicos1.model.AnexoProjeto;
import br.unitins.topicos1.model.Projeto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Serviço para análise de projetos usando Google Gemini API (GRATUITO!)
 * 
 * Tier Gratuito:
 * - 15 requisições por minuto
 * - 1 milhão de tokens por mês
 * - Totalmente grátis!
 * 
 * Obtenha sua chave em: https://makersuite.google.com/app/apikey
 */
@ApplicationScoped
public class GeminiAnalysisService {
    
    @ConfigProperty(name = "gemini.api.key", defaultValue = "")
    String geminiApiKey;
    
    @Inject
    AnexoProjetoService anexoService;
    
    @Inject
    GeminiClient geminiClient;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Analisa um projeto usando Gemini e retorna notas para cada critério
     */
    public AIAnalysisResult analyzeProject(Projeto projeto) throws IOException, InterruptedException {
        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            throw new IllegalStateException("Chave da API Gemini não configurada. Configure 'gemini.api.key' no application.properties");
        }
        
        System.out.println("🤖 Iniciando análise com Google Gemini para projeto: " + projeto.getTitulo());
        
        // Monta o contexto do projeto
        String projectContext = buildProjectContext(projeto);
        
        // Monta o system prompt e user input
        String systemPrompt = buildSystemPrompt();
        String userInput = projectContext;
        
        try {
            // Chama a API do Gemini
            String geminiResponse = geminiClient.generateJson(geminiApiKey, systemPrompt, userInput);
            
            // Parse da resposta
            return parseGeminiResponse(geminiResponse);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao solicitar análise de IA: " + e.getMessage(), e);
        }
    }
    
    /**
     * Constrói o contexto do projeto com todas as informações disponíveis
     */
    private String buildProjectContext(Projeto projeto) {
        StringBuilder context = new StringBuilder();
        
        context.append("# INFORMAÇÕES DO PROJETO\n\n");
        context.append("**Título:** ").append(projeto.getTitulo()).append("\n\n");
        context.append("**Resumo Popular:** ").append(projeto.getResumoPopular()).append("\n\n");
        
        if (projeto.getDescricaoCompleta() != null) {
            context.append("**Descrição Completa:**\n").append(projeto.getDescricaoCompleta()).append("\n\n");
        }
        
        if (projeto.getObjetivos() != null) {
            context.append("**Objetivos:**\n").append(projeto.getObjetivos()).append("\n\n");
        }
        
        if (projeto.getMetodologia() != null) {
            context.append("**Metodologia:**\n").append(projeto.getMetodologia()).append("\n\n");
        }
        
        if (projeto.getResultadosEsperados() != null) {
            context.append("**Resultados Esperados:**\n").append(projeto.getResultadosEsperados()).append("\n\n");
        }
        
        if (projeto.getOrcamentoEstimado() != null) {
            context.append("**Orçamento Estimado:** R$ ").append(projeto.getOrcamentoEstimado()).append("\n\n");
        }
        
        if (projeto.getPrazoExecucao() != null) {
            context.append("**Prazo de Execução:** ").append(projeto.getPrazoExecucao()).append(" meses\n\n");
        }
        
        context.append("**Área Temática:** ").append(projeto.getAreaTematica().getLabel()).append("\n\n");
        
        // Lista anexos (se houver)
        List<AnexoProjeto> anexos = anexoService.findByProjeto(projeto.getId());
        if (!anexos.isEmpty()) {
            context.append("**Anexos do Projeto:**\n");
            for (AnexoProjeto anexo : anexos) {
                context.append("- ").append(anexo.getNomeArquivo())
                    .append(" (").append(anexo.getTipoArquivo()).append(")\n");
            }
            context.append("\n");
        }
        
        return context.toString();
    }
    
    /**
     * Constrói o system prompt para análise da IA
     */
    private String buildSystemPrompt() {
        return """
            Você é um especialista em avaliação de projetos de inovação urbana e deve analisar projetos segundo 4 critérios específicos.
            
            # CRITÉRIOS DE AVALIAÇÃO
            
            Avalie o projeto de 0 a 10 em cada um dos seguintes critérios:
            
            1. **VIABILIDADE TÉCNICA** (0-10):
               - O projeto é tecnicamente viável?
               - Os recursos necessários são realistas?
               - A metodologia proposta é adequada?
               - Há riscos técnicos significativos?
            
            2. **IMPACTO SOCIAL** (0-10):
               - Quantas pessoas serão beneficiadas?
               - Qual a relevância do problema abordado?
               - O projeto promove inclusão e equidade?
               - Há potencial de transformação social?
            
            3. **INOVAÇÃO** (0-10):
               - O projeto traz soluções inovadoras?
               - Usa tecnologias modernas/adequadas?
               - Há criatividade na abordagem?
               - Diferencia-se de projetos existentes?
            
            4. **ORÇAMENTO** (0-10):
               - O orçamento é realista e bem justificado?
               - Há boa relação custo-benefício?
               - Os custos são compatíveis com resultados?
               - O prazo de execução é adequado?
            
            # FORMATO DA RESPOSTA
            
            Responda APENAS em formato JSON válido, seguindo exatamente esta estrutura:
            
            {
              "criterioViabilidade": "8.5",
              "criterioImpacto": "9.0",
              "criterioInovacao": "7.5",
              "criterioOrcamento": "8.0",
              "justificativa": "Análise detalhada explicando as notas atribuídas...",
              "pontosFortesDetalhados": "Descrição dos pontos fortes identificados...",
              "pontosFracosDetalhados": "Descrição dos pontos fracos e riscos...",
              "recomendacoes": "Sugestões de melhorias para o projeto..."
            }
            
            REGRAS IMPORTANTES:
            - Responda SOMENTE em JSON seguindo o schema fornecido
            - Use números decimais como STRING para as notas (ex: "8.5", não 8.5)
            - Seja objetivo e técnico na justificativa
            - Considere o contexto de uma cidade como Gurupi-TO
            - Forneça uma análise completa mas concisa
            - Retorne APENAS o JSON, sem markdown ou texto adicional
            """;
    }
    
    
    /**
     * Faz parse da resposta da IA (similar ao código de referência)
     */
    private AIAnalysisResult parseGeminiResponse(String geminiResponse) throws IOException {
        System.out.println("🔍 Fazendo parse da resposta do Gemini...");
        
        try {
            JsonNode root = objectMapper.readTree(geminiResponse);
            
            // Extrai o texto da estrutura de resposta do Gemini
            // Formato: candidates[0].content.parts[0].text
            if (root.has("candidates")) {
                JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
                if (!textNode.isMissingNode()) {
                    // O texto contém o JSON real
                    String actualJson = textNode.asText();
                    root = objectMapper.readTree(actualJson);
                }
            }
            
            // Remove possíveis markdown code blocks
            String jsonText = root.toString();
            if (root.isTextual()) {
                jsonText = root.asText();
            }
            
            jsonText = jsonText.trim();
            if (jsonText.startsWith("```json")) {
                jsonText = jsonText.substring(7);
            }
            if (jsonText.startsWith("```")) {
                jsonText = jsonText.substring(3);
            }
            if (jsonText.endsWith("```")) {
                jsonText = jsonText.substring(0, jsonText.length() - 3);
            }
            jsonText = jsonText.trim();
            
            JsonNode result = objectMapper.readTree(jsonText);
            
            AIAnalysisResult analysis = new AIAnalysisResult();
            analysis.setCriterioViabilidade(new BigDecimal(result.get("criterioViabilidade").asText()));
            analysis.setCriterioImpacto(new BigDecimal(result.get("criterioImpacto").asText()));
            analysis.setCriterioInovacao(new BigDecimal(result.get("criterioInovacao").asText()));
            analysis.setCriterioOrcamento(new BigDecimal(result.get("criterioOrcamento").asText()));
            analysis.setJustificativa(result.get("justificativa").asText());
            
            // Monta análise completa
            StringBuilder analiseCompleta = new StringBuilder();
            analiseCompleta.append("# ANÁLISE GERADA POR IA (Google Gemini)\n\n");
            analiseCompleta.append("## Justificativa\n").append(result.get("justificativa").asText()).append("\n\n");
            
            if (result.has("pontosFortesDetalhados")) {
                analiseCompleta.append("## Pontos Fortes\n").append(result.get("pontosFortesDetalhados").asText()).append("\n\n");
            }
            
            if (result.has("pontosFracosDetalhados")) {
                analiseCompleta.append("## Pontos Fracos e Riscos\n").append(result.get("pontosFracosDetalhados").asText()).append("\n\n");
            }
            
            if (result.has("recomendacoes")) {
                analiseCompleta.append("## Recomendações\n").append(result.get("recomendacoes").asText()).append("\n");
            }
            
            analysis.setAnaliseCompleta(analiseCompleta.toString());
            
            System.out.println("✅ Parse concluído com sucesso");
            
            return analysis;
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao fazer parse da resposta: " + e.getMessage());
            throw new IOException("Falha ao interpretar resposta da IA: " + e.getMessage(), e);
        }
    }
    
    /**
     * Classe para armazenar o resultado da análise da IA
     */
    public static class AIAnalysisResult {
        private BigDecimal criterioViabilidade;
        private BigDecimal criterioImpacto;
        private BigDecimal criterioInovacao;
        private BigDecimal criterioOrcamento;
        private String justificativa;
        private String analiseCompleta;
        
        // Getters e Setters
        public BigDecimal getCriterioViabilidade() {
            return criterioViabilidade;
        }
        
        public void setCriterioViabilidade(BigDecimal criterioViabilidade) {
            this.criterioViabilidade = criterioViabilidade;
        }
        
        public BigDecimal getCriterioImpacto() {
            return criterioImpacto;
        }
        
        public void setCriterioImpacto(BigDecimal criterioImpacto) {
            this.criterioImpacto = criterioImpacto;
        }
        
        public BigDecimal getCriterioInovacao() {
            return criterioInovacao;
        }
        
        public void setCriterioInovacao(BigDecimal criterioInovacao) {
            this.criterioInovacao = criterioInovacao;
        }
        
        public BigDecimal getCriterioOrcamento() {
            return criterioOrcamento;
        }
        
        public void setCriterioOrcamento(BigDecimal criterioOrcamento) {
            this.criterioOrcamento = criterioOrcamento;
        }
        
        public String getJustificativa() {
            return justificativa;
        }
        
        public void setJustificativa(String justificativa) {
            this.justificativa = justificativa;
        }
        
        public String getAnaliseCompleta() {
            return analiseCompleta;
        }
        
        public void setAnaliseCompleta(String analiseCompleta) {
            this.analiseCompleta = analiseCompleta;
        }
    }
}

