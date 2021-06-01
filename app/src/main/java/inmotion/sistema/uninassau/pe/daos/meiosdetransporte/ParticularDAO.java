package inmotion.sistema.uninassau.pe.daos.meiosdetransporte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inmotion.sistema.uninassau.pe.daos.CriaBancoCompleto;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Particular;

public class ParticularDAO {
    private static final String queryByID   = "SELECT * FROM PARTICULAR WHERE MEIODETRANSPORTE_ID = ?";
    private static final String queryAll    = "SELECT * FROM PARTICULAR";

    private CriaBancoCompleto db;
    private Context mContext;

    public ParticularDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }

    public long insert(Particular particular) {
        SQLiteDatabase banco = null;
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(this.mContext);
        long id = mdao.insert(particular);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao inserir novo meio de transporte particular.", Toast.LENGTH_LONG).show();
        }else{
            banco = db.getInstance(mContext).getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("MEIODETRANSPORTE_ID", id);
            valores.put("MARCA", particular.getMarca());
            valores.put("MODELO", particular.getModelo());
            valores.put("COR", particular.getCor());
            banco.insert("PARTICULAR", null, valores);
            banco.close();
        }
        return id;
    }

    public long update(Particular particular) {
        MeiosDeTransporteDAO edao = new MeiosDeTransporteDAO(this.mContext);
        long id = edao.update(particular);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte.", Toast.LENGTH_LONG).show();
        }else{
            ContentValues valores = new ContentValues();
            valores.put("MARCA", particular.getMarca());
            valores.put("MODELO", particular.getModelo());
            valores.put("COR", particular.getCor());
            SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
            id = banco.update("PARTICULAR",valores,"MEIODETRANSPORTE_ID = ?",new String[]{Integer.toString(particular.getId())});
            if(id == -1L){
                Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte Particular.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mContext, "Meio de Transporte Particular Atualizado com Sucesso!.", Toast.LENGTH_LONG).show();
            }
            banco.close();
        }
        return id;
    }

    public Particular readByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        MeioDeTransporte m = null;
        Particular p = null;
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        m = mdao.readByID(id);
        if(m != null){
            cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
            if(cursor.moveToFirst()){
                p = new Particular();
                p.setId(id);
                p.setTipo(m.getTipo());
                p.setDescricao(m.getDescricao());
                p.setMarca(cursor.getString(1));
                p.setModelo(cursor.getString(2));
                p.setCor(cursor.getString(3));
            }
        }
        return p;
    }

    public List<Particular> readAll() {
        List<Particular> lista = new ArrayList<>();
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(queryAll, null);
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        if (cursor.moveToFirst()) {
            Particular p = null;
            MeioDeTransporte m = null;
            do {
                m = mdao.readByID(cursor.getInt(0));
                p = new Particular();
                p.setId(m.getId());
                p.setTipo(m.getTipo());
                p.setDescricao(m.getDescricao());
                p.setMarca(cursor.getString(1));
                p.setModelo(cursor.getString(2));
                p.setCor(cursor.getString(3));
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        banco.close();
        db.close();
        return lista;
    }

    public long deleteByID(Particular particular){
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        long id = banco.delete("PARTICULAR", "MEIODETRANSPORTE_ID = ?", new String[]{Integer.toString(particular.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao remover meio de transporte particular.", Toast.LENGTH_LONG).show();
        }
        mdao.deleteByID(particular);
        banco.close();
        return id;
    }
}
