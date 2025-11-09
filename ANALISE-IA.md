# 🤖 Sistema de Análise de Projetos com IA

Sistema completo para análise automática de projetos usando **Google Gemini** (100% GRÁTIS!), com fluxo de aprovação por gestores.

## 🎉 Análise GRÁTIS com Google Gemini!

Todas as análises são feitas com **Google Gemini**, uma IA de última geração totalmente **gratuita**!

| Característica | Valor |
|----------------|-------|
| **Preço** | 🎁 **R$ 0,00 (SEMPRE!)** |
| **Limite Grátis** | 15 req/min, 1M tokens/mês |
| **Para 100 projetos/mês** | **R$ 0,00** |
| **Para 3.000 projetos/mês** | **R$ 0,00** |
| **Qualidade** | ⭐⭐⭐⭐⭐ Excelente |

**→ [Veja como configurar (2 minutos)](GEMINI-SETUP.md)**

## 🎯 Fluxo Completo

```
1. Gestor solicita análise por IA
          ↓
2. IA analisa projeto + anexos
          ↓
3. Avaliação criada (status: PENDENTE)
          ↓
4. Gestor revisa a avaliação
          ↓
     ┌────┴────┐
     ↓         ↓
 APROVAR   REJEITAR
     ↓         ↓
5. Nota      Avaliação
   conta     descartada
```

## 📋 Endpoints da API

### 1. Solicitar Análise por IA
```
POST /avaliacoes/solicitar-analise-ia
Content-Type: application/json
```

**Body:**
```json
{
  "projetoId": 1,
  "gestorId": 8
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "projetoId": 1,
  "projetoTitulo": "Sistema de Mobilidade Urbana Inteligente",
  "gestorId": 8,
  "gestorNome": "Roberto Ferreira",
  "nota": 8.37,
  "criterioViabilidade": 8.5,
  "criterioImpacto": 9.0,
  "criterioInovacao": 7.5,
  "criterioOrcamento": 8.5,
  "justificativa": "O projeto apresenta uma proposta sólida e bem estruturada...",
  "dataAvaliacao": "2024-11-08T15:30:00",
  "geradaPorIA": true,
  "analiseIA": "# ANÁLISE GERADA POR IA\n\n## Justificativa\n...",
  "statusAvaliacao": "Pendente de Aprovação",
  "dataAprovacao": null,
  "motivoRejeicao": null
}
```

### 2. Listar Avaliações Pendentes
```
GET /avaliacoes/pendentes
```

**Resposta:**
```json
[
  {
    "id": 1,
    "projetoId": 1,
    "projetoTitulo": "Sistema de Mobilidade Urbana Inteligente",
    "geradaPorIA": true,
    "statusAvaliacao": "Pendente de Aprovação",
    "nota": 8.37,
    ...
  }
]
```

### 3. Contar Avaliações Pendentes
```
GET /avaliacoes/pendentes/count
```

**Resposta:**
```json
{
  "count": 3
}
```

### 4. Aprovar Avaliação
```
PUT /avaliacoes/{avaliacaoId}/aprovar
Content-Type: application/json
```

**Body:**
```json
{
  "gestorId": 8
}
```

**Resposta:**
```json
{
  "id": 1,
  "statusAvaliacao": "Aprovada",
  "dataAprovacao": "2024-11-08T16:00:00",
  ...
}
```

### 5. Rejeitar Avaliação
```
PUT /avaliacoes/{avaliacaoId}/rejeitar
Content-Type: application/json
```

**Body:**
```json
{
  "gestorId": 8,
  "motivo": "A análise não considerou adequadamente o contexto local da cidade."
}
```

**Resposta:**
```json
{
  "id": 1,
  "statusAvaliacao": "Rejeitada",
  "dataAprovacao": "2024-11-08T16:05:00",
  "motivoRejeicao": "A análise não considerou adequadamente o contexto local da cidade.",
  ...
}
```

### 6. Buscar Avaliação por ID
```
GET /avaliacoes/{avaliacaoId}
```

