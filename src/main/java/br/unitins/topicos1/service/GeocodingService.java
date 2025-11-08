package br.unitins.topicos1.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unitins.topicos1.dto.CepLocationDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GeocodingService {
    
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Cache simples em memória para evitar chamadas repetidas
    private final Map<String, CepLocationDTO> cache = new HashMap<>();
    
    /**
     * Geocodifica um CEP brasileiro usando ViaCEP + Nominatim
     */
    public CepLocationDTO geocodeCep(String cep) {
        String cleanCep = cep.replaceAll("\\D", "");
        
        if (cleanCep.length() != 8) {
            throw new IllegalArgumentException("CEP deve ter 8 dígitos");
        }
        
        // Verifica cache
        if (cache.containsKey(cleanCep)) {
            return cache.get(cleanCep);
        }
        
        try {
            // 1. Busca dados do CEP no ViaCEP
            String viaCepUrl = "https://viacep.com.br/ws/" + cleanCep + "/json/";
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(viaCepUrl))
                .timeout(java.time.Duration.ofSeconds(10))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro ao buscar CEP no ViaCEP");
            }
            
            JsonNode viaCepData = objectMapper.readTree(response.body());
            
            if (viaCepData.has("erro") && viaCepData.get("erro").asBoolean()) {
                throw new IllegalArgumentException("CEP não encontrado");
            }
            
            String logradouro = viaCepData.get("logradouro").asText("");
            String bairro = viaCepData.get("bairro").asText("");
            String localidade = viaCepData.get("localidade").asText("");
            String uf = viaCepData.get("uf").asText("");
            
            // 2. Geocodifica usando Nominatim (OpenStreetMap)
            BigDecimal[] coordinates = geocodeWithNominatim(logradouro, bairro, localidade, uf, cleanCep);
            
            CepLocationDTO location = new CepLocationDTO(
                cleanCep,
                coordinates[0],
                coordinates[1],
                logradouro,
                bairro,
                localidade,
                uf
            );
            
            // Adiciona ao cache
            cache.put(cleanCep, location);
            
            return location;
            
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao geocodificar CEP: " + e.getMessage(), e);
        }
    }
    
    private BigDecimal[] geocodeWithNominatim(String logradouro, String bairro, String cidade, String uf, String cep) 
            throws IOException, InterruptedException {
        
        // Tenta endereço completo primeiro
        if (!logradouro.isEmpty()) {
            String fullAddress = String.join(", ", logradouro, bairro, cidade, uf, "Brasil");
            BigDecimal[] coords = tryGeocode(fullAddress);
            if (coords != null) return coords;
            
            Thread.sleep(1000); // Rate limit do Nominatim
        }
        
        // Fallback: cidade + UF
        String cityAddress = cidade + " - " + uf + ", Brasil";
        BigDecimal[] coords = tryGeocode(cityAddress);
        if (coords != null) return coords;
        
        // Fallback final: coordenadas aproximadas de Gurupi (pode ajustar conforme sua cidade)
        // Gurupi, TO: -11.7289, -49.0683
        return new BigDecimal[] { 
            new BigDecimal("-11.7289"), 
            new BigDecimal("-49.0683") 
        };
    }
    
    private BigDecimal[] tryGeocode(String address) throws IOException, InterruptedException {
        String nominatimUrl = "https://nominatim.openstreetmap.org/search?q=" 
            + java.net.URLEncoder.encode(address, java.nio.charset.StandardCharsets.UTF_8)
            + "&format=json&limit=1";
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(nominatimUrl))
            .header("User-Agent", "HackGurupi/1.0")
            .timeout(java.time.Duration.ofSeconds(10))
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            JsonNode results = objectMapper.readTree(response.body());
            if (results.isArray() && results.size() > 0) {
                JsonNode first = results.get(0);
                BigDecimal lat = new BigDecimal(first.get("lat").asText());
                BigDecimal lon = new BigDecimal(first.get("lon").asText());
                return new BigDecimal[] { lat, lon };
            }
        }
        
        return null;
    }
    
    /**
     * Limpa o cache (útil para testes)
     */
    public void clearCache() {
        cache.clear();
    }
}

