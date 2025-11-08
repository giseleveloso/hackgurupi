package br.unitins.topicos1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unitins.topicos1.dto.CepLocationDTO;
import br.unitins.topicos1.dto.HeatPointDTO;
import br.unitins.topicos1.model.Voto;
import br.unitins.topicos1.repository.VotoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class HeatmapService {
    
    @Inject
    VotoRepository votoRepository;
    
    @Inject
    GeocodingService geocodingService;
    
    /**
     * Gera dados do mapa de calor com base nos votos
     * 
     * @param projetoId Filtro opcional por projeto
     * @param precision Precisão da agregação (casas decimais: 3 = ~100m, 4 = ~10m)
     * @return Lista de pontos para o mapa de calor
     */
    public List<HeatPointDTO> generateHeatmap(Long projetoId, Integer precision) {
        // Busca todos os votos (ou filtrados por projeto)
        List<Voto> votos = projetoId != null 
            ? votoRepository.findByProjeto(projetoId)
            : votoRepository.listAll();
        
        if (votos.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Mapa para agregar votos por localização arredondada
        Map<String, Long> locationVotes = new HashMap<>();
        Map<String, BigDecimal[]> locationCoords = new HashMap<>();
        
        // Processa cada voto
        for (Voto voto : votos) {
            try {
                String cep = voto.getCidadao().getCep();
                if (cep == null || cep.isEmpty()) {
                    continue;
                }
                
                // Geocodifica o CEP
                CepLocationDTO location = geocodingService.geocodeCep(cep);
                
                // Arredonda coordenadas para agregar votos próximos
                BigDecimal roundedLat = location.latitude().setScale(precision, RoundingMode.HALF_UP);
                BigDecimal roundedLng = location.longitude().setScale(precision, RoundingMode.HALF_UP);
                
                String key = roundedLat + "," + roundedLng;
                
                // Incrementa contagem para esta localização
                locationVotes.put(key, locationVotes.getOrDefault(key, 0L) + 1);
                locationCoords.put(key, new BigDecimal[] { roundedLat, roundedLng });
                
            } catch (Exception e) {
                System.err.println("Erro ao processar voto: " + e.getMessage());
                // Continua processando os outros votos
            }
        }
        
        if (locationVotes.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Encontra o máximo de votos para normalização
        Long maxVotes = locationVotes.values().stream()
            .max(Long::compareTo)
            .orElse(1L);
        
        // Gera lista de pontos do mapa de calor
        List<HeatPointDTO> heatPoints = new ArrayList<>();
        for (Map.Entry<String, Long> entry : locationVotes.entrySet()) {
            BigDecimal[] coords = locationCoords.get(entry.getKey());
            Long voteCount = entry.getValue();
            
            heatPoints.add(HeatPointDTO.of(coords[0], coords[1], voteCount, maxVotes));
        }
        
        return heatPoints;
    }
    
    /**
     * Gera mapa de calor para um projeto específico
     */
    public List<HeatPointDTO> generateHeatmapForProjeto(Long projetoId) {
        return generateHeatmap(projetoId, 3); // precisão padrão: ~100m
    }
    
    /**
     * Gera mapa de calor geral (todos os votos)
     */
    public List<HeatPointDTO> generateGeneralHeatmap() {
        return generateHeatmap(null, 3);
    }
}