### 7. Listar Avaliações de um Projeto
```
GET /avaliacoes/projeto/{projetoId}
```

## 🔧 Configuração (2 minutos)

### 1. Obter Chave do Google Gemini

1. Acesse: **https://makersuite.google.com/app/apikey**
2. Faça login com sua conta Google
3. Clique em **"Create API key"**
4. Copie a chave gerada (começa com `AIzaSy...`)

**→ [Guia detalhado passo a passo](COMO-OBTER-CHAVE-GEMINI.md)**

### 2. Configurar no Projeto

Edite `application.properties`:

```properties
# Google Gemini (100% GRÁTIS!)
gemini.api.key=AIzaSyXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```

**Modelo padrão:**
- `gemini-2.0-flash-exp` - Mais rápido e recente (~2-3s), já configurado automaticamente
- Temperature: 0.2 para respostas consistentes
- Response MIME type forçado para JSON

**→ [Mais detalhes e comparações](GEMINI-SETUP.md)**

### 3. Testar

```bash
# Inicie a aplicação
./mvnw quarkus:dev

# Solicite uma análise
curl -X POST http://localhost:8080/avaliacoes/solicitar-analise-ia \
  -H "Content-Type: application/json" \
  -d '{"projetoId": 1, "gestorId": 8}'
```

## 📊 Critérios de Avaliação

A IA avalia cada projeto em **4 critérios** (0-10):

### 1. Viabilidade Técnica
- O projeto é tecnicamente viável?
- Os recursos necessários são realistas?
- A metodologia proposta é adequada?
- Há riscos técnicos significativos?

### 2. Impacto Social
- Quantas pessoas serão beneficiadas?
- Qual a relevância do problema abordado?
- O projeto promove inclusão e equidade?
- Há potencial de transformação social?

### 3. Inovação
- O projeto traz soluções inovadoras?
- Usa tecnologias modernas/adequadas?
- Há criatividade na abordagem?
- Diferencia-se de projetos existentes?

### 4. Orçamento
- O orçamento é realista e bem justificado?
- Há boa relação custo-benefício?
- Os custos são compatíveis com resultados?
- O prazo de execução é adequado?

**Nota Final** = Média dos 4 critérios

## 🔍 O que a IA Analisa

### Dados do Projeto
- ✅ Título
- ✅ Resumo popular
- ✅ Descrição completa
- ✅ Objetivos
- ✅ Metodologia
- ✅ Resultados esperados
- ✅ Orçamento estimado
- ✅ Prazo de execução
- ✅ Área temática

### Anexos
- ✅ Lista de arquivos anexados
- ✅ Tipos de documentos
- ⚠️ Conteúdo dos anexos (não implementado ainda)

## 💡 Exemplos de Uso

### Exemplo 1: Análise Simples

```javascript
// 1. Solicitar análise
const response = await fetch('/avaliacoes/solicitar-analise-ia', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    projetoId: 1,
    gestorId: 8
  })
});

const avaliacao = await response.json();
console.log('Nota da IA:', avaliacao.nota);
console.log('Status:', avaliacao.statusAvaliacao);
```

### Exemplo 2: Aprovar/Rejeitar

```javascript
// Listar pendentes
const pendentes = await fetch('/avaliacoes/pendentes').then(r => r.json());

// Aprovar primeira avaliação
await fetch(`/avaliacoes/${pendentes[0].id}/aprovar`, {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ gestorId: 8 })
});

// Rejeitar segunda avaliação
await fetch(`/avaliacoes/${pendentes[1].id}/rejeitar`, {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    gestorId: 8,
    motivo: 'Orçamento subestimado'
  })
});
```

### Exemplo 3: Dashboard de Aprovações

```javascript
// Conta pendentes
const { count } = await fetch('/avaliacoes/pendentes/count')
  .then(r => r.json());

if (count > 0) {
  console.log(`⚠️ Você tem ${count} avaliações pendentes!`);
  
  // Lista detalhes
  const avaliacoes = await fetch('/avaliacoes/pendentes')
    .then(r => r.json());
  
  avaliacoes.forEach(av => {
    console.log(`- Projeto: ${av.projetoTitulo}`);
    console.log(`  Nota: ${av.nota}`);
    console.log(`  Data: ${av.dataAvaliacao}`);
  });
}
```

