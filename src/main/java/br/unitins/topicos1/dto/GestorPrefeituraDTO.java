package br.unitins.topicos1.dto;

import java.time.LocalDate;

public record GestorPrefeituraDTO(
    String nome,
    String email,
    String cpf,
    String senha,
    LocalDate dataNascimento,
    String secretaria,
    String cargo,
    Integer nivelAcesso
) {}

