package br.unitins.topicos1.dto;

import java.time.LocalDate;

import br.unitins.topicos1.model.RepositorioDados;

public record RepositorioDadosResponseDTO(
    Long id,
    String nome,
    String descricao,
    String url,
    String fonte,
    String categoriaDados,
    LocalDate ultimaAtualizacao,
    Boolean ativo
) {
    public static RepositorioDadosResponseDTO valueOf(RepositorioDados repositorio) {
        return new RepositorioDadosResponseDTO(
            repositorio.getId(),
            repositorio.getNome(),
            repositorio.getDescricao(),
            repositorio.getUrl(),
            repositorio.getFonte(),
            repositorio.getCategoriaDados(),
            repositorio.getUltimaAtualizacao(),
            repositorio.getAtivo()
        );
    }
}

