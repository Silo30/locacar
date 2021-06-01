package inmotion.sistema.uninassau.pe.daos.meiosdetransporte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inmotion.sistema.uninassau.pe.daos.CriaBancoCompleto;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Alugado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;

public class AlugadoDAO {
    private static final String queryByID   = "SELECT * FROM ALUGADO WHERE MEIODETRANSPORTE_ID = ?";
    private static final String queryAll    = "SELECT * FROM ALUGADO";

    private CriaBancoCompleto db;
    private Context mContext;

    public AlugadoDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }

    public long insert(Alugado alugado) {
        SQLiteDatabase banco = null;
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(this.mContext);
        long id = mdao.insert(alugado);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao inserir novo meio de transporte alugado.", Toast.LENGTH_LONG).show();
        }else{
            banco = db.getInstance(mContext).getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("MEIODETRANSPORTE_ID", id);
            valores.put("LOCADORA", alugado.getLocadora());
            valores.put("MARCA", alugado.getMarca());
            valores.put("MODELO", alugado.getModelo());
            valores.put("COR", alugado.getCor());
            banco.insert("ALUGADO", null, valores);
            banco.close();
        }
        return id;
    }

    public long update(Alugado alugado) {
        MeiosDeTransporteDAO edao = new MeiosDeTransporteDAO(this.mContext);
        long id = edao.update(alugado);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte.", Toast.LENGTH_LONG).show();
        }else{
            ContentValues valores = new ContentValues();
            valores.put("LOCADORA", alugado.getLocadora());
            valores.put("MARCA", alugado.getMarca());
            valores.put("MODELO", alugado.getModelo());
            valores.put("COR", alugado.getCor());
            SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
            id = banco.update("ALUGADO",valores,"MEIODETRANSPORTE_ID = ?",new String[]{Integer.toString(alugado.getId())});
            if(id == -1L){
                Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte Alugado.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mContext, "Meio de Transporte Alugado Atualizado com Sucesso!.", Toast.LENGTH_LONG).show();
            }
            banco.close();
        }
        return id;
    }

    public Alugado readByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        MeioDeTransporte m = null;
        Alugado a = null;
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        m = mdao.readByID(id);
        if(m != null){
            cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
            if(cursor.moveToFirst()){
                a = new Alugado();
                a.setId(id);
                a.setTipo(m.getTipo());
                a.setDescricao(m.getDescricao());
                a.setLocadora(cursor.getString(1));
                a.setMarca(cursor.getString(2));
                a.setModelo(cursor.getString(3));
                a.setCor(cursor.getString(4));
            }
        }
        return a;
    }

    public List<Alugado> readAll() {
        List<Alugado> lista = new ArrayList<>();
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(queryAll, null);
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        if (cursor.moveToFirst()) {
            Alugado a = null;
            MeioDeTransporte m = null;
            do {
                m = mdao.readByID(cursor.getInt(0));
                a = new Alugado();
                a.setId(m.getId());
                a.setTipo(m.getTipo());
                a.setDescricao(m.getDescricao());
                a.setLocadora(cursor.getString(1));
                a.setMarca(cursor.getString(2));
                a.setModelo(cursor.getString(3));
                a.setCor(cursor.getString(4));
                lista.add(a);
            } while (cursor.moveToNext());
        }
        cursor.close();
        banco.close();
        db.close();
        return lista;
    }

    public long deleteByID(Alugado alugado){
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        long id = banco.delete("ALUGADO", "MEIODETRANSPORTE_ID = ?", new String[]{Integer.toString(alugado.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao remover meio de transporte alugado.", Toast.LENGTH_LONG).show();
        }
        mdao.deleteByID(alugado);
        banco.close();
        return id;
    }
}
