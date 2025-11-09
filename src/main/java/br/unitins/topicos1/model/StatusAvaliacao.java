package br.unitins.topicos1.model;

public enum StatusAvaliacao {
    PENDENTE_APROVACAO(1, "Pendente de Aprovação"),
    APROVADA(2, "Aprovada"),
    REJEITADA(3, "Rejeitada");
    
    private final Integer id;
    private final String label;
    
    StatusAvaliacao(Integer id, String label) {
        this.id = id;
        this.label = label;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static StatusAvaliacao valueOf(Integer id) {
        if (id == null) return null;
        for (StatusAvaliacao status : StatusAvaliacao.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de avaliação inválido: " + id);
    }
}

