# 📘 Como Fazer Upload pelo Swagger

## 🎯 Guia Rápido

### ✅ Campos Corretos

| Campo | ❌ ERRADO | ✅ CORRETO |
|-------|-----------|------------|
| **file** | (não selecionar) | Selecionar arquivo |
| **fileName** | `TICKETS` | `TICKETS.pdf` |
| **mimeType** | `pdf` | `application/pdf` |

## 📋 Passo a Passo Detalhado

### 1. Abra o Swagger UI
```
http://localhost:8080/q/swagger-ui
```

### 2. Localize o Endpoint
```
POST /projetos/{projetoId}/anexos
```

### 3. Clique em "Try it out"

### 4. Preencha os Campos

#### **projetoId** (Path Parameter)
```
1
```
*Use um ID de projeto que existe no banco*

#### **Request body** (multipart/form-data)

##### ① **file** - Selecione o Arquivo
- Clique em **"Escolher Arquivo"** ou **"Choose File"**
- Navegue até o arquivo
- Selecione o arquivo (ex: `apresentacao.pdf`)

##### ② **fileName** - Nome COM EXTENSÃO
```
apresentacao.pdf
```

⚠️ **IMPORTANTE:** Deve incluir a extensão!

**Exemplos corretos:**
- `documento.pdf`
- `planilha.xlsx`
- `foto.jpg`
- `apresentação_projeto.pptx`

**Exemplos incorretos:**
- ❌ `documento` (sem extensão)
- ❌ `TICKETS` (sem extensão)
- ❌ `arquivo` (sem extensão)

##### ③ **mimeType** - Tipo MIME Completo
```
application/pdf
```

⚠️ **IMPORTANTE:** Deve ser o tipo MIME completo, não apenas a extensão!

**Tipos MIME comuns:**

| Tipo de Arquivo | mimeType Correto |
|----------------|------------------|
| PDF | `application/pdf` |
| Word (DOC) | `application/msword` |
| Word (DOCX) | `application/vnd.openxmlformats-officedocument.wordprocessingml.document` |
| Excel (XLS) | `application/vnd.ms-excel` |
| Excel (XLSX) | `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` |
| PowerPoint (PPT) | `application/vnd.ms-powerpoint` |
| PowerPoint (PPTX) | `application/vnd.openxmlformats-officedocument.presentationml.presentation` |
| JPG/JPEG | `image/jpeg` |
| PNG | `image/png` |
| GIF | `image/gif` |
| TXT | `text/plain` |
| CSV | `text/csv` |
| ZIP | `application/zip` |

**Exemplos incorretos:**
- ❌ `pdf` (falta o prefixo)
- ❌ `.pdf` (não é um tipo MIME)
- ❌ `document` (não é um tipo MIME válido)

### 5. Clique em "Execute"

### 6. Verifique a Resposta

#### **Sucesso (201 Created):**
```json
{
  "id": 1,
  "projetoId": 1,
  "nomeArquivo": "apresentacao.pdf",
  "urlArquivo": "projetos/1/abc123-def456.pdf",
  "tipoArquivo": "application/pdf",
  "tamanho": 524288,
  "dataUpload": "2024-11-08T15:30:00"
}
```

#### **Erro (400 Bad Request):**
```json
{
  "error": "Arquivo deve ter uma extensão válida (ex: .pdf, .jpg)"
}
```

## 🔧 Correções Automáticas

O sistema agora tenta corrigir automaticamente alguns erros comuns:

### Correção de mimeType Incompleto

Se você digitar apenas a extensão (ex: `pdf`), o sistema vai tentar inferir o tipo correto:

```
Input:  "pdf"
Output: "application/pdf" ✅
```

Mas é **sempre melhor** fornecer o tipo MIME completo!

## 🐛 Problemas Comuns

### 1. "Nome do arquivo não pode ser vazio"

**Causa:** Campo `fileName` não foi preenchido.

**Solução:** Preencha o campo `fileName` com o nome + extensão do arquivo.

### 2. "Arquivo deve ter uma extensão válida"

**Causa:** O `fileName` não tem extensão (ex: "documento" ao invés de "documento.pdf").

**Solução:** Adicione a extensão ao nome:
```
documento.pdf
```

