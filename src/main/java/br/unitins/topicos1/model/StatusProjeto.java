package br.unitins.topicos1.model;

public enum StatusProjeto {
    RASCUNHO(1, "Rascunho"),
    AGUARDANDO_AVALIACAO(2, "Aguardando Avaliação"),
    AGUARDANDO_APROVACAO(3, "Aguardando Aprovação"),
    APROVADO(4, "Aprovado"),
    REJEITADO(5, "Rejeitado"),
    EM_EXECUCAO(6, "Em Execução"),
    CONCLUIDO(7, "Concluído"),
    CANCELADO(8, "Cancelado");

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

