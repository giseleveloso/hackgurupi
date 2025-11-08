package br.unitins.topicos1.dto;

import java.time.LocalDate;

public record AcademicoDTO(
    String nome,
    String email,
    String cpf,
    String senha,
    LocalDate dataNascimento,
    String instituicao,
    String curso,
    String lattes,
    String areaAtuacao,
    Boolean vinculoProfessor
) {}

