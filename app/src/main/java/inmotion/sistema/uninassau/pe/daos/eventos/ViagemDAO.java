package inmotion.sistema.uninassau.pe.daos.eventos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inmotion.sistema.uninassau.pe.daos.CriaBancoCompleto;
import inmotion.sistema.uninassau.pe.model.eventos.Evento;
import inmotion.sistema.uninassau.pe.model.eventos.Viagem;

public class ViagemDAO {

    private static final String queryByID   = "SELECT * FROM VIAGEM WHERE ID=?";
    private static final String queryAll    = "SELECT * FROM VIAGEM";

    private CriaBancoCompleto db;
    private Context mContext;

    public ViagemDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }
    public long insert(Viagem viagem) {
        SQLiteDatabase banco = null;
        EventoDAO edao = new EventoDAO(this.mContext);
        long id = edao.insert(viagem);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao inserir novo evento de viagem.", Toast.LENGTH_LONG).show();
        }else{
            banco = db.getInstance(mContext).getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("EVENTO_ID", id);
            valores.put("INICIO", viagem.getInicio());
            valores.put("FIM", viagem.getFim());
            valores.put("DISTANCIA", viagem.getDistancia());
            banco.insert("VIAGEM", null, valores);
            banco.close();
        }
        return id;
    }

    public long update(Viagem evt) {
        EventoDAO edao = new EventoDAO(this.mContext);
        long id = edao.update(evt);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao atualizar viagem.", Toast.LENGTH_LONG).show();
        }else{
            ContentValues valores = new ContentValues();
            valores.put("INICIO", evt.getInicio());
            valores.put("FIM", evt.getFim());
            valores.put("DISTANCIA", evt.getDistancia());
            SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
            id = banco.update("VIAGEM",valores,"EVENTO_ID = ?",new String[]{Integer.toString(evt.getId())});
            if(id == -1L){
                Toast.makeText(mContext, "Falha ao atualizar Viagem.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mContext, "Viagem Atualizada com Sucesso!.", Toast.LENGTH_LONG).show();
            }
            banco.close();
        }
        return id;
    }

    public Viagem readByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        Evento m = null;
        Viagem v = null;
        EventoDAO edao = new EventoDAO(mContext);
        m = edao.readByID(id);
        if(m != null){
            cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
            if(cursor.moveToFirst()){
                v = new Viagem();
                v.setId(id);
                v.setData(m.getData());
                v.setMeiodetransporte_id(m.getMeiodetransporte_id());
                v.setInicio(cursor.getString(1));
                v.setFim(cursor.getString(2));
                v.setDistancia(cursor.getFloat(3));
            }
        }
        return v;
    }

    public List<Viagem> readAll() {
        List<Viagem> lista = new ArrayList<>();
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(queryAll, null);
        EventoDAO edao = new EventoDAO(mContext);
        if (cursor.moveToFirst()) {
            Viagem v = null;
            Evento m = null;
            do {
                m = edao.readByID(cursor.getInt(0));
                v = new Viagem();
                v.setId(m.getId());
                v.setData(m.getData());
                v.setMeiodetransporte_id(m.getMeiodetransporte_id());
                v.setInicio(cursor.getString(1));
                v.setFim(cursor.getString(2));
                v.setDistancia(cursor.getFloat(3));
                lista.add(v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        banco.close();
        db.close();
        return lista;
    }

    public long deleteByID(Viagem viagem){
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        EventoDAO mdao = new EventoDAO(mContext);
        long id = banco.delete("VIAGEM", "EVENTO_ID = ?", new String[]{Integer.toString(viagem.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao remover evento de viagem", Toast.LENGTH_LONG).show();
        }
        mdao.deleteByID(viagem);
        banco.close();
        return id;
    }
}
