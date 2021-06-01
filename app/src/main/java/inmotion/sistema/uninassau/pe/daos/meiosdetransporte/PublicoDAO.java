package inmotion.sistema.uninassau.pe.daos.meiosdetransporte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inmotion.sistema.uninassau.pe.daos.CriaBancoCompleto;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Publico;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;

public class PublicoDAO {
    private static final String queryByID   = "SELECT * FROM PUBLICO WHERE MEIODETRANSPORTE_ID = ?";
    private static final String queryAll    = "SELECT * FROM PUBLICO";

    private CriaBancoCompleto db;
    private Context mContext;

    public PublicoDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }

    public long insert(Publico publico) {
        SQLiteDatabase banco = null;
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(this.mContext);
        long id = mdao.insert(publico);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao inserir novo meio de transporte público.", Toast.LENGTH_LONG).show();
        }else{
            banco = db.getInstance(mContext).getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("MEIODETRANSPORTE_ID", id);
            valores.put("EMPRESA", publico.getEmpresa());
            banco.insert("PUBLICO", null, valores);
            banco.close();
        }
        return id;
    }

    public long update(Publico publico) {
        MeiosDeTransporteDAO edao = new MeiosDeTransporteDAO(this.mContext);
        long id = edao.update(publico);
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte.", Toast.LENGTH_LONG).show();
        }else{
            ContentValues valores = new ContentValues();
            valores.put("EMPRESA", publico.getEmpresa());
            SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
            id = banco.update("PUBLICO",valores,"MEIODETRANSPORTE_ID = ?",new String[]{Integer.toString(publico.getId())});
            if(id == -1L){
                Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte Público.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mContext, "Meio de Transporte Público Atualizado com Sucesso!.", Toast.LENGTH_LONG).show();
            }
            banco.close();
        }
        return id;
    }

    public Publico readByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        MeioDeTransporte m = null;
        Publico p = null;
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        m = mdao.readByID(id);
        if(m != null){
            cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
            if(cursor.moveToFirst()){
                p = new Publico();
                p.setId(id);
                p.setTipo(m.getTipo());
                p.setDescricao(m.getDescricao());
                p.setEmpresa(cursor.getString(1));
            }
        }
        return p;
    }

    public List<Publico> readAll() {
        List<Publico> lista = new ArrayList<>();
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(queryAll, null);
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        if (cursor.moveToFirst()) {
            Publico p = null;
            MeioDeTransporte m = null;
            do {
                m = mdao.readByID(cursor.getInt(0));
                p = new Publico();
                p.setId(m.getId());
                p.setTipo(m.getTipo());
                p.setDescricao(m.getDescricao());
                p.setEmpresa(cursor.getString(1));
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        banco.close();
        db.close();
        return lista;
    }

    public long deleteByID(Publico publico){
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
        long id = banco.delete("PUBLICO", "MEIODETRANSPORTE_ID = ?", new String[]{Integer.toString(publico.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao remover meio de transporte publico.", Toast.LENGTH_LONG).show();
        }
        mdao.deleteByID(publico);
        banco.close();
        return id;
    }
}
