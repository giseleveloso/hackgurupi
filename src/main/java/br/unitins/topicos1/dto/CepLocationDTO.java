package br.unitins.topicos1.dto;

import java.math.BigDecimal;

public record CepLocationDTO(
    String cep,
    BigDecimal latitude,
    BigDecimal longitude,
    String logradouro,
    String bairro,
    String localidade,
    String uf
) {}

