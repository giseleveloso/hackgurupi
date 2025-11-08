package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Cidadao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CidadaoRepository implements PanacheRepository<Cidadao> {
    
    public Cidadao findByEmail(String email) {
        return find("UPPER(email) = ?1", email.toUpperCase()).firstResult();
    }
    
    public Cidadao findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
    
    public List<Cidadao> findByBairro(String bairro) {
        return find("UPPER(bairro) LIKE ?1", "%" + bairro.toUpperCase() + "%").list();
    }
    
    public List<Cidadao> findAtivos() {
        return find("ativo = true ORDER BY nome").list();
    }
}

