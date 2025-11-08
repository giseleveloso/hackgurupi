# 📎 Sistema de Upload de Anexos

Sistema completo para upload, listagem, download e exclusão de arquivos anexados a projetos.

## 📋 Funcionalidades

- ✅ Upload de arquivos (multipart/form-data)
- ✅ Validação de tipo e tamanho
- ✅ Listagem de anexos por projeto
- ✅ Download de arquivos
- ✅ Exclusão de anexos
- ✅ Drag & Drop (frontend)
- ✅ Armazenamento organizado por projeto

## 🚀 Endpoints da API

### 1. Upload de Anexo
```
POST /projetos/{projetoId}/anexos
Content-Type: multipart/form-data
```

**Parâmetros:**
- `file`: Arquivo (binário)
- `fileName`: Nome do arquivo
- `mimeType`: Tipo MIME

**Exemplo com cURL:**
```bash
curl -X POST "http://localhost:8080/projetos/1/anexos" \
  -F "file=@documento.pdf" \
  -F "fileName=documento.pdf" \
  -F "mimeType=application/pdf"
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "projetoId": 1,
  "nomeArquivo": "documento.pdf",
  "urlArquivo": "projetos/1/abc123-456def.pdf",
  "tipoArquivo": "application/pdf",
  "tamanho": 524288,
  "dataUpload": "2024-11-08T12:30:00"
}
```

### 2. Listar Anexos do Projeto
```
GET /projetos/{projetoId}/anexos
```

**Exemplo:**
```bash
curl http://localhost:8080/projetos/1/anexos
```

**Resposta:**
```json
[
  {
    "id": 1,
    "projetoId": 1,
    "nomeArquivo": "documento.pdf",
    "urlArquivo": "projetos/1/abc123.pdf",
    "tipoArquivo": "application/pdf",
    "tamanho": 524288,
    "dataUpload": "2024-11-08T12:30:00"
  }
]
```

### 3. Download de Anexo
```
GET /projetos/{projetoId}/anexos/{anexoId}/download
```

**Exemplo:**
```bash
curl -O "http://localhost:8080/projetos/1/anexos/1/download"
```

Retorna o arquivo com headers apropriados:
- `Content-Disposition: attachment; filename="documento.pdf"`
- `Content-Type: application/pdf`

### 4. Excluir Anexo
```
DELETE /projetos/{projetoId}/anexos/{anexoId}
```

**Exemplo:**
```bash
curl -X DELETE http://localhost:8080/projetos/1/anexos/1
```

**Resposta:** `204 No Content`

## 📁 Tipos de Arquivo Permitidos

### Documentos
- **PDF**: `.pdf`
- **Word**: `.doc`, `.docx`
- **Excel**: `.xls`, `.xlsx`
- **PowerPoint**: `.ppt`, `.pptx`
- **Texto**: `.txt`, `.csv`

### Imagens
- **JPEG**: `.jpg`, `.jpeg`
- **PNG**: `.png`
- **GIF**: `.gif`

### Compactados
- **ZIP**: `.zip`
- **RAR**: `.rar`

## ⚙️ Configurações

### application.properties
```properties
# Diretório de upload
app.upload.directory=uploads

# Tamanho máximo: 10MB (em bytes)
app.upload.max-file-size=10485760

# Configurações do Quarkus
quarkus.http.body.handle-file-uploads=true
quarkus.http.limits.max-body-size=10M
```

### Alterar Configurações

**Aumentar limite para 50MB:**
```properties
app.upload.max-file-size=52428800
quarkus.http.limits.max-body-size=50M
```

**Mudar diretório:**
```properties
app.upload.directory=/var/uploads
```

## 🎨 Frontend - Exemplo HTML

Acesse: `http://localhost:8080/upload-example.html`

### Exemplo com JavaScript/Fetch

```javascript
const fileInput = document.querySelector('input[type="file"]');
const file = fileInput.files[0];

const formData = new FormData();
formData.append('file', file);
formData.append('fileName', file.name);
formData.append('mimeType', file.type);

const response = await fetch('/projetos/1/anexos', {
  method: 'POST',
  body: formData
});

if (response.ok) {
  const anexo = await response.json();
  console.log('Upload completo:', anexo);
}
```

### Exemplo com Axios

```javascript
const formData = new FormData();
formData.append('file', file);
formData.append('fileName', file.name);
formData.append('mimeType', file.type);

axios.post('/projetos/1/anexos', formData, {
  headers: {
    'Content-Type': 'multipart/form-data'
  },
  onUploadProgress: (progressEvent) => {
    const percentCompleted = Math.round(
      (progressEvent.loaded * 100) / progressEvent.total
    );
    console.log(`Upload: ${percentCompleted}%`);
  }
}).then(response => {
  console.log('Anexo criado:', response.data);
});
```

## 🔒 Segurança

### Validações Implementadas

1. **Tipo de arquivo**: Apenas extensões permitidas
2. **Tamanho**: Limite de 10MB por arquivo
3. **Nome único**: UUID para evitar sobrescrever
4. **Organização**: Arquivos separados por projeto

### Melhorias Recomendadas

