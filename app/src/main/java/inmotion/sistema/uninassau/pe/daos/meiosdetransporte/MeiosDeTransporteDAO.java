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


public class MeiosDeTransporteDAO {

    private static final String queryByID   = "SELECT * FROM MEIODETRANSPORTE WHERE ID=?";
    private static final String queryAll    = "SELECT * FROM MEIODETRANSPORTE";

    private CriaBancoCompleto db;
    private Context mContext;

    public MeiosDeTransporteDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }

    public long insert(MeioDeTransporte meio) {
        ContentValues valores = new ContentValues();
        valores.put("DESCRICAO", meio.getDescricao());
        valores.put("TIPO", meio.getTipo());

        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        long id = banco.insert("MEIODETRANSPORTE", null, valores);

        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao inserir novo meio de transporte.", Toast.LENGTH_LONG).show();
        }
        banco.close();
        return id;
    }

    public long update(MeioDeTransporte meio) {
        ContentValues valores = new ContentValues();
        valores.put("DESCRICAO", meio.getDescricao());
        valores.put("TIPO", meio.getTipo());
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        long id = banco.update("MEIODETRANSPORTE",valores,"ID = ?",new String[]{Integer.toString(meio.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao Atualizar Meio de Transporte.", Toast.LENGTH_LONG).show();
        }
        banco.close();
        return id;
    }

    public int findIDByDescricao(String descricao) {
        int id;
        String busca = "SELECT id FROM MeioDeTransporte WHERE descricao LIKE '%" + descricao + "%'";
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(busca, null);
        cursor.moveToFirst();
        id = cursor.getInt(0);
        cursor.close();
        banco.close();
        return id;
    }

    public MeioDeTransporte readByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        MeioDeTransporte m = null;
        cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
        if(cursor.moveToFirst()){
            m = new MeioDeTransporte(cursor.getInt(0), cursor.getString(1));
            m.setTipo(cursor.getString(2));
        }
        return m;
    }

    public MeioDeTransporte readSpecificCategoryByID(int id){
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        MeioDeTransporte m = null;
        String queryAl = "SELECT 'ALUGADO' AS CATEGORIA FROM MEIODETRANSPORTE M INNER JOIN ALUGADO A ON A.MEIODETRANSPORTE_ID = M.ID WHERE M.ID = ?";
        String queryPub = "SELECT 'PUBLICO' AS CATEGORIA FROM MEIODETRANSPORTE M INNER JOIN PUBLICO A ON A.MEIODETRANSPORTE_ID = M.ID WHERE M.ID = ?";
        String queryPar = "SELECT 'PARTICULAR' AS CATEGORIA FROM MEIODETRANSPORTE M INNER JOIN PARTICULAR A ON A.MEIODETRANSPORTE_ID = M.ID WHERE M.ID = ?";
        String queryComp = "SELECT 'COMPARTILHADO' AS CATEGORIA FROM MEIODETRANSPORTE M INNER JOIN COMPARTILHADO A ON A.MEIODETRANSPORTE_ID = M.ID WHERE M.ID = ?";
        String[] param = new String[]{Integer.toString(id)};
        cursor = banco.rawQuery(queryAl, param);
        if(cursor.moveToFirst() && "ALUGADO".equals(cursor.getString(0))){
            AlugadoDAO aldao = new AlugadoDAO(mContext);
            return aldao.readByID(id);
        }
        cursor = banco.rawQuery(queryPub, param);
        if(cursor.moveToFirst() && "PUBLICO".equals(cursor.getString(0))){
            PublicoDAO pudao = new PublicoDAO(mContext);
            return pudao.readByID(id);
        }
        cursor = banco.rawQuery(queryPar, param);
        if(cursor.moveToFirst() && "PARTICULAR".equals(cursor.getString(0))){
            ParticularDAO pardao = new ParticularDAO(mContext);
            return pardao.readByID(id);
        }
        cursor = banco.rawQuery(queryComp, param);
        if(cursor.moveToFirst() && "COMPARTILHADO".equals(cursor.getString(0))){
            CompartilhadoDAO cdao = new CompartilhadoDAO(mContext);
            return cdao.readByID(id);
        }
        return m;
    }

    public List<MeioDeTransporte> readAll() {
        List<MeioDeTransporte> lista = new ArrayList<MeioDeTransporte>();
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor = banco.rawQuery(queryAll, null);
        if (cursor.moveToFirst()) {
            MeioDeTransporte m = null;
            do {
                m = new MeioDeTransporte(cursor.getInt(0), cursor.getString(1));
                m.setTipo(cursor.getString(2));
                lista.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        banco.close();
        db.close();
        return lista;
    }

    public List<MeioDeTransporte> readAllSpecific(){
        List<MeioDeTransporte> lista = new ArrayList<MeioDeTransporte>();
        ParticularDAO padao = new ParticularDAO(mContext);
        CompartilhadoDAO cdao = new CompartilhadoDAO(mContext);
        AlugadoDAO adao = new AlugadoDAO(mContext);
        PublicoDAO pdao = new PublicoDAO(mContext);
        lista.addAll(padao.readAll());
        lista.addAll(cdao.readAll());
        lista.addAll(adao.readAll());
        lista.addAll(pdao.readAll());
        return lista;
    }

    public long deleteByID(MeioDeTransporte meio){
        SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
        long id = banco.delete("MEIODETRANSPORTE", "ID = ?", new String[]{Integer.toString(meio.getId())});
        if (id == -1L) {
            Toast.makeText(mContext, "Falha ao remover meio de transporte.", Toast.LENGTH_LONG).show();
        }
        banco.close();
        return id;
    }
}