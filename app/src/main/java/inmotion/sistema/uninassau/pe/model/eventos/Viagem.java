package inmotion.sistema.uninassau.pe.model.eventos;

import java.io.Serializable;

public class Viagem extends Evento implements Serializable {
    private String inicio;
    private String fim;
    private float distancia;

    public Viagem(int evento_id, String data, String inicio, String fim, float distancia) {
        super(data,evento_id);
        this.inicio = inicio;
        this.fim = fim;
        this.distancia = distancia;
    }

    public Viagem() {
        super();
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }
}