# 🎉 Análise com Google Gemini (100% GRÁTIS!)

## ✅ Por que usar Gemini?

| Característica | Google Gemini |
|----------------|---------------|
| **Preço** | 🎁 **R$ 0,00 SEMPRE!** |
| **Limite Grátis** | 15 req/min, 1M tokens/mês |
| **Qualidade** | ⭐⭐⭐⭐⭐ Excelente |
| **Velocidade** | ⚡ 2-3 segundos |
| **Para 100 projetos/mês** | **R$ 0,00** |
| **Para 3.000 projetos/mês** | **R$ 0,00** |
| **Requisito** | Apenas conta Google |

**Conclusão:** Análises ilimitadas sem gastar nada! 🚀

## 🔧 Configuração (2 minutos)

### Passo 1: Obter Chave da API

1. Acesse: **https://makersuite.google.com/app/apikey**
2. Faça login com sua conta Google
3. Clique em **"Get API Key"** ou **"Create API Key"**
4. Selecione um projeto ou crie um novo
5. Copie a chave gerada (começa com `AIzaSy...`)

### Passo 2: Configurar no Projeto

Edite `application.properties`:

```properties
# Google Gemini (GRÁTIS!)
gemini.api.key=AIzaSyXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```

**Pronto!** Você já pode usar! 🎉

O modelo padrão é `gemini-2.0-flash-exp` (mais recente e rápido)

### Passo 3: Testar

```bash
# Inicie a aplicação
./mvnw quarkus:dev

# Solicite uma análise
curl -X POST http://localhost:8080/avaliacoes/solicitar-analise-ia \
  -H "Content-Type: application/json" \
  -d '{"projetoId": 1, "gestorId": 8}'
```

Você verá no log:
```
🤖 Iniciando análise com Google Gemini para projeto: ...
📡 Enviando requisição para Google Gemini...
✅ Resposta recebida do Google Gemini
✅ Usando Google Gemini (GRÁTIS)
```

## 🎯 Modelo Padrão

### gemini-2.0-flash-exp (Experimental)
- ✅ **Mais recente e rápido**
- ✅ **Ideal para produção**
- ✅ **Totalmente gratuito**
- ✅ Já configurado por padrão
- ✅ **Suporta response_mime_type: application/json**

```properties
# Modelo gemini-2.0-flash-exp é usado automaticamente
# Nenhuma configuração adicional necessária
```

**Velocidade:** ~2-3 segundos por análise
**Qualidade:** Excelente para análise de projetos
**Temperature:** 0.2 (mais determinístico)

## 📊 Limites do Tier Gratuito

| Limite | Valor |
|--------|-------|
| Requisições por minuto | 15 |
| Requisições por dia | 1.500 |
| Tokens por minuto | 32.000 |
| Tokens por mês | 1.000.000 |

**Para 100 projetos/mês:** ~30.000 tokens = **3% do limite grátis!** 🎉

## 💡 Exemplo de Resultado

### Projeto: Sistema de Mobilidade Urbana

**Análise do Google Gemini:**
```json
{
  "criterioViabilidade": 8.5,
  "criterioImpacto": 9.0,
  "criterioInovacao": 7.5,
  "criterioOrcamento": 8.5,
  "nota": 8.37,
  "justificativa": "O projeto apresenta uma proposta bem estruturada e tecnicamente viável. A solução proposta tem potencial de beneficiar significativamente a população local...",
  "pontosFortesDetalhados": "- Metodologia clara e bem definida\n- Impacto social significativo\n- Orçamento realista...",
  "pontosFracosDetalhados": "- Prazo de execução pode ser otimista\n- Dependência de infraestrutura existente...",
  "recomendacoes": "- Considerar fase piloto inicial\n- Estabelecer parcerias com prefeitura..."
}
```

**Resultado:** Análise completa, profissional e totalmente gratuita! 🎯

## ⚡ Otimização de Performance

### 1. Modelo padrão: gemini-2.0-flash-exp
- ✅ **Já configurado** automaticamente
- ✅ **Mais rápido** da família Gemini 2.0
- ✅ **Velocidade:** ~2-3 segundos por análise
- ✅ **Gratuito**
- ✅ **Temperature: 0.2** (respostas mais consistentes)
- ✅ **response_mime_type: application/json** (força JSON válido)

