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
        IOException last = null;
        
        // Retry logic para lidar com rate limiting (429)
        while (attempts < 2) {
            attempts++;
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);

                // Envia o corpo da requisição
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int status = con.getResponseCode();
                
                // Se rate limited, aguarda e tenta novamente
                if (status == 429) {
                    LOG.warnf("Rate limited (429), tentando novamente em %d ms...", 800L * attempts);
                    try {
                        Thread.sleep(800L * attempts);
                    } catch (InterruptedException ignored) {
                    }
                    continue;
                }
                
                // Se erro, lê a resposta de erro
                if (status != 200) {
                    reader = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
                    StringBuilder err = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        err.append(line);
                    }
                    LOG.errorf("Gemini HTTP %d: %s", status, err.toString());
                    throw new IOException("Gemini HTTP " + status + ": " + err.toString());
                }

                // Lê a resposta de sucesso
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                String jsonResponse = sb.toString().trim();
                
                System.out.println("✅ Resposta recebida do Google Gemini");
                return extractPrettyJsonFromCandidates(jsonResponse);
                
            } catch (IOException e) {
                last = e;
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
        }
        throw last != null ? last : new IOException("Falha ao chamar Gemini após múltiplas tentativas");
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

