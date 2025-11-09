package br.unitins.topicos1.repository;

import java.util.ArrayList;
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

    public List<Projeto> findByAcademicoWithFilters(
        Long academicoId,
        Integer statusId,
        Integer areaId,
        String q,
        String order
    ) {
        StringBuilder where = new StringBuilder("academico.id = ?1");
        List<Object> params = new ArrayList<>();
        params.add(academicoId);
        int idx = 2;

        if (statusId != null) {
            where.append(" AND status = ?").append(idx);
            params.add(statusId);
            idx++;
        }
        if (areaId != null) {
            where.append(" AND areaTematica = ?").append(idx);
            params.add(areaId);
            idx++;
        }
        if (q != null && !q.isBlank()) {
            String search = q.trim().toLowerCase();
            if (!search.isEmpty()) {
                where.append(" AND LOWER(titulo) LIKE ?").append(idx);
                params.add("%" + search + "%");
                idx++;
            }
        }

        String orderNormalized = order != null ? order.trim().toLowerCase() : "";
        String orderBy = " ORDER BY dataSubmissao DESC";
        if ("antigos".equals(orderNormalized)) {
            orderBy = " ORDER BY dataSubmissao ASC";
        } else if ("votados".equals(orderNormalized)) {
            orderBy = " ORDER BY totalVotos DESC";
        } else if ("recentes".equals(orderNormalized)) {
            orderBy = " ORDER BY dataSubmissao DESC";
        }

        return find(where.toString() + orderBy, params.toArray()).list();
    }
}