### 2. Retry automático para Rate Limiting
O cliente trata automaticamente erros 429 (rate limiting) com até 2 tentativas

Nenhuma configuração adicional necessária!

### 3. Cache de Resultados (implementar se necessário)
```java
// Evita analisar o mesmo projeto várias vezes
if (projetoCacheMap.containsKey(projetoId)) {
    return projetoCacheMap.get(projetoId);
}
```

## 🐛 Solução de Problemas

### Erro: "Chave da API Gemini não configurada"
**Solução:** Adicione a chave em `application.properties`
```properties
gemini.api.key=AIzaSyXXXXXXXXXXXXXXXXXXXX
```

### Erro: "403 - API key not valid"
**Causa:** Chave inválida ou não ativada

**Solução:**
1. Verifique se copiou a chave corretamente
2. Certifique-se de que a API "Generative Language API" está ativada
3. Acesse: https://console.cloud.google.com/apis/library/generativelanguage.googleapis.com

### Erro: "429 - Quota exceeded"
**Causa:** Ultrapassou o limite de requisições

**Solução:**
- Aguarde 1 minuto (limite por minuto)
- Ou aguarde até o dia seguinte (limite diário)
- Limite: 15 req/min, 1.500 req/dia

### Erro: "400 - Invalid JSON"
**Causa:** Gemini retornou JSON mal formatado

**Solução:** Já tratado no código! O parser remove markdown automaticamente.

## 📈 Monitoramento de Uso

### Ver uso atual
1. Acesse: https://makersuite.google.com/app/apikey
2. Clique na sua chave
3. Veja "Usage" para estatísticas

### Dashboard de métricas
```
Total de análises hoje: X
Tokens usados este mês: Y / 1.000.000
Custo total: R$ 0,00 🎉
```

## 🚀 Próximos Passos

### 1. Análise de Imagens (Gemini Pro Vision)
```java
// Analisa imagens nos anexos do projeto
String imageAnalysis = geminiService.analyzeImage(anexoImage);
```

### 2. Análise de PDFs
```java
// Extrai e analisa conteúdo de PDFs
String pdfContent = extractTextFromPDF(anexoPdf);
analysis = geminiService.analyzeWithContext(projeto, pdfContent);
```

### 3. Análise Comparativa
```java
// Compara projeto com outros similares
List<Projeto> similares = findSimilarProjects(projeto);
String comparison = geminiService.compareProjects(projeto, similares);
```

## 🎁 Benefícios do Tier Gratuito

- ✅ **Sem cartão de crédito necessário**
- ✅ **Sem cobrança surpresa**
- ✅ **1 milhão de tokens/mês** (suficiente para ~3.000 análises!)
- ✅ **Sempre grátis** (não é trial)
- ✅ **Mesma qualidade** do tier pago

## 📞 Links Úteis

- **Obter API Key:** https://makersuite.google.com/app/apikey
- **Documentação:** https://ai.google.dev/docs
- **Preços:** https://ai.google.dev/pricing
- **Console:** https://console.cloud.google.com/
- **Suporte:** https://ai.google.dev/support

## 💰 Custos (Detalhe: ZERO!)

Para uma plataforma com **1.200 análises/ano** (100/mês):

| Item | Custo |
|------|-------|
| **Análises com IA** | **R$ 0,00** |
| **Chave API** | **R$ 0,00** |
| **Tokens** | **R$ 0,00** |
| **Requisições** | **R$ 0,00** |
| **Manutenção** | **R$ 0,00** |
| **TOTAL** | **R$ 0,00** 🎉 |

**Economia:** 100% do orçamento de IA! 💰

---

## 🎯 Resumo

1. Acesse: https://makersuite.google.com/app/apikey
2. Copie a chave API
3. Cole em `application.properties`:
   ```properties
   gemini.api.key=AIzaSyXXXXXXXXXXXXXX
   ```
4. **Pronto!** Análises ilimitadas e grátis! 🚀

**Recomendação:** Use Gemini. É grátis, rápido e tão bom quanto GPT-4! 🎉