## 🎨 Frontend - Exemplo de Interface

```html
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <title>Avaliações Pendentes</title>
</head>
<body>
    <h1>🤖 Avaliações por IA</h1>
    
    <div id="pendentes"></div>
    
    <script>
        const gestorId = 8; // ID do gestor logado
        
        // Carrega avaliações pendentes
        async function loadPendentes() {
            const response = await fetch('/avaliacoes/pendentes');
            const avaliacoes = await response.json();
            
            const container = document.getElementById('pendentes');
            container.innerHTML = avaliacoes.map(av => `
                <div class="card">
                    <h3>${av.projetoTitulo}</h3>
                    <p><strong>Nota:</strong> ${av.nota}/10</p>
                    
                    <h4>Critérios:</h4>
                    <ul>
                        <li>Viabilidade: ${av.criterioViabilidade}</li>
                        <li>Impacto: ${av.criterioImpacto}</li>
                        <li>Inovação: ${av.criterioInovacao}</li>
                        <li>Orçamento: ${av.criterioOrcamento}</li>
                    </ul>
                    
                    <details>
                        <summary>Ver Análise Completa</summary>
                        <pre>${av.analiseIA}</pre>
                    </details>
                    
                    <button onclick="aprovar(${av.id})">✅ Aprovar</button>
                    <button onclick="rejeitar(${av.id})">❌ Rejeitar</button>
                </div>
            `).join('');
        }
        
        // Aprova avaliação
        async function aprovar(avaliacaoId) {
            await fetch(`/avaliacoes/${avaliacaoId}/aprovar`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ gestorId })
            });
            
            alert('✅ Avaliação aprovada!');
            loadPendentes(); // Recarrega lista
        }
        
        // Rejeita avaliação
        async function rejeitar(avaliacaoId) {
            const motivo = prompt('Motivo da rejeição:');
            if (!motivo) return;
            
            await fetch(`/avaliacoes/${avaliacaoId}/rejeitar`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ gestorId, motivo })
            });
            
            alert('❌ Avaliação rejeitada!');
            loadPendentes(); // Recarrega lista
        }
        
        // Carrega ao abrir página
        loadPendentes();
    </script>
</body>
</html>
```

## 🔒 Regras de Negócio

### Validações

1. ✅ Apenas um gestor pode solicitar análise por vez
2. ✅ Não pode haver análise pendente duplicada para o mesmo projeto
3. ✅ Apenas o gestor responsável pode aprovar/rejeitar
4. ✅ Avaliação só pode ser aprovada/rejeitada uma vez
5. ✅ Apenas avaliações **aprovadas** contam para nota técnica do projeto

### Status de Avaliação

| Status | Descrição | Conta na Nota? |
|--------|-----------|----------------|
| **Pendente** | Aguardando aprovação do gestor | ❌ Não |
| **Aprovada** | Gestor aprovou a análise | ✅ Sim |
| **Rejeitada** | Gestor rejeitou a análise | ❌ Não |

### Cálculo de Notas

**Nota da Avaliação** = (Viabilidade + Impacto + Inovação + Orçamento) / 4

**Nota Técnica do Projeto** = Média de todas as avaliações **APROVADAS**

**Nota Final do Projeto** = (Nota Técnica × 0.6) + (Nota Popular × 0.4)

## 💰 Custos (Spoiler: R$ 0,00!)

### Google Gemini - 100% GRÁTIS

| Limite | Valor | Suficiente para |
|--------|-------|-----------------|
| Requisições/minuto | 15 | Mais de 20.000 análises/dia |
| Requisições/dia | 1.500 | Todas as análises necessárias |
| Tokens/mês | 1.000.000 | ~3.000 análises/mês |

### Estimativa por Análise

