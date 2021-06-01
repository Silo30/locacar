package inmotion.sistema.uninassau.pe.model.eventos;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Evento implements Serializable{
    private int id;
    private Timestamp data;
    private int meiodetransporte_id;
    private static SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");

    public Evento(){

    }
    public Evento(String data, int meiodetransporte_id) {
        try{
            this.data = new Timestamp(in.parse(data).getTime());
        }catch(ParseException pe){
            try{
                this.data = new Timestamp(out.parse(data).getTime());
            }catch (ParseException pe2){
                throw new IllegalArgumentException("Formato de data inválido para cadastro de evento!");
            }
        }
        this.meiodetransporte_id = meiodetransporte_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return out.format(new Date(this.data.getTime()));
    }

    public void setData(String data) {
        try{
            this.data = new Timestamp(out.parse(data).getTime());
        }catch(ParseException ex){
            try{
                this.data = new Timestamp(in.parse(data).getTime());
            }catch (ParseException pe2){
                throw new IllegalArgumentException("Formato de data inválido para cadastro de evento!");
            }
        }
    }

    public int getMeiodetransporte_id(){return this.meiodetransporte_id;}

    public void setMeiodetransporte_id(int meiodetransporte_id){this.meiodetransporte_id = meiodetransporte_id;}
}
