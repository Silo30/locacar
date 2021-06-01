package inmotion.sistema.uninassau.pe.model.meiosdetransporte;

import java.io.Serializable;

/**
 * Created by mirandagab on 07/07/2018.
 */
public class Alugado extends MeioDeTransporte implements Serializable {
    private String locadora;
    private String marca;
    private String modelo;
    private String cor;

    public Alugado(int id, String descricao, String locadora, String marca,
                   String modelo, String cor) {
        super(id,descricao);
        super.setTipo("ALUGADO");
        super.setDescricao(descricao);
        this.locadora = locadora;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
    }

    public Alugado() {
        super();
    }

    public String getLocadora() {
        return locadora;
    }

    public void setLocadora(String locadora) {
        this.locadora = locadora;
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