package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.AreaTematica;
import br.unitins.topicos1.model.Projeto;
import br.unitins.topicos1.model.StatusProjeto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjetoRepository implements PanacheRepository<Projeto> {
    
    public List<Projeto> findByAcademico(Long academicoId) {
        return find("academico.id = ?1 ORDER BY dataSubmissao DESC", academicoId).list();
    }
    
    public List<Projeto> findByStatus(StatusProjeto status) {
        return find("status = ?1 ORDER BY dataSubmissao DESC", status.getId()).list();
    }
    
    public List<Projeto> findByAreaTematica(AreaTematica area) {
        return find("areaTematica = ?1 ORDER BY totalVotos DESC", area.getId()).list();
    }
    
    public List<Projeto> findPublicos() {
        return find("status IN (?1, ?2, ?3) ORDER BY totalVotos DESC", 
            StatusProjeto.APROVADO.getId(),
            StatusProjeto.EM_EXECUCAO.getId(),
            StatusProjeto.CONCLUIDO.getId()).list();
    }
    
    public List<Projeto> findTopVotados(int limit) {
        return find("status IN (?1, ?2) ORDER BY totalVotos DESC", 
            StatusProjeto.APROVADO.getId(),
            StatusProjeto.EM_EXECUCAO.getId())
            .page(0, limit).list();
    }
    
    public Long countByStatus(StatusProjeto status) {
        return count("status", status.getId());
    }
    
    public Long countByAreaTematica(AreaTematica area) {
        return count("areaTematica", area.getId());
    }
}