#### 1. Verificação de Tipo MIME Real
```java
// Adicionar ao FileService
private boolean isValidFileType(InputStream inputStream) throws IOException {
    byte[] header = new byte[8];
    inputStream.mark(8);
    inputStream.read(header);
    inputStream.reset();
    
    // Verifica magic numbers (PDF, JPEG, PNG, etc)
    // ...
}
```

#### 2. Scan de Vírus
```java
// Integrar com ClamAV ou similar
@Inject
VirusScanService virusScanService;

public String saveFile(InputStream inputStream, String fileName) {
    if (!virusScanService.isSafe(inputStream)) {
        throw new SecurityException("Arquivo contém vírus");
    }
    // ...
}
```

#### 3. Autenticação
```java
@RolesAllowed("ACADEMICO")
public Response upload(...) {
    // Apenas acadêmicos podem fazer upload
}
```

## 📂 Estrutura de Armazenamento

```
uploads/
└── projetos/
    ├── 1/
    │   ├── abc123-def456.pdf
    │   └── 789ghi-012jkl.docx
    ├── 2/
    │   └── mno345-pqr678.xlsx
    └── 3/
        └── stu901-vwx234.png
```

**Benefícios:**
- ✅ Organização por projeto
- ✅ Fácil backup por projeto
- ✅ Nomes únicos (UUID)
- ✅ Fácil limpeza ao deletar projeto

## 🗑️ Limpeza de Arquivos Órfãos

### Script de Limpeza

```java
@Scheduled(every = "24h")
@Transactional
public void cleanupOrphanFiles() {
    Path uploadPath = Paths.get(uploadDirectory, "projetos");
    
    try (Stream<Path> paths = Files.walk(uploadPath)) {
        paths.filter(Files::isRegularFile)
             .forEach(filePath -> {
                 String relativePath = uploadPath.relativize(filePath).toString();
                 
                 // Verifica se arquivo existe no banco
                 boolean exists = anexoRepository
                     .find("urlArquivo", "projetos/" + relativePath)
                     .count() > 0;
                 
                 if (!exists) {
                     Files.deleteIfExists(filePath);
                     System.out.println("Arquivo órfão deletado: " + relativePath);
                 }
             });
    } catch (IOException e) {
        System.err.println("Erro na limpeza: " + e.getMessage());
    }
}
```

## 🚀 Melhorias Futuras

### 1. Upload Direto para Cloud Storage

**Amazon S3:**
```java
@Inject
S3Client s3Client;

public String uploadToS3(InputStream inputStream, String fileName) {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket("meu-bucket")
        .key("projetos/" + projetoId + "/" + fileName)
        .build();
    
    s3Client.putObject(request, RequestBody.fromInputStream(inputStream, fileSize));
    
    return "https://meu-bucket.s3.amazonaws.com/projetos/" + projetoId + "/" + fileName;
}
```

### 2. Miniatura para Imagens

```java
public BufferedImage createThumbnail(File imageFile) throws IOException {
    BufferedImage original = ImageIO.read(imageFile);
    
    int width = 200;
    int height = (int) (original.getHeight() * (width / (double) original.getWidth()));
    
    BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = thumbnail.createGraphics();
    g.drawImage(original.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
    g.dispose();
    
    return thumbnail;
}
```

### 3. Upload em Chunks (Arquivos Grandes)

```javascript
async function uploadInChunks(file) {
    const chunkSize = 1024 * 1024; // 1MB
    const totalChunks = Math.ceil(file.size / chunkSize);
    
    for (let i = 0; i < totalChunks; i++) {
        const start = i * chunkSize;
        const end = Math.min(start + chunkSize, file.size);
        const chunk = file.slice(start, end);
        
        await uploadChunk(chunk, i, totalChunks);
    }
}
```

### 4. Processamento Assíncrono

```java
@ApplicationScoped
public class AsyncFileProcessor {
    
    @Inject
    @Channel("file-processing")
    Emitter<FileProcessingRequest> emitter;
    
    public void processAsync(Long anexoId) {
        emitter.send(new FileProcessingRequest(anexoId));
    }
    
    @Incoming("file-processing")
    @Blocking
    public void process(FileProcessingRequest request) {
        // Gera miniatura, extrai texto, etc
    }
}
```

## 🐛 Tratamento de Erros

### Erros Comuns

| Código | Erro | Solução |
|--------|------|---------|
| 400 | "Arquivo não enviado" | Certifique-se de enviar o campo `file` |
| 400 | "Tipo de arquivo não permitido" | Verifique extensões permitidas |
| 400 | "Arquivo excede tamanho máximo" | Reduza tamanho ou aumente limite |
| 404 | "Projeto não encontrado" | Verifique se projetoId existe |
| 404 | "Anexo não encontrado" | Verifique se anexoId existe |
| 500 | "Erro ao salvar arquivo" | Verifique permissões do diretório |

### Logs Úteis

```java
System.out.println("📁 Salvando arquivo: " + fileName);
System.out.println("📊 Tamanho: " + fileSize + " bytes");
System.out.println("📂 Destino: " + relativePath);
System.out.println("✅ Upload completo!");
```

## 📞 Suporte

Para mais informações:
- Documentação Quarkus: https://quarkus.io/guides/rest-json
- JAX-RS Multipart: https://docs.jboss.org/resteasy/docs/

