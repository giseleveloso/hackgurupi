package br.unitins.topicos1.model;

public enum StatusProjeto {
    RASCUNHO(1, "Rascunho"),
    AGUARDANDO_APROVACAO(2, "Aguardando Aprovação"),
    APROVADO(3, "Aprovado"),
    REJEITADO(4, "Rejeitado"),
    EM_EXECUCAO(5, "Em Execução"),
    CONCLUIDO(6, "Concluído"),
    CANCELADO(7, "Cancelado");

    private final Integer id;
    private final String label;

    StatusProjeto(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static StatusProjeto valueOf(Integer id) {
        if (id == null)
            return null;
        for (StatusProjeto status : StatusProjeto.values()) {
            if (status.getId().equals(id))
                return status;
        }
        return null;
    }
}

