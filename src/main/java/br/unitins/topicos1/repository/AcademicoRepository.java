package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Academico;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcademicoRepository implements PanacheRepository<Academico> {
    
    public Academico findByEmail(String email) {
        return find("UPPER(email) = ?1", email.toUpperCase()).firstResult();
    }
    
    public Academico findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
    
    public List<Academico> findByInstituicao(String instituicao) {
        return find("UPPER(instituicao) LIKE ?1", "%" + instituicao.toUpperCase() + "%").list();
    }
    
    public List<Academico> findAtivos() {
        return find("ativo = true ORDER BY nome").list();
    }
}

