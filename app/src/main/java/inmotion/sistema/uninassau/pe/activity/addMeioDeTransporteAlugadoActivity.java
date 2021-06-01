package inmotion.sistema.uninassau.pe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.AlugadoDAO;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Alugado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Compartilhado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Particular;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Publico;
import inmotion.sistema.uninassau.pe.move.R;

public class addMeioDeTransporteAlugadoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    AlugadoDAO dao;
    MeioDeTransporte item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meio_de_transporte_alugado);
        dao = new AlugadoDAO(getApplicationContext());

        Spinner categoriaSpinnerAlugado = (Spinner) findViewById(R.id.categoriaSpinnerAlugado);
        categoriaSpinnerAlugado.setSelection(1);
        categoriaSpinnerAlugado.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        String descricao = intent.getStringExtra("descricao");
        EditText editText = (EditText) findViewById(R.id.descricaoAlugado);
        editText.setText(descricao);

        item = (MeioDeTransporte) intent.getSerializableExtra("item");
        EditText locadora = (EditText) findViewById(R.id.locadoraAlugado);
        EditText marca = (EditText) findViewById(R.id.marcaAlugado);
        EditText modelo = (EditText) findViewById(R.id.modeloAlugado);
        EditText cor = (EditText) findViewById(R.id.corAlugado);
        Spinner tipo = (Spinner) findViewById(R.id.tipoSpinnerAlugado);
        if (item != null) {
            categoriaSpinnerAlugado.setEnabled(false);
            Button button = (Button) findViewById(R.id.btnCriarMeioAlugado);
            button.setText("Confirmar Alterações");
            if (item instanceof Alugado) {
                Alugado alugado = (Alugado) item;
                locadora.setText(alugado.getLocadora());
                marca.setText(alugado.getMarca());
                modelo.setText(alugado.getModelo());
                cor.setText(alugado.getCor());
                tipo.setSelection(getIndex(tipo, alugado.getTipo()+""));
            } else {
                if (item instanceof Publico) {
                    Publico publico = (Publico) item;
                    locadora.setText(publico.getEmpresa());
                    tipo.setSelection(getIndex(tipo, publico.getTipo()+""));
                } else {
                    if (item instanceof Compartilhado) {
                        Compartilhado compartilhado = (Compartilhado) item;
                        locadora.setText(compartilhado.getEmpresa());
                        tipo.setSelection(getIndex(tipo, compartilhado.getTipo()+""));
                    } else {
                        //Particular
                        Particular particular = (Particular) item;
                        marca.setText(particular.getMarca());
                        modelo.setText(particular.getModelo());
                        cor.setText(particular.getCor());
                        tipo.setSelection(getIndex(tipo, particular.getTipo()+""));
                    }
                }
            }
        }
    }

    /**
     * Chamada ao clicar no botão de Salvar
     */
    public void salvar(View view) {
        String descricao = ((EditText) findViewById(R.id.descricaoAlugado)).getText().toString();
        String tipo = ((Spinner) findViewById(R.id.tipoSpinnerAlugado)).getSelectedItem().toString();
        String locadora = ((EditText) findViewById(R.id.locadoraAlugado)).getText().toString();
        String marca = ((EditText) findViewById(R.id.marcaAlugado)).getText().toString();
        String modelo = ((EditText) findViewById(R.id.modeloAlugado)).getText().toString();
        String cor = ((EditText) findViewById(R.id.corAlugado)).getText().toString();
        if (descricao.equals("") || locadora.equals("") || marca.equals("") || modelo.equals("") || cor.equals("")) {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
        } else {
            if(item == null){
                Alugado a = new Alugado();
                a.setDescricao(descricao);
                a.setTipo(tipo);
                a.setLocadora(locadora);
                a.setMarca(marca);
                a.setModelo(modelo);
                a.setCor(cor);
                dao.insert(a);
            } else{
                Alugado a = new Alugado();
                a.setDescricao(descricao);
                a.setTipo(tipo);
                a.setLocadora(locadora);
                a.setMarca(marca);
                a.setModelo(modelo);
                a.setCor(cor);
                a.setId(item.getId());
                long id = dao.update(a);
                if(id != -1L){
                    setResult(getResources().getInteger(R.integer.SUCESS));
                    finish();
                    Toast.makeText(this, "Meio de Transporte Alugado alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    setResult(getResources().getInteger(R.integer.NO_SUCESS));
                    finish();
                    Toast.makeText(this, "Falha ao Alterar Meio de Transporte Alugado!", Toast.LENGTH_SHORT).show();
                }
            }
            setResult(getResources().getInteger(R.integer.SUCESS));
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getItemAtPosition(position).toString()) {
            case "Particular": {
                //Particular
                Intent intent = new Intent(this, addMeioDeTranporteParticularActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION  | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                EditText editText = (EditText) findViewById(R.id.descricaoAlugado);
                String message = editText.getText().toString();
                intent.putExtra("descricao", message);
                intent.putExtra("item", item);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
            }
            case "Público": {
                //Público
                Intent intent = new Intent(this, addMeioDeTransportePublicoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                EditText editText = (EditText) findViewById(R.id.descricaoAlugado);
                String message = editText.getText().toString();
                intent.putExtra("descricao", message);
                intent.putExtra("item", item);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
            }
            case "Compartilhado": {
                //Compartilhado
                Intent intent = new Intent(this, addMeioDeTransporteCompartilhadoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                EditText editText = (EditText) findViewById(R.id.descricaoAlugado);
                String message = editText.getText().toString();
                intent.putExtra("descricao", message);
                intent.putExtra("item", item);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }
}
