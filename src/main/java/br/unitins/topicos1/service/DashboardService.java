package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.DashboardResponseDTO;
import br.unitins.topicos1.model.AreaTematica;
import br.unitins.topicos1.model.StatusProjeto;
import br.unitins.topicos1.repository.AcademicoRepository;
import br.unitins.topicos1.repository.CidadaoRepository;
import br.unitins.topicos1.repository.ProjetoRepository;
import br.unitins.topicos1.repository.VotoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DashboardService {
    
    @Inject
    ProjetoRepository projetoRepository;
    
    @Inject
    VotoRepository votoRepository;
    
    @Inject
    AcademicoRepository academicoRepository;
    
    @Inject
    CidadaoRepository cidadaoRepository;
    
    public DashboardResponseDTO getDashboard() {
        Integer totalProjetos = (int) projetoRepository.count();
        Integer projetosAprovados = projetoRepository.countByStatus(StatusProjeto.APROVADO).intValue();
        Integer projetosPendentes = projetoRepository.countByStatus(StatusProjeto.AGUARDANDO_APROVACAO).intValue();
        Integer projetosEmExecucao = projetoRepository.countByStatus(StatusProjeto.EM_EXECUCAO).intValue();
        Integer projetosConcluidos = projetoRepository.countByStatus(StatusProjeto.CONCLUIDO).intValue();
        Integer totalVotos = (int) votoRepository.count();
        Integer totalAcademicos = (int) academicoRepository.count();
        Integer totalCidadaos = (int) cidadaoRepository.count();
        Integer totalUsuarios = totalAcademicos + totalCidadaos;
        
        // Mockado - seria calculado com consulta real
        String areaMaisPropostas = calcularAreaMaisPropostas();
        String areaMaisVotada = "Saúde"; // Mockado
        
        return new DashboardResponseDTO(
            totalProjetos,
            projetosAprovados,
            projetosPendentes,
            projetosEmExecucao,
            projetosConcluidos,
            totalVotos,
            totalUsuarios,
            totalAcademicos,
            totalCidadaos,
            areaMaisPropostas,
            areaMaisVotada
        );
    }
    
    private String calcularAreaMaisPropostas() {
        Long maxCount = 0L;
        AreaTematica areaMaisPropostas = null;
        
        for (AreaTematica area : AreaTematica.values()) {
            Long count = projetoRepository.countByAreaTematica(area);
            if (count > maxCount) {
                maxCount = count;
                areaMaisPropostas = area;
            }
        }
        
        return areaMaisPropostas != null ? areaMaisPropostas.getLabel() : "Nenhuma";
    }
}

