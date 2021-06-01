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


public class EventoDAO {

    private static final String queryByID   = "SELECT * FROM EVENTO WHERE ID = ?";
    private static final String queryAll    = "SELECT * FROM EVENTO";


    private CriaBancoCompleto db;
    private Context mContext;

    public EventoDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }

    public long insert(Evento evt) {
        ContentValues valores = new ContentValues();
        valores.put("MEIODETRANSPORTE_ID", evt.getMeiodetransporte_id());
        valores.put("DATA", evt.getData());

        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        long id = banco.insert("EVENTO", null, valores);

        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao inserir novo evento.", Toast.LENGTH_LONG).show();
        }
        banco.close();
        return id;
    }

    public long update(Evento evt) {
        ContentValues valores = new ContentValues();
        valores.put("DATA", evt.getData());
        valores.put("MEIODETRANSPORTE_ID", evt.getMeiodetransporte_id());

        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        long id = banco.update("EVENTO",valores,"ID = ?",new String[]{Integer.toString(evt.getId())});

        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao atualizar evento.", Toast.LENGTH_LONG).show();
        }
        banco.close();
        return id;
    }

    public Evento readByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        Evento m = null;
        cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
        if(cursor.moveToFirst()){
            System.out.println(cursor.getString(2));
            m = new Evento(cursor.getString(2), cursor.getInt(1));
            m.setId(id);
        }
        return m;
    }

    public List<Evento> readAll() {
        List<Evento> lista = new ArrayList<>();
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(queryAll, null);
        if (cursor.moveToFirst()) {
            Evento m = null;
            do {
                m = new Evento(cursor.getString(2), cursor.getInt(1));
                m.setId(cursor.getInt(0));
                lista.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        banco.close();
        db.close();
        return lista;
    }

    public List<Evento> readAllSpecific(){
        List<Evento> lista = new ArrayList<Evento>();
        ViagemDAO vdao = new ViagemDAO(mContext);
        GastoDAO gdao = new GastoDAO(mContext);
        lista.addAll(vdao.readAll());
        lista.addAll(gdao.readAll());
        return lista;
    }

    public long deleteByID(Evento evt){
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        long id = banco.delete("EVENTO", "ID = ?", new String[]{Integer.toString(evt.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao remover meio de transporte.", Toast.LENGTH_LONG).show();
        }
        banco.close();
        return id;
    }
}