package br.unitins.topicos1.dto;

import java.time.LocalDate;

public record CidadaoDTO(
    String nome,
    String email,
    String cpf,
    String senha,
    LocalDate dataNascimento,
    String bairro,
    String interessesAreas
) {}

