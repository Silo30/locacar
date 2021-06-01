package inmotion.sistema.uninassau.pe.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.MeiosDeTransporteDAO;
import inmotion.sistema.uninassau.pe.model.eventos.Evento;
import inmotion.sistema.uninassau.pe.model.eventos.Gasto;
import inmotion.sistema.uninassau.pe.model.eventos.Viagem;
import inmotion.sistema.uninassau.pe.model.relatorios.EstatisticasGeral;
import inmotion.sistema.uninassau.pe.model.relatorios.EstatisticasPorMeioDeTransporte;

public class RelatoriosDAO {

    private CriaBancoCompleto db;
    private Context mContext;
    private static SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
    public RelatoriosDAO(Context context) {
        this.db = new CriaBancoCompleto(context);
        this.mContext = context;
    }

    public EstatisticasPorMeioDeTransporte relatorioIndividual(int meioDeTransporte,
                                                               Timestamp dataInicial, Timestamp datafinal) {
        EstatisticasPorMeioDeTransporte stats = new EstatisticasPorMeioDeTransporte();
        //Carrega todas as viagens dentro do intervalo
        StringBuilder viagensQuery = new StringBuilder();
        try{
            BufferedReader q = new BufferedReader(new InputStreamReader( this.mContext.getAssets().open("RelatAllViagens.sql") ) );
            String entrada;
            while( (entrada = q.readLine()) != null){
                viagensQuery.append(entrada);
            }
        }catch(IOException ex){
            Toast.makeText(mContext, "Falha ao ler consulta de viagens.", Toast.LENGTH_LONG).show();
        }
        //Carrega todos os gastos dentro do intervalo
        StringBuilder gastosQuery = new StringBuilder();
        try{
            BufferedReader q = new BufferedReader(new InputStreamReader( this.mContext.getAssets().open("RelatAllGastos.sql") ) );
            String entrada;
            while( (entrada = q.readLine()) != null){
                gastosQuery.append(entrada);
            }
        }catch(IOException ex){
            Toast.makeText(mContext, "Falha ao ler consulta de gastos.", Toast.LENGTH_LONG).show();
        }
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        cursor = banco.rawQuery(viagensQuery.toString(), new String[]{Integer.toString(meioDeTransporte)
                ,out.format(new Date(dataInicial.getTime())),out.format(new Date(datafinal.getTime()))});
        ArrayList<Evento> eventos = new ArrayList<>(20);
        if(cursor.moveToFirst()){
            do{
                Viagem v = new Viagem();
                v.setId(cursor.getInt(0));
                v.setMeiodetransporte_id(meioDeTransporte);
                v.setData(cursor.getString(1));
                v.setInicio(cursor.getString(2));
                v.setFim(cursor.getString(3));
                v.setDistancia(cursor.getFloat(4));
                eventos.add(v);
            }while(cursor.moveToNext());
        }
        cursor = banco.rawQuery(gastosQuery.toString(), new String[]{Integer.toString(meioDeTransporte)
                ,out.format(new Date(dataInicial.getTime())),out.format(new Date(datafinal.getTime()))});
        if(cursor.moveToFirst()){
            do{
                Gasto v = new Gasto();
                v.setId(cursor.getInt(0));
                v.setMeiodetransporte_id(meioDeTransporte);
                v.setData(cursor.getString(1));
                v.setTipo(cursor.getString(2));
                v.setValor(cursor.getFloat(3));
                v.setObservacao(cursor.getString(4));
                eventos.add(v);
            }while(cursor.moveToNext());
        }
        HashMap<String,Float> listaDeGastos = new HashMap<>(eventos.size());
        //Prepara Médias, qtds e totais
        float combustivel = 0;
        for(Evento e : eventos){
            if(e instanceof  Viagem){
                Viagem v = (Viagem) e;
                stats.setQtdViagens(stats.getQtdViagens()+1);
                stats.setTotalDistancia(stats.getTotalDistancia()+v.getDistancia());
            }else{
                Gasto g = (Gasto) e;
                stats.setQtdServicos(stats.getQtdServicos()+1);
                stats.setTotalGastos(stats.getTotalGastos()+ g.getValor());
                if(g.getTipo().equals("Combustível")){
                    combustivel+= g.getValor();
                }
                //Prepara lista de gastos;
                if(g.getValor() != 0){
                    if(listaDeGastos.containsKey(g.getTipo())){
                        listaDeGastos.put(g.getTipo(),listaDeGastos.get(g.getTipo())+g.getValor());
                    }else{
                        listaDeGastos.put(g.getTipo(), g.getValor());
                    }
                }
            }
        }
        //Prepara Proporções
        String consultaTodosViagens = "SELECT SUM(DISTANCIA) FROM VIAGEM WHERE EVENTO_ID <> ?";
        String consultaTodosGastos = "SELECT SUM(VALOR) FROM GASTO WHERE EVENTO_ID <> ?";
        float totalViagens = 0;
        float totalGastos = 0;
        cursor = banco.rawQuery(consultaTodosViagens,new String[]{Integer.toString(meioDeTransporte)});
        if(cursor.moveToFirst()){
            totalViagens = cursor.getFloat(0);
        }
        cursor = banco.rawQuery(consultaTodosGastos,new String[]{Integer.toString(meioDeTransporte)});
        if(cursor.moveToFirst()){
            totalGastos = cursor.getFloat(0);
        }
        //Seta Médias
        stats.setMediaCombustivelPorKm(combustivel/stats.getTotalDistancia());
        stats.setMediaGastosPorKm(stats.getTotalGastos()/stats.getTotalDistancia());
        //Seta Proporções
        stats.setProporcaoGastos(stats.getTotalGastos()*100.0F/totalGastos);
        stats.setProporcaoViagens(stats.getTotalDistancia()*100.0F/totalViagens);
        stats.setDataFinal(datafinal);
        stats.setDataInicial(dataInicial);
        stats.setListaDeGastos(listaDeGastos);
        MeiosDeTransporteDAO mdao = new MeiosDeTransporteDAO(this.mContext);
        stats.setMeioDeTransporte(mdao.readSpecificCategoryByID(meioDeTransporte));
        return stats;
    }

