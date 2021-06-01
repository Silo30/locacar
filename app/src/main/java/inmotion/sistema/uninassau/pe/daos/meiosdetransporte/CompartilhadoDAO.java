package inmotion.sistema.uninassau.pe.daos.meiosdetransporte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inmotion.sistema.uninassau.pe.daos.CriaBancoCompleto;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Compartilhado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;

public class CompartilhadoDAO{
        private static final String queryByID   = "SELECT * FROM COMPARTILHADO WHERE MEIODETRANSPORTE_ID = ?";
        private static final String queryAll    = "SELECT * FROM COMPARTILHADO";

        private CriaBancoCompleto db;
        private Context mContext;

        public CompartilhadoDAO(Context context) {
            this.db = new CriaBancoCompleto(context);
            this.mContext = context;
        }

        public long insert(Compartilhado compartilhado) {
            SQLiteDatabase banco = null;
            MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(this.mContext);
            long id = mdao.insert(compartilhado);
            if (id == -1L) {
                Toast.makeText(mContext, "Falha ao inserir novo meio de transporte compartilhado.", Toast.LENGTH_LONG).show();
            }else{
                banco = db.getInstance(mContext).getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put("MEIODETRANSPORTE_ID", id);
                valores.put("EMPRESA", compartilhado.getEmpresa());
                banco.insert("COMPARTILHADO", null, valores);
                banco.close();
            }
            return id;
        }

        public long update(Compartilhado compartilhado) {
            MeiosDeTransporteDAO edao = new MeiosDeTransporteDAO(this.mContext);
            long id = edao.update(compartilhado);
            if (id == -1L) {
                Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte.", Toast.LENGTH_LONG).show();
            }else{
                ContentValues valores = new ContentValues();
                valores.put("EMPRESA", compartilhado.getEmpresa());
                SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
                id = banco.update("COMPARTILHADO",valores,"MEIODETRANSPORTE_ID = ?",new String[]{Integer.toString(compartilhado.getId())});
                if(id == -1L){
                    Toast.makeText(mContext, "Falha ao atualizar Meio de Transporte Compartilhado.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext, "Meio de Transporte Compartilhado Atualizado com Sucesso!.", Toast.LENGTH_LONG).show();
                }
                banco.close();
            }
            return id;
        }

        public Compartilhado readByID(int id){
            SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
            Cursor cursor;
            MeioDeTransporte m = null;
            Compartilhado c = null;
            MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
            m = mdao.readByID(id);
            if(m != null){
                cursor = banco.rawQuery(queryByID, new String[]{Integer.toString(id)});
                if(cursor.moveToFirst()){
                    c = new Compartilhado();
                    c.setId(id);
                    c.setTipo(m.getTipo());
                    c.setDescricao(m.getDescricao());
                    c.setEmpresa(cursor.getString(1));
                }
            }
            return c;
        }

        public List<Compartilhado> readAll() {
            List<Compartilhado> lista = new ArrayList<>();
            SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
            Cursor cursor = banco.rawQuery(queryAll, null);
            MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
            if (cursor.moveToFirst()) {
                Compartilhado c = null;
                MeioDeTransporte m = null;
                do {
                    m = mdao.readByID(cursor.getInt(0));
                    c = new Compartilhado();
                    c.setId(m.getId());
                    c.setTipo(m.getTipo());
                    c.setDescricao(m.getDescricao());
                    c.setEmpresa(cursor.getString(1));
                    lista.add(c);
                } while (cursor.moveToNext());
            }
            cursor.close();
            banco.close();
            db.close();
            return lista;
        }

        public long deleteByID(Compartilhado compartilhado){
            SQLiteDatabase banco = db.getInstance(mContext).getWritableDatabase();
            MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(mContext);
            long id = banco.delete("COMPARTILHADO", "MEIODETRANSPORTE_ID = ?", new String[]{Integer.toString(compartilhado.getId())});
            if (id == -1L) {
                Toast.makeText(mContext, "Falha ao remover meio de transporte compartilhado.", Toast.LENGTH_LONG).show();
            }
            mdao.deleteByID(compartilhado);
            banco.close();
            return id;
        }
}
