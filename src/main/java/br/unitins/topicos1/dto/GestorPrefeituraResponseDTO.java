package br.unitins.topicos1.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.unitins.topicos1.model.GestorPrefeitura;

public record GestorPrefeituraResponseDTO(
    Long id,
    String nome,
    String email,
    String cpf,
    LocalDate dataNascimento,
    Integer pontuacao,
    LocalDateTime dataCadastro,
    Boolean ativo,
    String secretaria,
    String cargo,
    Integer nivelAcesso
) {
    public static GestorPrefeituraResponseDTO valueOf(GestorPrefeitura gestor) {
        return new GestorPrefeituraResponseDTO(
            gestor.getId(),
            gestor.getNome(),
            gestor.getEmail(),
            gestor.getCpf(),
            gestor.getDataNascimento(),
            gestor.getPontuacao(),
            gestor.getDataCadastro(),
            gestor.getAtivo(),
            gestor.getSecretaria(),
            gestor.getCargo(),
            gestor.getNivelAcesso()
        );
    }
}

