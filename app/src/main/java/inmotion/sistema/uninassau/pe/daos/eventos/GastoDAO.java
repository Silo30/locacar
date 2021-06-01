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
import inmotion.sistema.uninassau.pe.model.eventos.Gasto;

public class GastoDAO {

    private static final String queryByID   = "SELECT * FROM GASTO WHERE ID = ?";
    private static final String queryAll    = "SELECT * FROM GASTO";

    private CriaBancoCompleto db;
    private Context mContext;

    public GastoDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }

    public long insert(Gasto gasto) {
        SQLiteDatabase banco = null;
        EventoDAO edao = new EventoDAO(this.mContext);
        long id = edao.insert(gasto);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao inserir novo evento de gasto.", Toast.LENGTH_LONG).show();
        }else{
            banco = db.getInstance(mContext).getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("EVENTO_ID", id);
            valores.put("TIPO", gasto.getTipo());
            valores.put("VALOR", gasto.getValor());
            valores.put("OBSERVACAO", gasto.getObservacao());
            banco.insert("GASTO", null, valores);
            banco.close();
        }
        return id;
    }

    public long update(Gasto evt) {
        EventoDAO edao = new EventoDAO(this.mContext);
        long id = edao.update(evt);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao atualizar gasto.", Toast.LENGTH_LONG).show();
        }else{
            ContentValues valores = new ContentValues();
            valores.put("TIPO", evt.getTipo());
            valores.put("VALOR", evt.getValor());
            valores.put("OBSERVACAO", evt.getObservacao());
            SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
            id = banco.update("GASTO",valores,"EVENTO_ID = ?",new String[]{Integer.toString(evt.getId())});
            if(id == -1L){
                Toast.makeText(mContext, "Falha ao atualizar Despesa.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mContext, "Despesa Atualizada com Sucesso!.", Toast.LENGTH_LONG).show();
            }
            banco.close();
        }
        return id;
    }

    public Gasto readByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        Evento m = null;
        Gasto g = null;
        EventoDAO edao = new EventoDAO(mContext);
        m = edao.readByID(id);
        if(m != null){
            cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
            if(cursor.moveToFirst()){
                g = new Gasto();
                g.setId(id);
                g.setData(m.getData());
                g.setMeiodetransporte_id(m.getMeiodetransporte_id());
                g.setTipo(cursor.getString(1));
                g.setValor(cursor.getFloat(2));
                g.setObservacao(cursor.getString(3));
            }
        }
        return g;
    }

    public List<Gasto> readAll() {
        List<Gasto> lista = new ArrayList<>();
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(queryAll, null);
        EventoDAO edao = new EventoDAO(mContext);
        if (cursor.moveToFirst()) {
            Gasto g = null;
            Evento m = null;
            do {
                m = edao.readByID(cursor.getInt(0));
                g = new Gasto();
                g.setId(m.getId());
                g.setData(m.getData());
                g.setMeiodetransporte_id(m.getMeiodetransporte_id());
                g.setTipo(cursor.getString(1));
                g.setValor(cursor.getFloat(2));
                g.setObservacao(cursor.getString(3));
                lista.add(g);
            } while (cursor.moveToNext());
        }
        cursor.close();
        banco.close();
        db.close();
        return lista;
    }

    public long deleteByID(Gasto gasto){
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        EventoDAO mdao = new EventoDAO(mContext);
        long id = banco.delete("GASTO", "EVENTO_ID = ?", new String[]{Integer.toString(gasto.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao remover evento de gasto", Toast.LENGTH_LONG).show();
        }
        mdao.deleteByID(gasto);
        banco.close();
        return id;
    }
}
