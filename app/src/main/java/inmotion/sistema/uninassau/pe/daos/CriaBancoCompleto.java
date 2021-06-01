package inmotion.sistema.uninassau.pe.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CriaBancoCompleto extends SQLiteOpenHelper {

    public static volatile CriaBancoCompleto instancia;

    public static final String NOME_BANCO      = "move.db";
    public static final int VERSAO_BANCO       = 3;

    private static StringBuilder createQuery = null;

    private Context mContext;

    public CriaBancoCompleto(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
        this.mContext = context;
        if(CriaBancoCompleto.createQuery == null){
            try{
                BufferedReader q = new BufferedReader(new InputStreamReader( this.mContext.getAssets().open("CreateBanco.sql") ) );
                String entrada;
                createQuery = new StringBuilder();
                while( (entrada = q.readLine()) != null){
                    createQuery.append(entrada);
                }
            }catch(IOException ex){
                Toast.makeText(mContext, "Falha ao criar banco de dados da aplicação.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static CriaBancoCompleto getInstance(Context context) {
        if (instancia == null) {
            synchronized (CriaBancoCompleto.class) {
                if (instancia == null) {
                    instancia = new CriaBancoCompleto(context);
                }
            }
        }
        return instancia;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //criação das tabelas
        try{
            String aux = createQuery.toString();
            for (String query : aux.split(";")) {
                db.execSQL(query);
            }
        }catch (SQLiteException sqle){
            System.out.println(sqle.getLocalizedMessage());
            System.out.println(sqle.getMessage());
            throw sqle;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nada a fazer para a apresentação
    }
}