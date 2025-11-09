package br.unitins.topicos1.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Cliente HTTP para comunicação com a API do Google Gemini
 */
@ApplicationScoped
public class GeminiClient {

    private static final Logger LOG = Logger.getLogger(GeminiClient.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Gera conteúdo JSON usando o Gemini
     * 
     * @param apiKey Chave da API do Gemini
     * @param systemPrompt Prompt do sistema com instruções
     * @param userJson Entrada do usuário/dados
     * @return Resposta JSON do Gemini (formatada e extraída)
     * @throws IOException em caso de erro na comunicação
     */
    public String generateJson(String apiKey, String systemPrompt, String userJson) throws IOException {
        String urlStr = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent?key="
                + apiKey;
        String requestBody = buildBody(systemPrompt, userJson);

        int attempts = 0;
        int maxAttempts = 3;
        IOException last = null;
        
        LOG.info("🔄 Iniciando comunicação com Google Gemini API...");
        
        // Retry logic para lidar com rate limiting (429) e erros de rede
        while (attempts < maxAttempts) {
            attempts++;
            LOG.infof("📡 Tentativa %d/%d de conectar ao Gemini...", attempts, maxAttempts);
            
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setConnectTimeout(30000); // 30 segundos timeout
                con.setReadTimeout(60000); // 60 segundos timeout de leitura
                con.setDoOutput(true);

                // Envia o corpo da requisição
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int status = con.getResponseCode();
                LOG.infof("📊 Status HTTP recebido: %d", status);
                
                // Se rate limited, aguarda e tenta novamente
                if (status == 429) {
                    long waitTime = 1000L * attempts;
                    LOG.warnf("⏱️  Rate limited (429), aguardando %d ms antes da próxima tentativa...", waitTime);
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                
                // Se erro, lê a resposta de erro
                if (status != 200) {
                    String errorDetails = "";
                    try {
                        reader = new BufferedReader(new InputStreamReader(
                            con.getErrorStream() != null ? con.getErrorStream() : con.getInputStream(), 
                            StandardCharsets.UTF_8));
                        StringBuilder err = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            err.append(line);
                        }
                        errorDetails = err.toString();
                    } catch (Exception e) {
                        errorDetails = "Não foi possível ler detalhes do erro";
                    }
                    
                    LOG.errorf("❌ Gemini API retornou HTTP %d: %s", status, errorDetails);
                    
                    String errorMessage = String.format("Erro HTTP %d na API do Gemini", status);
                    if (status == 400) {
                        errorMessage += " (Requisição inválida - verifique o formato dos dados)";
                    } else if (status == 401 || status == 403) {
                        errorMessage += " (Chave da API inválida ou sem permissão)";
                    } else if (status == 404) {
                        errorMessage += " (Modelo não encontrado)";
                    } else if (status >= 500) {
                        errorMessage += " (Erro no servidor do Google)";
                    }
                    
                    throw new IOException(errorMessage + ": " + errorDetails);
                }

                // Lê a resposta de sucesso
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                String jsonResponse = sb.toString().trim();
                
                LOG.info("✅ Resposta recebida do Google Gemini com sucesso!");
                return extractPrettyJsonFromCandidates(jsonResponse);
                
            } catch (IOException e) {
                last = e;
                LOG.errorf("⚠️ Erro na tentativa %d: %s", attempts, e.getMessage());
                
                // Se não for a última tentativa, aguarda antes de tentar novamente
                if (attempts < maxAttempts) {
                    long waitTime = 1000L * attempts;
                    LOG.infof("⏱️  Aguardando %d ms antes da próxima tentativa...", waitTime);
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {}
                }
                if (con != null) {
                    con.disconnect();
                }
            }
        }
        
        String errorMsg = last != null ? last.getMessage() : "Erro desconhecido";
        LOG.errorf("❌ Falha após %d tentativas. Último erro: %s", maxAttempts, errorMsg);
        throw new IOException("Falha ao chamar Gemini após " + maxAttempts + " tentativas: " + errorMsg);
    }

    /**
     * Constrói o corpo da requisição para o Gemini
     */
    private String buildBody(String systemPrompt, String userJson) {
        String escapedSystem = systemPrompt.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        String escapedUser = userJson.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        
        return "{" +
                "\"contents\":[" +
                "{\"role\":\"user\",\"parts\":[{\"text\":\"SYSTEM:\\n" + escapedSystem + "\"}]}," +
                "{\"role\":\"user\",\"parts\":[{\"text\":\"" + escapedUser + "\"}]}]," +
                "\"generationConfig\":{\"temperature\":0.2,\"response_mime_type\":\"application/json\"}}";
    }

    /**
     * Extrai o JSON da estrutura de resposta do Gemini e formata
     */
    private String extractPrettyJsonFromCandidates(String jsonResponse) {
        try {
            JsonNode root = MAPPER.readTree(jsonResponse);
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            
            if (textNode.isMissingNode() || textNode.isNull()) {
                return jsonResponse;
            }
            
            String inner = textNode.asText();

            // Algumas respostas vêm com markdown code fences, remover se existir
            inner = inner.strip();
            if (inner.startsWith("```")) {
                int first = inner.indexOf('\n');
                int last = inner.lastIndexOf("```");
                if (first >= 0 && last > first) {
                    inner = inner.substring(first + 1, last);
                }
            }

            JsonNode innerJson = MAPPER.readTree(inner);
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(innerJson);
        } catch (Exception e) {
            LOG.warnf("Não foi possível extrair JSON limpo: %s", e.getMessage());
            return jsonResponse;
        }
    }
}

