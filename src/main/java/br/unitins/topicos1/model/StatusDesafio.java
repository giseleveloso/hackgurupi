package br.unitins.topicos1.model;

public enum StatusDesafio {
    ATIVO(1, "Ativo"),
    ENCERRADO(2, "Encerrado"),
    PLANEJAMENTO(3, "Planejamento");

    private final Integer id;
    private final String label;

    StatusDesafio(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static StatusDesafio valueOf(Integer id) {
        if (id == null)
            return null;
        for (StatusDesafio status : StatusDesafio.values()) {
            if (status.getId().equals(id))
                return status;
        }
        return null;
    }
}

