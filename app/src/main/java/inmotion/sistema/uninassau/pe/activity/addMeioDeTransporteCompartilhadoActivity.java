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

import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.CompartilhadoDAO;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Alugado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Compartilhado;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Particular;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.Publico;
import inmotion.sistema.uninassau.pe.move.R;

public class addMeioDeTransporteCompartilhadoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CompartilhadoDAO dao;
    MeioDeTransporte item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meio_de_transporte_compartilhado);
        dao = new CompartilhadoDAO(getApplicationContext());

        Spinner categoriaSpinnerCompartilhado = (Spinner) findViewById(R.id.categoriaSpinnerCompartilhado);
        categoriaSpinnerCompartilhado.setSelection(3);
        categoriaSpinnerCompartilhado.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        String descricao = intent.getStringExtra("descricao");
        EditText editText = (EditText) findViewById(R.id.descricaoCompartilhado);
        editText.setText(descricao);

        item = (MeioDeTransporte) intent.getSerializableExtra("item");
        EditText empresa = (EditText) findViewById(R.id.empresaCompartilhado);
        Spinner tipo = (Spinner) findViewById(R.id.tipoSpinnerCompartilhado);
        if (item != null) {
            categoriaSpinnerCompartilhado.setEnabled(false);
            Button button = (Button) findViewById(R.id.btnCriarMeioCompartilhado);
            button.setText("Confirmar Alterações");
            if (item instanceof Alugado) {
                Alugado alugado = (Alugado) item;
                empresa.setText(alugado.getLocadora());
                tipo.setSelection(getIndex(tipo, alugado.getTipo()+""));
            } else {
                if (item instanceof Publico) {
                    Publico publico = (Publico) item;
                    empresa.setText(publico.getEmpresa());
                    tipo.setSelection(getIndex(tipo, publico.getTipo()+""));
                } else {
                    if (item instanceof Compartilhado) {
                        Compartilhado compartilhado = (Compartilhado) item;
                        empresa.setText(compartilhado.getEmpresa());
                        tipo.setSelection(getIndex(tipo, compartilhado.getTipo()+""));
                    } else {
                        //Particular
                        Particular particular = (Particular) item;
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
        String descricao = ((EditText) findViewById(R.id.descricaoCompartilhado)).getText().toString();
        String tipo = ((Spinner) findViewById(R.id.tipoSpinnerCompartilhado)).getSelectedItem().toString();
        String empresa = ((EditText) findViewById(R.id.empresaCompartilhado)).getText().toString();
        if (descricao.equals("") || empresa.equals("")) {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
        } else {
            if (item == null) {
                Compartilhado c = new Compartilhado();
                c.setEmpresa(empresa);
                c.setDescricao(descricao);
                c.setTipo(tipo);
                dao.insert(c);
            } else {
                Compartilhado c = new Compartilhado();
                c.setEmpresa(empresa);
                c.setDescricao(descricao);
                c.setTipo(tipo);
                c.setId(item.getId());
                long id = dao.update(c);
                if(id != -1L){
                    setResult(getResources().getInteger(R.integer.SUCESS));
                    finish();
                    Toast.makeText(this, "Meio de Transporte Compartilhado alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    setResult(getResources().getInteger(R.integer.NO_SUCESS));
                    finish();
                    Toast.makeText(this, "Falha ao Alterar Meio de Transporte Compartilhado!", Toast.LENGTH_SHORT).show();
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                EditText editText = (EditText) findViewById(R.id.descricaoCompartilhado);
                String message = editText.getText().toString();
                intent.putExtra("descricao", message);
                intent.putExtra("item", item);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
            }
            case "Alugado": {
                //Alugado
                Intent intent = new Intent(this, addMeioDeTransporteAlugadoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                EditText editText = (EditText) findViewById(R.id.descricaoCompartilhado);
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
                EditText editText = (EditText) findViewById(R.id.descricaoCompartilhado);
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
