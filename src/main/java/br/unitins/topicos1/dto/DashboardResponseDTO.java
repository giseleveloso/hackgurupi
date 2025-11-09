package br.unitins.topicos1.dto;

public record DashboardResponseDTO(
    Integer totalProjetos,
    Integer projetosAprovados,
    Integer projetosPendentes,
    Integer projetosEmExecucao,
    Integer projetosConcluidos,
    Integer totalVotos,
    Integer totalUsuarios,
    Integer totalAcademicos,
    Integer totalCidadaos,
    String areaMaisPropostas,
    String areaMaisVotada,
    Integer votosUltimas24h,
    Integer comentariosUltimas24h,
    Integer usuariosAtivos
) {}