    public EstatisticasGeral relatorioGeral(Timestamp dataInicial, Timestamp datafinal) {
        EstatisticasGeral stats = new EstatisticasGeral();
        HashMap<String, Float> totalDistanciaPorCategoria, totalGastosPorCategoria;
        totalDistanciaPorCategoria = new HashMap<>(4);
        totalGastosPorCategoria = new HashMap<>(4);
        //Carrega todas as viagens dentro do intervalo
        StringBuilder viagensQuery = new StringBuilder();
        try{
            BufferedReader q = new BufferedReader(new InputStreamReader( this.mContext.getAssets().open("RelatGeralViagens.sql") ) );
            String entrada;
            while( (entrada = q.readLine()) != null){
                viagensQuery.append(entrada);
            }
        }catch(IOException ex){
            Toast.makeText(mContext, "Falha ao ler consulta de viagens.", Toast.LENGTH_LONG).show();
        }
        //Carrega todos os gastos dentro do intervalo
        StringBuilder gastosQuery = new StringBuilder();
        try{
            BufferedReader q = new BufferedReader(new InputStreamReader( this.mContext.getAssets().open("RelatGeralGastos.sql") ) );
            String entrada;
            while( (entrada = q.readLine()) != null){
                gastosQuery.append(entrada);
            }
        }catch(IOException ex){
            Toast.makeText(mContext, "Falha ao ler consulta de gastos.", Toast.LENGTH_LONG).show();
        }
        SQLiteDatabase banco = db.getInstance(mContext).getReadableDatabase();
        Cursor cursor;
        cursor = banco.rawQuery(viagensQuery.toString(), new String[]{out.format(new Date(dataInicial.getTime())),out.format(new Date(datafinal.getTime()))});
        ArrayList<Evento> eventos = new ArrayList<>(20);
        if(cursor.moveToFirst()){
            do{
                Viagem v = new Viagem();
                v.setId(cursor.getInt(0));
                v.setMeiodetransporte_id(cursor.getInt(5));
                v.setData(cursor.getString(1));
                v.setInicio(cursor.getString(2));
                v.setFim(cursor.getString(3));
                v.setDistancia(cursor.getFloat(4));
                stats.setTotalDistancia(stats.getTotalDistancia()+v.getDistancia());
                stats.setQtdViagens(stats.getQtdViagens()+1);
                if(totalDistanciaPorCategoria.containsKey(cursor.getString(6))){
                    totalDistanciaPorCategoria.put(cursor.getString(6),totalDistanciaPorCategoria.get(cursor.getString(6))+cursor.getFloat(4));
                }else{
                    totalDistanciaPorCategoria.put(cursor.getString(6),v.getDistancia());
                }
                eventos.add(v);
            }while(cursor.moveToNext());
        }
        cursor = banco.rawQuery(gastosQuery.toString(), new String[]{out.format(new Date(dataInicial.getTime())),out.format(new Date(datafinal.getTime()))});
        if(cursor.moveToFirst()){
            do{
                Gasto v = new Gasto();
                v.setId(cursor.getInt(0));
                v.setMeiodetransporte_id(cursor.getInt(5));
                v.setData(cursor.getString(1));
                v.setTipo(cursor.getString(2));
                v.setValor(cursor.getFloat(3));
                stats.setTotalGastos(stats.getTotalGastos()+v.getValor());
                stats.setQtdServicos(stats.getQtdServicos()+1);
                v.setObservacao(cursor.getString(4));
                if(totalGastosPorCategoria.containsKey(cursor.getString(6))){
                    totalGastosPorCategoria.put(cursor.getString(6),totalGastosPorCategoria.get(cursor.getString(6))+v.getValor());
                }else{
                    totalGastosPorCategoria.put(cursor.getString(6),v.getValor());
                }
                eventos.add(v);
            }while(cursor.moveToNext());
        }
        HashMap<String,Float> listaDeGastos = new HashMap<>(eventos.size());
        //Prepara Proporções
        HashMap<String, Float> proporcaoDistanciaPorCategoria, proporcaoGastosPorCategoria;
        proporcaoDistanciaPorCategoria = new HashMap<>(4);
        proporcaoGastosPorCategoria = new HashMap<>(4);
        if(totalDistanciaPorCategoria.containsKey("Particular")){ 
            proporcaoDistanciaPorCategoria.put("Particular", totalDistanciaPorCategoria.get("Particular")*100F / stats.getTotalDistancia());
        }else{
            proporcaoDistanciaPorCategoria.put("Particular", 0F);
        }
        if(totalDistanciaPorCategoria.containsKey("Público")){
            proporcaoDistanciaPorCategoria.put("Público", totalDistanciaPorCategoria.get("Público")*100F / stats.getTotalDistancia());
        }else{
            proporcaoDistanciaPorCategoria.put("Público", 0F);
        }
        if(totalDistanciaPorCategoria.containsKey("Compartilhado")){
            proporcaoDistanciaPorCategoria.put("Compartilhado", totalDistanciaPorCategoria.get("Compartilhado")*100F / stats.getTotalDistancia());
        }else{
            proporcaoDistanciaPorCategoria.put("Compartilhado", 0F);
        }
        if(totalDistanciaPorCategoria.containsKey("Alugado")){
            proporcaoDistanciaPorCategoria.put("Alugado", totalDistanciaPorCategoria.get("Alugado")*100F / stats.getTotalDistancia());
        }else{
            proporcaoDistanciaPorCategoria.put("Alugado", 0F);
        }
        if(totalGastosPorCategoria.containsKey("Particular")){
            proporcaoGastosPorCategoria.put("Particular", totalGastosPorCategoria.get("Particular")*100F / stats.getTotalGastos());
        }else{
            proporcaoGastosPorCategoria.put("Particular", 0F);
        }
        if(totalGastosPorCategoria.containsKey("Público")){
            proporcaoGastosPorCategoria.put("Público", totalGastosPorCategoria.get("Público")*100F / stats.getTotalGastos());
        }else{
            proporcaoGastosPorCategoria.put("Público", 0F);
        }
        if(totalGastosPorCategoria.containsKey("Compartilhado")){
            proporcaoGastosPorCategoria.put("Compartilhado", totalGastosPorCategoria.get("Compartilhado")*100F / stats.getTotalGastos());
        }else{
            proporcaoGastosPorCategoria.put("Compartilhado", 0F);
        }
        if(totalGastosPorCategoria.containsKey("Alugado")){
            proporcaoGastosPorCategoria.put("Alugado", totalGastosPorCategoria.get("Alugado")*100F / stats.getTotalGastos());
        }else{
            proporcaoGastosPorCategoria.put("Alugado", 0F);
        }
        //Seta Proporções
        stats.setTotalDistanciaPorCategoria(totalDistanciaPorCategoria);
        stats.setTotalGastosPorCategoria(totalGastosPorCategoria);
        stats.setProporcaoDistanciaPorCategoria(proporcaoDistanciaPorCategoria);
        stats.setProporcaoGastosPorCategoria(proporcaoGastosPorCategoria);
        stats.setDataFinal(datafinal);
        stats.setDataInicial(dataInicial);
        return stats;
    }
}
