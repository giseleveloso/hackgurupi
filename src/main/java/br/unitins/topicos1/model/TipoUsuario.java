package br.unitins.topicos1.model;

public enum TipoUsuario {
    ACADEMICO(1, "Acadêmico"),
    CIDADAO(2, "Cidadão"),
    GESTOR_PREFEITURA(3, "Gestor da Prefeitura");

    private final Integer id;
    private final String label;

    TipoUsuario(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static TipoUsuario valueOf(Integer id) {
        if (id == null)
            return null;
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.getId().equals(id))
                return tipo;
        }
        return null;
    }
}

