package inmotion.sistema.uninassau.pe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import inmotion.sistema.uninassau.pe.model.relatorios.EstatisticasGeral;
import inmotion.sistema.uninassau.pe.move.R;

public class exibirRelatorioGeralActivity extends AppCompatActivity {

    EstatisticasGeral item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_relatorio_geral);
        Intent intent = getIntent();
        item = (EstatisticasGeral) intent.getSerializableExtra("item");

        //Cabeçalho
        TextView relatorioGeralPeriodo = findViewById(R.id.relatorioGeralPeriodo);
        relatorioGeralPeriodo.setText(item.getDataInicialTxt() + " a " + item.getDataFinalTxt());

        //Bolinhas
        TextView est2linhasViagem3 = findViewById(R.id.est2linhasViagem3);
        est2linhasViagem3.setText(item.getQtdViagens() + " viagens\n" + String.format("%.2f", item.getTotalDistancia()) + " Km");
        TextView est2LinhasGastos5 = findViewById(R.id.est2LinhasGastos5);
        est2LinhasGastos5.setText(item.getQtdServicos() + " serviços\nR$ " + String.format("%.2f", item.getTotalGastos()));

        //Gráficos (texto)
        TextView particularPorcentV = findViewById(R.id.particularPorcentV);
        particularPorcentV.setText(String.format("%.1f", item.getProporcaoDistanciaPorCategoria().get("Particular")) + "%");
        if(particularPorcentV.getText().equals("0,0%")){
            particularPorcentV.setVisibility(View.INVISIBLE);
        }
        TextView alugadoPorcentV = findViewById(R.id.alugadoPorcentV);
        alugadoPorcentV.setText(String.format("%.1f", item.getProporcaoDistanciaPorCategoria().get("Alugado")) + "%");
        if(alugadoPorcentV.getText().equals("0,0%")){
            alugadoPorcentV.setVisibility(View.INVISIBLE);
        }
        TextView publicoPorcentV = findViewById(R.id.publicoPorcentV);
        publicoPorcentV.setText(String.format("%.1f", item.getProporcaoDistanciaPorCategoria().get("Público")) + "%");
        if(publicoPorcentV.getText().equals("0,0%")){
            publicoPorcentV.setVisibility(View.INVISIBLE);
        }
        TextView compartilhadoPorcentV = findViewById(R.id.compartilhadoPorcentV);
        compartilhadoPorcentV.setText(String.format("%.1f", item.getProporcaoDistanciaPorCategoria().get("Compartilhado")) + "%");
        if(compartilhadoPorcentV.getText().equals("0,0%")){
            compartilhadoPorcentV.setVisibility(View.INVISIBLE);
        }
        TextView particularPorcentG = findViewById(R.id.particularPorcentG);
        particularPorcentG.setText(String.format("%.1f", item.getProporcaoGastosPorCategoria().get("Particular")) + "%");
        if(particularPorcentG.getText().equals("0,0%")){
            particularPorcentG.setVisibility(View.INVISIBLE);
        }
        TextView alugadoPorcentoG = findViewById(R.id.alugadoPorcentoG);
        alugadoPorcentoG.setText(String.format("%.1f", item.getProporcaoGastosPorCategoria().get("Alugado")) + "%");
        if(alugadoPorcentoG.getText().equals("0,0%")){
            alugadoPorcentoG.setVisibility(View.INVISIBLE);
        }
        TextView publicoPorcentG = findViewById(R.id.publicoPorcentG);
        publicoPorcentG.setText(String.format("%.1f", item.getProporcaoGastosPorCategoria().get("Público")) + "%");
        if(publicoPorcentG.getText().equals("0,0%")){
            publicoPorcentG.setVisibility(View.INVISIBLE);
        }
        TextView compartilhadoPorcentoG = findViewById(R.id.compartilhadoPorcentoG);
        compartilhadoPorcentoG.setText(String.format("%.1f", item.getProporcaoGastosPorCategoria().get("Compartilhado")) + "%");
        if(compartilhadoPorcentoG.getText().equals("0,0%")){
            compartilhadoPorcentoG.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Gráficos (barras)
        final View v = findViewById(R.id.relatorioGeralConstraintLayout);
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ImageView graphViagensParticular = findViewById(R.id.graphViagensParticular);
                graphViagensParticular.requestLayout();
                graphViagensParticular.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoDistanciaPorCategoria().get("Particular"));
                ImageView graphGastosParticular = findViewById(R.id.graphGastosParticular);
                graphGastosParticular.requestLayout();
                graphGastosParticular.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoGastosPorCategoria().get("Particular"));

                ImageView graphViagensAlugado = findViewById(R.id.graphViagensAlugado);
                graphViagensAlugado.requestLayout();
                graphViagensAlugado.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoDistanciaPorCategoria().get("Alugado"));
                ImageView graphGastosAlugado = findViewById(R.id.graphGastosAlugado);
                graphGastosAlugado.requestLayout();
                graphGastosAlugado.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoGastosPorCategoria().get("Alugado"));

                ImageView graphViagensPublico = findViewById(R.id.graphViagensPublico);
                graphViagensPublico.requestLayout();
                graphViagensPublico.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoDistanciaPorCategoria().get("Público"));
                ImageView graphGastosPublico = findViewById(R.id.graphGastosPublico);
                graphGastosPublico.requestLayout();
                graphGastosPublico.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoGastosPorCategoria().get("Público"));

                ImageView graphViagensCompartilhado = findViewById(R.id.graphViagensCompartilhado);
                graphViagensCompartilhado.requestLayout();
                graphViagensCompartilhado.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoDistanciaPorCategoria().get("Compartilhado"));
                ImageView graphGastosCompartilhado = findViewById(R.id.graphGastosCompartilhado);
                graphGastosCompartilhado.requestLayout();
                graphGastosCompartilhado.getLayoutParams().width = (int) ((v.getWidth() - 32) / 100 * item.getProporcaoGastosPorCategoria().get("Compartilhado"));
            }
        });

    }
}
