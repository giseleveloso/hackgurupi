# 🧪 Testando Upload de Anexos

## ⚠️ Problema Comum: "Tipo de arquivo não permitido: "

Esse erro ocorre quando:
1. O nome do arquivo está **vazio ou nulo**
2. O arquivo **não tem extensão**
3. Os campos do formulário não estão sendo enviados corretamente

## ✅ Checklist de Validação

Antes de fazer o upload, verifique:

- [ ] O arquivo tem **nome** (não vazio)
- [ ] O arquivo tem **extensão** válida (.pdf, .jpg, etc)
- [ ] O tipo MIME está correto
- [ ] O tamanho é menor que 10MB
- [ ] O projeto existe no banco de dados

## 📝 Testes com cURL

### 1. Teste Básico - Upload de PDF
```bash
# Crie um arquivo de teste
echo "Teste" > teste.pdf

# Faça o upload
curl -X POST "http://localhost:8080/projetos/1/anexos" \
  -F "file=@teste.pdf" \
  -F "fileName=teste.pdf" \
  -F "mimeType=application/pdf" \
  -v
```

### 2. Upload de Imagem JPG
```bash
curl -X POST "http://localhost:8080/projetos/1/anexos" \
  -F "file=@imagem.jpg" \
  -F "fileName=imagem.jpg" \
  -F "mimeType=image/jpeg" \
  -v
```

### 3. Upload de Documento Word
```bash
curl -X POST "http://localhost:8080/projetos/1/anexos" \
  -F "file=@documento.docx" \
  -F "fileName=documento.docx" \
  -F "mimeType=application/vnd.openxmlformats-officedocument.wordprocessingml.document" \
  -v
```

## 🔍 Debug - Verificando Logs

No terminal onde o Quarkus está rodando, você verá:

```
📤 Iniciando upload:
  - Projeto ID: 1
  - Nome arquivo: teste.pdf
  - Content-Type: application/pdf
  - Tamanho: 5 bytes
```

Se aparecer:
```
  - Nome arquivo: null
```
ou
```
  - Nome arquivo: 
```

Significa que o campo `fileName` não está sendo enviado corretamente.

## 🐛 Erros Comuns e Soluções

### 1. "Nome do arquivo não pode ser vazio"
**Causa:** O campo `fileName` não foi enviado ou está vazio.

**Solução:**
```javascript
// ❌ ERRADO
formData.append('file', file);

// ✅ CORRETO
formData.append('file', file);
formData.append('fileName', file.name);
formData.append('mimeType', file.type);
```

### 2. "Arquivo deve ter uma extensão válida"
**Causa:** O nome do arquivo não tem extensão (ex: "documento" ao invés de "documento.pdf")

**Solução:**
```javascript
// Verificar antes de enviar
if (!file.name.includes('.')) {
    alert('Arquivo deve ter extensão (ex: .pdf, .jpg)');
    return;
}
```

### 3. "Tipo de arquivo não permitido: xyz"
**Causa:** Extensão não está na lista de permitidos.

**Tipos permitidos:**
- `pdf, doc, docx, xls, xlsx, ppt, pptx`
- `jpg, jpeg, png, gif`
- `txt, csv, zip, rar`

**Solução:** Use um dos tipos permitidos ou adicione a extensão em `FileService.java`:
```java
private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
    "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
    "jpg", "jpeg", "png", "gif",
    "txt", "csv", "zip", "rar",
    "mp4", "mp3" // ← Adicione aqui se necessário
);
```

### 4. "Arquivo excede o tamanho máximo permitido"
**Causa:** Arquivo maior que 10MB.

**Solução:** Altere em `application.properties`:
```properties
app.upload.max-file-size=52428800  # 50MB
quarkus.http.limits.max-body-size=50M
```

## 📱 Teste com Postman

1. **Método:** POST
2. **URL:** `http://localhost:8080/projetos/1/anexos`
3. **Body:** Selecione `form-data`
4. **Adicione campos:**
   - `file` (tipo: File) → Selecione o arquivo
   - `fileName` (tipo: Text) → Digite: `teste.pdf`
   - `mimeType` (tipo: Text) → Digite: `application/pdf`

## 🌐 Teste no Navegador

