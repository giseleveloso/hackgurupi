package br.unitins.topicos1.dto;

import java.math.BigDecimal;

public record HeatPointDTO(
    BigDecimal lat,
    BigDecimal lng,
    Double weight  // 0.0 a 1.0
) {
    public static HeatPointDTO of(BigDecimal lat, BigDecimal lng, Long voteCount, Long maxVotes) {
        // Usa escala logarítmica para normalizar o peso
        double weight = maxVotes > 0 
            ? Math.log(voteCount + 1.0) / Math.log(maxVotes + 1.0)
            : 0.0;
        return new HeatPointDTO(lat, lng, Math.min(1.0, weight));
    }
}

