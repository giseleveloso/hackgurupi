package br.unitins.topicos1.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.unitins.topicos1.model.Academico;

public record AcademicoResponseDTO(
    Long id,
    String nome,
    String email,
    String cpf,
    LocalDate dataNascimento,
    Integer pontuacao,
    LocalDateTime dataCadastro,
    Boolean ativo,
    String instituicao,
    String curso,
    String lattes,
    String areaAtuacao,
    Boolean vinculoProfessor
) {
    public static AcademicoResponseDTO valueOf(Academico academico) {
        return new AcademicoResponseDTO(
            academico.getId(),
            academico.getNome(),
            academico.getEmail(),
            academico.getCpf(),
            academico.getDataNascimento(),
            academico.getPontuacao(),
            academico.getDataCadastro(),
            academico.getAtivo(),
            academico.getInstituicao(),
            academico.getCurso(),
            academico.getLattes(),
            academico.getAreaAtuacao(),
            academico.getVinculoProfessor()
        );
    }
}

