package inmotion.sistema.uninassau.pe.model.meiosdetransporte;

import java.io.Serializable;

public class Compartilhado extends MeioDeTransporte implements Serializable {
    private String empresa;

    public Compartilhado(int evento_id, String descricao, String empresa) {
        super(evento_id, descricao);
        this.empresa = empresa;
        super.setTipo("COMPARTILHADO");
    }

    public Compartilhado() {
        super();
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
}
