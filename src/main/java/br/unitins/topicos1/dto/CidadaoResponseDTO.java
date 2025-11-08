package br.unitins.topicos1.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.unitins.topicos1.model.Cidadao;

public record CidadaoResponseDTO(
    Long id,
    String nome,
    String email,
    String cpf,
    LocalDate dataNascimento,
    Integer pontuacao,
    LocalDateTime dataCadastro,
    Boolean ativo,
    String cep,
    String bairro,
    String interessesAreas
) {
    public static CidadaoResponseDTO valueOf(Cidadao cidadao) {
        return new CidadaoResponseDTO(
            cidadao.getId(),
            cidadao.getNome(),
            cidadao.getEmail(),
            cidadao.getCpf(),
            cidadao.getDataNascimento(),
            cidadao.getPontuacao(),
            cidadao.getDataCadastro(),
            cidadao.getAtivo(),
            cidadao.getCep(),
            cidadao.getBairro(),
            cidadao.getInteressesAreas()
        );
    }
}

