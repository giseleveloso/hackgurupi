package br.unitins.topicos1.resource;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.unitins.topicos1.client.GeminiClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint de teste para verificar conexão com Google Gemini API
 */
@Path("/test/gemini")
@Produces(MediaType.APPLICATION_JSON)
public class GeminiTestResource {
    
    @ConfigProperty(name = "gemini.api.key", defaultValue = "")
    String geminiApiKey;
    
    @Inject
    GeminiClient geminiClient;
    
    /**
     * GET /test/gemini - Testa conexão com a API do Gemini
     */
    @GET
    public Response testConnection() {
        try {
            // Verifica se a chave está configurada
            if (geminiApiKey == null || geminiApiKey.isEmpty()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\": \"error\", \"message\": \"Chave da API não configurada\"}")
                    .build();
            }
            
            // Testa com uma requisição simples
            String systemPrompt = "Você é um assistente útil. Responda apenas em JSON no formato: {\"message\": \"sua resposta aqui\"}";
            String userInput = "Diga olá em JSON";
            
            System.out.println("\n🧪 === TESTE DE CONEXÃO COM GEMINI API === 🧪");
            System.out.println("🔑 Chave da API (primeiros 10 caracteres): " + geminiApiKey.substring(0, Math.min(10, geminiApiKey.length())) + "...");
            
            String response = geminiClient.generateJson(geminiApiKey, systemPrompt, userInput);
            
            System.out.println("✅ Teste concluído com sucesso!");
            System.out.println("📝 Resposta: " + response);
            
            return Response.ok()
                .entity("{\"status\": \"success\", \"message\": \"Conexão com Gemini OK\", \"response\": " + response + "}")
                .build();
                
        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"status\": \"error\", \"message\": \"" + e.getMessage().replace("\"", "'") + "\"}")
                .build();
        }
    }
}

