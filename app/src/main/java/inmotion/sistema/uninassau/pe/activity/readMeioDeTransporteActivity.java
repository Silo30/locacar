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

import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.AlugadoDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.CompartilhadoDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.MeiosDeTransporteDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.ParticularDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.PublicoDAO;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Alugado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Compartilhado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Particular;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Publico;
import inmotion.sistema.uninassau.pe.move.R;

import static android.graphics.Color.CYAN;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class readMeioDeTransporteActivity extends AppCompatActivity {

    MeiosDeTransporteDAO dao;
    MeioDeTransporte item;
    AlugadoDAO aldao;
    CompartilhadoDAO cdao;
    ParticularDAO pardao;
    PublicoDAO pudao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_meio_de_transporte);
        dao = new MeiosDeTransporteDAO(getApplicationContext());
        aldao = new AlugadoDAO(getApplicationContext());
        cdao = new CompartilhadoDAO(getApplicationContext());
        pardao = new ParticularDAO(getApplicationContext());
        pudao = new PublicoDAO(getApplicationContext());
        Intent intent = getIntent();
        item = (MeioDeTransporte) intent.getSerializableExtra("item");

        ImageView avatar = (ImageView) findViewById(R.id.readAvatar);
        TextView avatar_text = (TextView) findViewById(R.id.readAvatar_text);
        TextView linha1 = (TextView) findViewById(R.id.readDescricao);
        TextView linha2 = (TextView) findViewById(R.id.readCategoria);
        TextView tipo = (TextView) findViewById(R.id.readTipo);
        TextView labelMarca = (TextView) findViewById(R.id.readMarcaLabel);
        TextView marca = (TextView) findViewById(R.id.readMarca);
        TextView labelModelo = (TextView) findViewById(R.id.readModeloLabel);
        TextView modelo = (TextView) findViewById(R.id.readModelo);
        TextView labelCor = (TextView) findViewById(R.id.readCorLabel);
        TextView cor = (TextView) findViewById(R.id.readCor);
        TextView labelLocEmpresa = (TextView) findViewById(R.id.readEmpresaLabel);
        TextView locEmpresa = (TextView) findViewById(R.id.readLocEmpresa);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.readTransporteLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        if (item.getDescricao() != null) {
            linha1.setText(item.getDescricao());
            if (item.getDescricao().length() > 0) {
                avatar_text.setText(item.getDescricao().substring(0, 1).toUpperCase());
            } else {
                avatar_text.setText("");
            }
        } else {
            linha1.setText("");
            avatar_text.setText("");
        }

        if (item instanceof Alugado) {
            linha2.setText("Alugado");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                avatar.setBackgroundTintList(getColorStateList(R.color.alugado));
            } else {
                avatar.setBackgroundTintList(ColorStateList.valueOf(GREEN));
            }
            Alugado alugado = (Alugado) item;
            tipo.setText(alugado.getTipo());
            marca.setText(alugado.getMarca());
            modelo.setText(alugado.getModelo());
            cor.setText(alugado.getCor());
            locEmpresa.setText(alugado.getLocadora());
            labelLocEmpresa.setText("Locadora");
        } else {
            if (item instanceof Publico) {
                linha2.setText("Público");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    avatar.setBackgroundTintList(getColorStateList(R.color.publico));
                } else {
                    avatar.setBackgroundTintList(ColorStateList.valueOf(CYAN));
                }
                Publico publico = (Publico) item;
                tipo.setText(publico.getTipo());
                locEmpresa.setText(publico.getEmpresa());
                constraintLayout.removeView(marca);
                constraintLayout.removeView(modelo);
                constraintLayout.removeView(cor);
                constraintLayout.removeView(labelMarca);
                constraintLayout.removeView(labelModelo);
                constraintLayout.removeView(labelCor);
                constraintSet.connect(labelLocEmpresa.getId(), ConstraintSet.TOP,
                        tipo.getId(), ConstraintSet.BOTTOM, 16);


            } else {
                if (item instanceof Compartilhado) {
                    linha2.setText("Compartilhado");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        avatar.setBackgroundTintList(getColorStateList(R.color.compartilhado));
                    } else {
                        avatar.setBackgroundTintList(ColorStateList.valueOf(RED));
                    }
                    Compartilhado compartilhado = (Compartilhado) item;
                    tipo.setText(compartilhado.getTipo());
                    locEmpresa.setText(compartilhado.getEmpresa());
                    constraintLayout.removeView(marca);
                    constraintLayout.removeView(modelo);
                    constraintLayout.removeView(cor);
                    constraintLayout.removeView(labelMarca);
                    constraintLayout.removeView(labelModelo);
                    constraintLayout.removeView(labelCor);
                    constraintSet.connect(labelLocEmpresa.getId(), ConstraintSet.TOP,
                            tipo.getId(), ConstraintSet.BOTTOM, 16);
                } else {
                    if (item instanceof Particular) {
                        linha2.setText("Particular");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            avatar.setBackgroundTintList(getColorStateList(R.color.particular));
                        } else {
                            avatar.setBackgroundTintList(ColorStateList.valueOf(YELLOW));
                        }
                        Particular particular = (Particular) item;
                        tipo.setText(particular.getTipo());
                        marca.setText(particular.getMarca());
                        modelo.setText(particular.getModelo());
                        cor.setText(particular.getCor());
                        constraintLayout.removeView(labelLocEmpresa);
                        constraintLayout.removeView(locEmpresa);
                    } else {
                        linha2.setText("");
                    }
                }
            }
        }
        constraintSet.applyTo(constraintLayout);

    }

    public void editar(View view) {
        Intent intent;
        if (item instanceof Alugado) {
            intent = new Intent(this, addMeioDeTransporteAlugadoActivity.class);
        } else if (item instanceof Publico) {
            intent = new Intent(this, addMeioDeTransportePublicoActivity.class);
        } else if (item instanceof Compartilhado) {
            intent = new Intent(this, addMeioDeTransporteCompartilhadoActivity.class);
        } else {
            intent = new Intent(this, addMeioDeTranporteParticularActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("descricao", item.getDescricao());
        intent.putExtra("item", item);
        startActivity(intent);
        finish();
    }

    public void excluir(View view) {
        long result = 0;
        if(item instanceof Particular){
            result = pardao.deleteByID((Particular)item);
        }else if(item instanceof Compartilhado){
            result = cdao.deleteByID((Compartilhado)item);
        }else if(item instanceof Publico){
            result = pudao.deleteByID((Publico)item);
        }else{
            result = aldao.deleteByID((Alugado)item);
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
