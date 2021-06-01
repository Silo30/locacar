package inmotion.sistema.uninassau.pe.model.eventos;

import java.io.Serializable;

public class Gasto extends Evento implements Serializable {
    private String tipo;
    private String observacao;
    private float valor;

    public Gasto(int evento_id, String data, String tipo, float valor, String observacao) {
        super(data, evento_id);
        this.tipo = tipo;
        this.valor = valor;
        this.observacao = observacao;
    }

    public Gasto() {
        super();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
