package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.dto.VotoDTO;
import br.unitins.topicos1.model.Cidadao;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.model.StatusProjeto;
import br.unitins.topicos1.model.Voto;
import br.unitins.topicos1.repository.VotoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VotoService {

    @Inject
    VotoRepository repository;

    @Inject
    ProjetoService projetoService;

    @Inject
    CidadaoService cidadaoService;

    public List<Voto> findByProjeto(Long projetoId) {
        return repository.findByProjeto(projetoId);
    }

    public List<Voto> findByCidadao(Long cidadaoId) {
        return repository.findByCidadao(cidadaoId);
    }

    @Transactional
    public Voto votar(VotoDTO dto, String ipAddress) {
        Projeto projeto = projetoService.findById(dto.projetoId());
        Cidadao cidadao = cidadaoService.findById(dto.cidadaoId());

        // Validar se projeto pode ser votado
        if (!projeto.getStatus().equals(StatusProjeto.APROVADO) &&
                !projeto.getStatus().equals(StatusProjeto.EM_EXECUCAO)
                && !projeto.getStatus().equals(StatusProjeto.AGUARDANDO_AVALIACAO))

            throw new IllegalArgumentException("Projeto não disponível para votação");

        // Verificar se cidadão já votou NESTE projeto
        Voto votoExistente = repository.findByProjetoAndCidadao(dto.projetoId(), dto.cidadaoId());
        if (votoExistente != null)
            throw new IllegalArgumentException("Você já votou neste projeto");

        Voto voto = new Voto();
        voto.setProjeto(projeto);
        voto.setCidadao(cidadao);
        voto.setDataVoto(LocalDateTime.now());
        voto.setIpAddress(ipAddress);

        repository.persist(voto);

        // Atualizar total de votos do projeto
        Long totalVotos = repository.countByProjeto(dto.projetoId());
        projetoService.atualizarTotalVotos(dto.projetoId(), totalVotos.intValue());

        // Adicionar pontos ao cidadão
        cidadaoService.adicionarPontos(dto.cidadaoId(), 5);

        return voto;
    }

    @Transactional
    public void removerVoto(Long projetoId, Long cidadaoId) {
        Voto voto = repository.findByProjetoAndCidadao(projetoId, cidadaoId);

        if (voto == null)
            throw new IllegalArgumentException("Voto não encontrado");

        repository.delete(voto);

        // Atualizar total de votos do projeto
        Long totalVotos = repository.countByProjeto(projetoId);
        projetoService.atualizarTotalVotos(projetoId, totalVotos.intValue());

        // Remover pontos do cidadão
        cidadaoService.adicionarPontos(cidadaoId, -5);
    }
}