**Projeto médio** (~2000 palavras):
- Tokens de entrada: ~3.000
- Tokens de saída: ~800
- **Total:** ~3.800 tokens

**Custo por análise:** **R$ 0,00** 🎉

### Para Diferentes Volumes

| Volume | Tokens/mês | Custo |
|--------|------------|-------|
| 10 projetos | 38.000 | **R$ 0,00** |
| 100 projetos | 380.000 | **R$ 0,00** |
| 250 projetos | 950.000 | **R$ 0,00** |
| 3.000 projetos | 11.400.000 | **R$ 0,00*** |

*Ainda grátis, mas vai usar ~11x o limite mensal (precisaria distribuir ao longo do ano)

### 🎉 Sem Preocupações

- ✅ Sem cartão de crédito
- ✅ Sem cobrança surpresa
- ✅ Sem limite de projetos por dia (até 1.500)
- ✅ Sempre grátis (não é trial)

## 🐛 Tratamento de Erros

### Erros Comuns

#### 1. "Chave da API Gemini não configurada"
**Causa:** `gemini.api.key` vazio em `application.properties`

**Solução:**
```properties
gemini.api.key=AIzaSyXXXXXXXXXXXXXXX
```

#### 2. "Erro na API Gemini: 400 - API key not valid"
**Causa:** Chave da API inválida

**Solução:** 
- Verifique se a chave começa com `AIzaSy`
- Verifique se não há espaços antes/depois
- Regenere a chave em https://makersuite.google.com/app/apikey

#### 3. "Erro na API Gemini: 429 - Quota exceeded"
**Causa:** Limite de requisições excedido (15/min ou 1.500/dia)

**Solução:** 
- Aguarde 1 minuto (limite por minuto)
- Ou aguarde até amanhã (limite diário)
- O limite é muito alto, isso raramente acontece

#### 4. "Já existe uma avaliação de IA pendente"
**Causa:** Tentou solicitar análise duplicada

**Solução:** Aprove ou rejeite a avaliação pendente primeiro

#### 5. "Apenas o gestor responsável pode aprovar"
**Causa:** Gestor diferente tentou aprovar

**Solução:** Use o mesmo `gestorId` que solicitou a análise

## 🚀 Melhorias Futuras

### 1. Análise de Anexos
```java
// Extrair texto de PDFs e incluir na análise
String anexoContent = extractTextFromPDF(anexo);
projectContext.append("**Conteúdo do Anexo:**\n").append(anexoContent);
```

### 2. Análise Comparativa
```java
// Comparar projeto com outros similares
List<Projeto> projetosSimilares = findSimilarProjects(projeto);
String comparison = geminiService.compareProjects(projeto, projetosSimilares);
```

### 3. Sugestões de Melhoria
```java
// IA sugere melhorias específicas
String suggestions = geminiService.generateImprovementSuggestions(projeto);
```

### 4. Análise de Sentimento em Comentários
```java
// Analisa comentários dos cidadãos
List<Comentario> comentarios = comentarioRepository.findByProjeto(projetoId);
SentimentAnalysis sentiment = geminiService.analyzeSentiment(comentarios);
```

### 5. Detecção de Plágio
```java
// Verifica se projeto é similar a outros
PlagiarismCheck check = geminiService.checkPlagiarism(projeto);
```

### 6. Análise Histórica e Tendências
```java
// Analisa evolução do projeto ao longo do tempo
List<AvaliacaoTecnica> historico = avaliacaoRepository.findByProjeto(projetoId);
String trendAnalysis = geminiService.analyzeTrends(projeto, historico);
```

## 📞 Suporte

Para problemas ou dúvidas:

1. **Erro de configuração?** Verifique `application.properties`
2. **Erro da API?** Consulte: https://ai.google.dev/docs
3. **Como obter chave?** [COMO-OBTER-CHAVE-GEMINI.md](COMO-OBTER-CHAVE-GEMINI.md)
4. **Guia completo?** [GEMINI-SETUP.md](GEMINI-SETUP.md)

---

**Desenvolvido para HackGurupi** 🚀

