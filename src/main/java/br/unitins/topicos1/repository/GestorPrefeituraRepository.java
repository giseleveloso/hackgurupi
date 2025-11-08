package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.GestorPrefeitura;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GestorPrefeituraRepository implements PanacheRepository<GestorPrefeitura> {
    
    public GestorPrefeitura findByEmail(String email) {
        return find("UPPER(email) = ?1", email.toUpperCase()).firstResult();
    }
    
    public GestorPrefeitura findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
    
    public List<GestorPrefeitura> findBySecretaria(String secretaria) {
        return find("UPPER(secretaria) LIKE ?1", "%" + secretaria.toUpperCase() + "%").list();
    }
    
    public List<GestorPrefeitura> findAtivos() {
        return find("ativo = true ORDER BY nome").list();
    }
}