Abra: `http://localhost:8080/upload-example.html`

1. Clique na área de upload
2. Selecione um arquivo
3. Abra o **Console do navegador** (F12)
4. Você verá:
```
📤 Enviando arquivo:
  - Nome: teste.pdf
  - Tipo: application/pdf
  - Tamanho: 12345
```

## 🔧 Verificando o Backend

### 1. Verifique se o projeto existe
```bash
curl http://localhost:8080/projetos/1
```

Deve retornar os dados do projeto. Se retornar 404, o projeto não existe.

### 2. Liste anexos existentes
```bash
curl http://localhost:8080/projetos/1/anexos
```

### 3. Faça download de um anexo
```bash
curl -O http://localhost:8080/projetos/1/anexos/1/download
```

## 📊 Resposta de Sucesso

Upload bem-sucedido retorna **201 Created**:
```json
{
  "id": 1,
  "projetoId": 1,
  "nomeArquivo": "teste.pdf",
  "urlArquivo": "projetos/1/abc123-def456.pdf",
  "tipoArquivo": "application/pdf",
  "tamanho": 12345,
  "dataUpload": "2024-11-08T15:30:00"
}
```

## 🚨 Respostas de Erro

### 400 Bad Request
```json
{
  "error": "Nome do arquivo não pode ser vazio"
}
```

```json
{
  "error": "Arquivo deve ter uma extensão válida (ex: .pdf, .jpg)"
}
```

```json
{
  "error": "Tipo de arquivo não permitido: .exe. Tipos permitidos: pdf, doc, docx, xls, xlsx, ppt, pptx, jpg, jpeg, png, gif, txt, csv, zip, rar"
}
```

### 404 Not Found
```json
{
  "error": "Projeto não encontrado"
}
```

## 💡 Exemplo Completo com JavaScript/Fetch

```javascript
async function uploadFile(projetoId, file) {
    // 1. Validações no frontend
    if (!file.name.includes('.')) {
        alert('Arquivo deve ter extensão');
        return;
    }
    
    const maxSize = 10 * 1024 * 1024; // 10MB
    if (file.size > maxSize) {
        alert('Arquivo muito grande (máx: 10MB)');
        return;
    }
    
    // 2. Prepara FormData
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileName', file.name);
    formData.append('mimeType', file.type || 'application/octet-stream');
    
    // 3. Log para debug
    console.log('Upload:', {
        nome: file.name,
        tipo: file.type,
        tamanho: file.size
    });
    
    // 4. Envia
    try {
        const response = await fetch(`/projetos/${projetoId}/anexos`, {
            method: 'POST',
            body: formData
        });
        
        if (response.ok) {
            const anexo = await response.json();
            console.log('✅ Upload completo:', anexo);
            alert('Arquivo enviado com sucesso!');
        } else {
            const error = await response.json();
            console.error('❌ Erro:', error);
            alert('Erro: ' + error.error);
        }
    } catch (error) {
        console.error('❌ Erro de rede:', error);
        alert('Erro ao enviar arquivo');
    }
}

// Uso:
const inputFile = document.querySelector('input[type="file"]');
inputFile.addEventListener('change', (e) => {
    if (e.target.files.length > 0) {
        uploadFile(1, e.target.files[0]); // projetoId = 1
    }
});
```

## 📂 Verificando Arquivos no Servidor

Os arquivos são salvos em:
```
uploads/
└── projetos/
    └── 1/
        ├── abc123-def456.pdf
        └── xyz789-ghi012.jpg
```

Para verificar:
```bash
# Windows
dir uploads\projetos\1

# Linux/Mac
ls -lh uploads/projetos/1
```

## 🎯 Resumo - Campos Obrigatórios

Para fazer upload, você **DEVE** enviar:

| Campo | Tipo | Obrigatório | Exemplo |
|-------|------|-------------|---------|
| `file` | File | ✅ SIM | (arquivo binário) |
| `fileName` | String | ✅ SIM | `"documento.pdf"` |
| `mimeType` | String | ⚠️ Recomendado | `"application/pdf"` |

**Atenção:** Se você esquecer de enviar `fileName`, o upload vai falhar com o erro que você está vendo!

