package inmotion.sistema.uninassau.pe.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Alugado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Compartilhado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Particular;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Publico;
import inmotion.sistema.uninassau.pe.model.relatorios.EstatisticasPorMeioDeTransporte;
import inmotion.sistema.uninassau.pe.move.R;

import static android.graphics.Color.CYAN;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class exibirRelatorioIndividualActivity extends AppCompatActivity {

    EstatisticasPorMeioDeTransporte item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_relatorio_individual);
        Intent intent = getIntent();
        item = (EstatisticasPorMeioDeTransporte) intent.getSerializableExtra("item");

        //Cabeçalho
        ImageView avatar = (ImageView) findViewById(R.id.estatisticasAvatar);
        TextView relatorioIndividualPeriodo = findViewById(R.id.relatorioIndividualPeriodo);
        relatorioIndividualPeriodo.setText(item.getDataInicialTxt() + " a " + item.getDataFinalTxt());
        TextView estatIndivVeicNome = findViewById(R.id.estatIndivVeicNome);
        estatIndivVeicNome.setText(item.getMeioDeTransporte().getDescricao());
        TextView estatIndivVeicCategoria = findViewById(R.id.estatIndivVeicCategoria);
        if(item.getMeioDeTransporte() instanceof Alugado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    avatar.setBackgroundTintList(getColorStateList(R.color.alugado));
                } else {
                    avatar.setBackgroundTintList(ColorStateList.valueOf(GREEN));
                }
                estatIndivVeicCategoria.setText("Alugado");
        }else if(item.getMeioDeTransporte() instanceof Particular){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    avatar.setBackgroundTintList(getColorStateList(R.color.particular));
                } else {
                    avatar.setBackgroundTintList(ColorStateList.valueOf(YELLOW));
                }
                estatIndivVeicCategoria.setText("Particular");
        }else if(item.getMeioDeTransporte() instanceof Publico){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    avatar.setBackgroundTintList(getColorStateList(R.color.publico));
                } else {
                    avatar.setBackgroundTintList(ColorStateList.valueOf(CYAN));
                }
                estatIndivVeicCategoria.setText("Público");
        }else if(item.getMeioDeTransporte() instanceof Compartilhado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    avatar.setBackgroundTintList(getColorStateList(R.color.compartilhado));
                } else {
                    avatar.setBackgroundTintList(ColorStateList.valueOf(RED));
                }
                estatIndivVeicCategoria.setText("Compartilhado");
        }


        //Bolinhas
        TextView est2linhasViagem = findViewById(R.id.est2linhasViagem);
        est2linhasViagem.setText(item.getQtdViagens() + " viagens\n" + String.format("%.2f", item.getTotalDistancia()) + " Km");
        TextView est2LinhasGastos = findViewById(R.id.est2LinhasGastos);
        est2LinhasGastos.setText(item.getQtdServicos() + " serviços\nR$ " + String.format("%.2f", item.getTotalGastos()));
        TextView est2LinhasGasolina = findViewById(R.id.est2LinhasGasolina);
        est2LinhasGasolina.setText("Combustível\nR$ " + String.format("%.2f", item.getMediaCombustivelPorKm()) + " / Km");
        TextView est2LinhasGastos2 = findViewById(R.id.est2LinhasGastos2);
        est2LinhasGastos2.setText("Média de gastos\nR$ " + String.format("%.2f", item.getMediaGastosPorKm()) + " / Km");

        //Gráficos (textos)
        TextView textoGraphViagens = findViewById(R.id.textoGraphViagens);
        textoGraphViagens.setText(String.format("%.2f", item.getProporcaoViagens()) + "% do geral.");
        TextView textoGraphDespesas = findViewById(R.id.textoGraphDespesas);
        textoGraphDespesas.setText(String.format("%.2f", item.getProporcaoGastos()) + "% do geral.");


        //Lista de despesas
        TextView listaDeGastos = findViewById(R.id.listaDeGastos);
        listaDeGastos.setText(item.getListaDeGastosSet().toString().substring(1, item.getListaDeGastosSet().toString().length() - 1).replace(", ", "\n").replace("=", " = R$ ") + "\n");
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Gráficos (barras)
        final View v = findViewById(R.id.relatorioConstraintLayout);
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ImageView graphViagens = findViewById(R.id.graphViagens);
                graphViagens.requestLayout();
                graphViagens.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoViagens());
                ImageView graphGastos = findViewById(R.id.graphGastos);
                graphGastos.requestLayout();
                graphGastos.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoGastos());
            }
        });

    }
}
