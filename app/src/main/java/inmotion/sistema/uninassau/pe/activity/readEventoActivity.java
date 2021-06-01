package inmotion.sistema.uninassau.pe.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import inmotion.sistema.uninassau.pe.daos.eventos.EventoDAO;
import inmotion.sistema.uninassau.pe.daos.eventos.GastoDAO;
import inmotion.sistema.uninassau.pe.daos.eventos.ViagemDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.MeiosDeTransporteDAO;
import inmotion.sistema.uninassau.pe.model.eventos.Evento;
import inmotion.sistema.uninassau.pe.model.eventos.Gasto;
import inmotion.sistema.uninassau.pe.model.eventos.Viagem;
import inmotion.sistema.uninassau.pe.move.R;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class readEventoActivity extends AppCompatActivity {

    EventoDAO dao;
    GastoDAO gdao;
    ViagemDAO vdao;
    MeiosDeTransporteDAO meiosDeTransporteDAO;
    Evento item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_evento);
        dao = new EventoDAO(getApplicationContext());
        gdao = new GastoDAO(getApplicationContext());
        vdao = new ViagemDAO(getApplicationContext());
        meiosDeTransporteDAO = new MeiosDeTransporteDAO(getApplicationContext());

        Intent intent = getIntent();
        item = (Evento) intent.getSerializableExtra("item2");

        ImageView avatar = (ImageView) findViewById(R.id.readAvatar2);
        TextView avatar_text = (TextView) findViewById(R.id.readAvatar_text2);
        TextView linha1 = (TextView) findViewById(R.id.readEventoTipo);
        TextView linha2 = (TextView) findViewById(R.id.readEventoMeioTransporte);
        TextView data = (TextView) findViewById(R.id.readEventoData);
        TextView labelCampo1 = (TextView) findViewById(R.id.readEventoCampo1Label);
        TextView campo1 = (TextView) findViewById(R.id.readEventoCampo1);
        TextView labelCampo2 = (TextView) findViewById(R.id.readEventoCampo2Label);
        TextView campo2 = (TextView) findViewById(R.id.readEventoCampo2);
        TextView labelCampo3 = (TextView) findViewById(R.id.readEventoCampo3Label);
        TextView campo3 = (TextView) findViewById(R.id.readEventoCampo3);

        if (item instanceof Viagem) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                avatar.setBackgroundTintList(getColorStateList(R.color.viagem));
            } else {
                avatar.setBackgroundTintList(ColorStateList.valueOf(GREEN));
            }
            avatar_text.setText("V");

            Viagem viagem = (Viagem) item;
            data.setText(viagem.getData());

            linha1.setText("Viagem");
            campo1.setText(viagem.getDistancia() + " Km");
            campo2.setText(viagem.getInicio());
            campo3.setText(viagem.getFim());

            if (meiosDeTransporteDAO.readByID(viagem.getMeiodetransporte_id()) != null) {
                linha2.setText(meiosDeTransporteDAO.readByID(viagem.getMeiodetransporte_id()).toString());
            }


        } else if (item instanceof Gasto) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                avatar.setBackgroundTintList(getColorStateList(R.color.despesa));
            } else {
                avatar.setBackgroundTintList(ColorStateList.valueOf(RED));
            }
            avatar_text.setText("$");

            Gasto gasto = (Gasto) item;
            data.setText(gasto.getData());

            linha1.setText("Gasto");
            labelCampo1.setText("R$");
            campo1.setText(gasto.getValor() + "");
            labelCampo2.setText("Categoria");
            campo2.setText(gasto.getTipo());
            labelCampo3.setText("Descrição");
            campo3.setText(gasto.getObservacao());

            if (meiosDeTransporteDAO.readByID(gasto.getMeiodetransporte_id()) != null) {
                linha2.setText(meiosDeTransporteDAO.readByID(gasto.getMeiodetransporte_id()).toString());
            }

        }

    }

    public void editar(View view) {
        Intent intent;
        if (item instanceof Viagem) {
            intent = new Intent(this, addEventoViagemActivity.class);
        } else {
            //Gasto
            intent = new Intent(this, addEventoDespesaActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("data", item.getData());
        intent.putExtra("item", item);
        startActivity(intent);
        finish();

    }

    public void excluir(View view) {
        long result = 0;
        if(item instanceof Viagem){
            result = vdao.deleteByID((Viagem)item);
        }else{
            result = gdao.deleteByID((Gasto)item);
        }
        if(result != -1){
            Toast.makeText(this, "Registro removido com sucesso!", Toast.LENGTH_SHORT).show();
            setResult(getResources().getInteger(R.integer.SUCESS));
        }else{
            Toast.makeText(this, "Falha ao remover registro", Toast.LENGTH_SHORT).show();
            setResult(getResources().getInteger(R.integer.NO_SUCESS));
        }
        finish();
    }

}
