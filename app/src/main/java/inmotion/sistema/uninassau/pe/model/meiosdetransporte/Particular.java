package inmotion.sistema.uninassau.pe.model.meiosdetransporte;

import java.io.Serializable;


public class Particular extends MeioDeTransporte implements Serializable {

    private String marca;
    private String modelo;
    private String cor;


    public Particular(int evento_id, String descricao, String marca, String modelo, String cor) {
        super(evento_id,descricao);
        super.setTipo("PARTICULAR");
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
    }

    public Particular() {
        super();
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}