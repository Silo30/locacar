package inmotion.sistema.uninassau.pe.model.meiosdetransporte;

import java.io.Serializable;

/**
 * Created by mirandagab on 07/07/2018.
 */
public class Publico extends MeioDeTransporte implements Serializable {
    private String empresa;

    public Publico(int evento_id, String descricao, String empresa) {
        super(evento_id, descricao);
        super.setTipo("PUBLICO");
        this.empresa = empresa;
    }

    public Publico() {
        super();
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

}
