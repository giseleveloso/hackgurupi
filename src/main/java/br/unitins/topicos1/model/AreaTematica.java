package br.unitins.topicos1.model;

public enum AreaTematica {
    MOBILIDADE(1, "Mobilidade"),
    SAUDE(2, "Saúde"),
    EDUCACAO(3, "Educação"),
    MEIO_AMBIENTE(4, "Meio Ambiente"),
    SEGURANCA(5, "Segurança"),
    ASSISTENCIA_SOCIAL(6, "Assistência Social"),
    CULTURA(7, "Cultura"),
    TECNOLOGIA(8, "Tecnologia"),
    INFRAESTRUTURA(9, "Infraestrutura"),
    OUTROS(10, "Outros");

    private final Integer id;
    private final String label;

    AreaTematica(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static AreaTematica valueOf(Integer id) {
        if (id == null)
            return null;
        for (AreaTematica area : AreaTematica.values()) {
            if (area.getId().equals(id))
                return area;
        }
        return null;
    }
}

