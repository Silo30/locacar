package inmotion.sistema.uninassau.pe.model.meiosdetransporte;

import java.io.Serializable;

public class MeioDeTransporte implements Serializable {
    private int id;
    private String descricao;
    private String tipo;

    public MeioDeTransporte() {

    }
    public MeioDeTransporte(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo(){return this.tipo;}

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString(){
        return this.descricao;
    }
}