### 3. "Tipo de arquivo não permitido: xyz"

**Causa:** A extensão do arquivo não está na lista de tipos permitidos.

**Extensões permitidas:**
- Documentos: `pdf`, `doc`, `docx`, `xls`, `xlsx`, `ppt`, `pptx`, `txt`, `csv`
- Imagens: `jpg`, `jpeg`, `png`, `gif`
- Compactados: `zip`, `rar`

**Solução:** Use um arquivo com extensão permitida.

### 4. "Arquivo excede o tamanho máximo permitido"

**Causa:** Arquivo maior que 10MB.

**Solução:** 
- Use um arquivo menor, OU
- Peça ao administrador para aumentar o limite em `application.properties`:
```properties
app.upload.max-file-size=52428800  # 50MB
quarkus.http.limits.max-body-size=50M
```

### 5. "Projeto não encontrado"

**Causa:** O `projetoId` não existe no banco de dados.

**Solução:** 
1. Liste os projetos disponíveis:
   ```
   GET /projetos
   ```
2. Use um `projetoId` válido.

## 📸 Exemplo Visual

```
┌─────────────────────────────────────────────┐
│ POST /projetos/{projetoId}/anexos          │
└─────────────────────────────────────────────┘

Parameters
──────────────────────────────────────────────
projetoId*  [1                              ]
  integer($int64) (path)

Request body  [multipart/form-data ▼]
──────────────────────────────────────────────
file       [Escolher Arquivo] apresentacao.pdf
  string($binary)

fileName   [apresentacao.pdf                ]
  string

mimeType   [application/pdf                 ]
  string

──────────────────────────────────────────────
           [ Execute ]
```

## 💡 Dicas Pro

### 1. Use o Nome Original do Arquivo
Quando selecionar o arquivo, copie o nome dele para o campo `fileName`:

Se selecionou `Aqualy - apresenta____o.pdf`, use:
```
fileName: Aqualy - apresenta____o.pdf
```

### 2. Referência Rápida de MIME Types

Mantenha esta lista à mão:

**Documentos:**
```
PDF:  application/pdf
DOCX: application/vnd.openxmlformats-officedocument.wordprocessingml.document
XLSX: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
```

**Imagens:**
```
JPG:  image/jpeg
PNG:  image/png
GIF:  image/gif
```

**Outros:**
```
TXT:  text/plain
ZIP:  application/zip
```

### 3. Teste com Arquivo Pequeno Primeiro

Antes de enviar arquivos grandes:
1. Crie um arquivo de teste pequeno
2. Teste o upload
3. Se funcionar, envie arquivos maiores

```bash
# Criar arquivo de teste
echo "Teste de upload" > teste.txt
```

Depois use:
```
fileName: teste.txt
mimeType: text/plain
```

## 🔗 Alternativas ao Swagger

Se o Swagger continuar dando problemas, use:

### 1. cURL (Terminal)
```bash
curl -X POST "http://localhost:8080/projetos/1/anexos" \
  -F "file=@apresentacao.pdf" \
  -F "fileName=apresentacao.pdf" \
  -F "mimeType=application/pdf"
```

### 2. Postman
1. Método: POST
2. URL: `http://localhost:8080/projetos/1/anexos`
3. Body → form-data
4. Adicione campos: `file`, `fileName`, `mimeType`

### 3. Interface Web
```
http://localhost:8080/upload-simples.html
```

Esta interface já preenche tudo automaticamente! 🎉

## 📞 Ajuda Adicional

Se ainda tiver problemas:

1. **Verifique os logs do servidor** para ver mensagens detalhadas
2. **Use a interface de teste** em `/upload-simples.html`
3. **Teste com cURL** para isolar se é problema do Swagger

---

## ✅ Checklist Final

Antes de clicar em "Execute":

- [ ] Selecionei o arquivo em **file**
- [ ] **fileName** tem extensão (ex: `.pdf`)
- [ ] **mimeType** é completo (ex: `application/pdf`, não `pdf`)
- [ ] **projetoId** existe no banco
- [ ] Arquivo é menor que 10MB
- [ ] Extensão está na lista de permitidos

Se todos os itens estão ✅, pode executar! 🚀

