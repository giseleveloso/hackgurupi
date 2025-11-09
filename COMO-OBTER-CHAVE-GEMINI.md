# 🔑 Como Obter sua Chave do Google Gemini (2 minutos)

## 📱 Passo a Passo com Imagens

### 1️⃣ Acesse o Site
Abra no navegador:
```
https://makersuite.google.com/app/apikey
```

ou pesquise no Google: **"Google AI Studio API Key"**

### 2️⃣ Faça Login
- Use sua conta Google pessoal ou do trabalho
- Não precisa de cartão de crédito! 🎉

### 3️⃣ Crie uma Chave API

Você verá uma tela assim:

```
┌─────────────────────────────────────┐
│  Google AI Studio                   │
│                                     │
│  ⚡ Get API Key                     │
│                                     │
│  [+ Create API key in new project] │
│                                     │
│  ou                                 │
│                                     │
│  [+ Create API key]                 │
│  (em projeto existente)             │
└─────────────────────────────────────┘
```

Clique em **"Create API key in new project"** (mais fácil)

### 4️⃣ Copie a Chave

A chave será gerada instantaneamente:

```
┌─────────────────────────────────────┐
│  ✅ API key created                 │
│                                     │
│  AIzaSyABCDEFGHIJKLMNOPQRSTUVWXYZ  │
│                                     │
│  [📋 Copy]                          │
└─────────────────────────────────────┘
```

Clique em **"Copy"** ou selecione e copie manualmente.

### 5️⃣ Cole no Projeto

Abra `application.properties` e cole:

```properties
gemini.api.key=AIzaSyABCDEFGHIJKLMNOPQRSTUVWXYZ
```

**Importante:** 
- Cole a chave COMPLETA! Ela começa com `AIzaSy...`
- O modelo `gemini-2.0-flash-exp` já está configurado automaticamente
- Retry automático para rate limiting (429)

### 6️⃣ Teste!

```bash
# Inicie o projeto
./mvnw quarkus:dev

# Teste a análise
curl -X POST http://localhost:8080/avaliacoes/solicitar-analise-ia \
  -H "Content-Type: application/json" \
  -d '{"projetoId": 1, "gestorId": 8}'
```

## ✅ Confirmação

Se deu certo, você verá nos logs:

```
🤖 Iniciando análise com Google Gemini para projeto: ...
📡 Enviando requisição para Google Gemini...
✅ Resposta recebida do Google Gemini
✅ Usando Google Gemini (GRÁTIS)
```

## 🎉 Pronto!

Agora você tem análises **ILIMITADAS** e **GRÁTIS** com Google Gemini!

- ✅ Sem custo
- ✅ Sem cartão
- ✅ Sem surpresas
- ✅ 1 milhão tokens/mês (suficiente para ~3.000 análises!)

## 🔒 Segurança da Chave

### ⚠️ NUNCA compartilhe sua chave!

**NUNCA faça isso:**
- ❌ Commitar no Git
- ❌ Postar em fóruns
- ❌ Enviar por email
- ❌ Compartilhar em grupos

**SEMPRE:**
- ✅ Mantenha em `application.properties` (local)
- ✅ Use variáveis de ambiente em produção
- ✅ Adicione ao `.gitignore` se necessário

### 🔐 Em Produção

Use variável de ambiente:

```bash
# No servidor
export GEMINI_API_KEY="AIzaSyABCDEFGHIJKLMNOPQRSTUVWXYZ"
```

E em `application.properties`:

```properties
gemini.api.key=${GEMINI_API_KEY}
```

## 🐛 Problemas?

### "API key not valid"
**Solução:** 
1. Verifique se copiou a chave completa (começa com `AIzaSy`)
2. Certifique-se de não ter espaços antes/depois
3. Regenere a chave se necessário

### "API not enabled"
**Solução:**
1. Acesse: https://console.cloud.google.com/apis/library/generativelanguage.googleapis.com
2. Clique em "Enable"
3. Aguarde 1-2 minutos
4. Tente novamente

### "Quota exceeded"
**Solução:**
- Aguarde 1 minuto (limite: 15 req/min)
- Ou aguarde até amanhã (limite: 1.500 req/dia)

## 📊 Ver seu Uso

1. Acesse: https://makersuite.google.com/app/apikey
2. Clique na sua chave
3. Veja estatísticas de uso

```
┌─────────────────────────────────────┐
│  Uso do mês                         │
│                                     │
│  📊 Requisições: 42 / 1.500         │
│  📊 Tokens: 15.234 / 1.000.000      │
│                                     │
│  💰 Custo: R$ 0,00 🎉               │
└─────────────────────────────────────┘
```

## 🎁 Dica Extra

Você pode criar múltiplas chaves para diferentes ambientes:

```properties
# Desenvolvimento
%dev.gemini.api.key=AIzaSy...CHAVE_DEV...

# Produção
%prod.gemini.api.key=AIzaSy...CHAVE_PROD...
```

Assim você tem controle separado e pode revogar uma sem afetar a outra!

## 📞 Links Rápidos

- **Criar chave:** https://makersuite.google.com/app/apikey
- **Documentação:** https://ai.google.dev/docs
- **Suporte:** https://ai.google.dev/support
- **Console:** https://console.cloud.google.com/

---

**Tempo total:** ~2 minutos ⏱️

**Custo:** R$ 0,00 para sempre! 🎉

