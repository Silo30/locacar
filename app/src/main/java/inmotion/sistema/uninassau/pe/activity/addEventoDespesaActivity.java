package inmotion.sistema.uninassau.pe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import inmotion.sistema.uninassau.pe.daos.eventos.GastoDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.MeiosDeTransporteDAO;
import inmotion.sistema.uninassau.pe.model.eventos.Evento;
import inmotion.sistema.uninassau.pe.model.eventos.Gasto;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;
import inmotion.sistema.uninassau.pe.model.eventos.Viagem;
import inmotion.sistema.uninassau.pe.move.R;

public class addEventoDespesaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MeiosDeTransporteDAO daoMeioTransporte;
    GastoDAO dao;
    Evento item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evento_despesa);
        daoMeioTransporte = new MeiosDeTransporteDAO(getApplicationContext());
        dao = new GastoDAO(getApplicationContext());

        if (daoMeioTransporte.readAllSpecific().size() > 0) {

            Spinner tipoEventosSpinner2 = (Spinner) findViewById(R.id.tipoEventosSpinner2);
            tipoEventosSpinner2.setSelection(1);
            tipoEventosSpinner2.setOnItemSelectedListener(this);

            Spinner meioTransporteEventoSpinner = (Spinner) findViewById(R.id.meioTransporteEventoSpinner2);

            List<MeioDeTransporte> listaMeios = daoMeioTransporte.readAllSpecific();
            ArrayAdapter<MeioDeTransporte> adapter =
                    new ArrayAdapter<MeioDeTransporte>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listaMeios);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            meioTransporteEventoSpinner.setAdapter(adapter);
            meioTransporteEventoSpinner.setSelection(getIndex(meioTransporteEventoSpinner, getIntent().getStringExtra("meioTransporte")));

            Intent intent = getIntent();
            String data = intent.getStringExtra("data");
            EditText editText = (EditText) findViewById(R.id.dataDespesa);
            editText.setText(data);

            item = (Evento) intent.getSerializableExtra("item");
            Spinner categoriaDespesaSpinner = (Spinner) findViewById(R.id.categoriaDespesaSpinner);
            EditText valorDespesa = (EditText) findViewById(R.id.valorDespesa);
            EditText descricao = (EditText) findViewById(R.id.descricaoDespesa);
            if (item != null) {
                tipoEventosSpinner2.setEnabled(false);
                Button button = (Button) findViewById(R.id.btnSalvarDespesa);
                button.setText("Confirmar Alterações");
                if (item instanceof Viagem) {
                    Viagem viagem = (Viagem) item;
                    meioTransporteEventoSpinner.setSelection(getIndex(meioTransporteEventoSpinner, daoMeioTransporte.readByID(viagem.getMeiodetransporte_id()).toString()));
                } else {
                    //Gasto
                    Gasto gasto = (Gasto) item;
                    meioTransporteEventoSpinner.setSelection(getIndex(meioTransporteEventoSpinner, daoMeioTransporte.readByID(gasto.getMeiodetransporte_id()).toString()));
                    valorDespesa.setText(gasto.getValor() + "");
                    descricao.setText(gasto.getObservacao());
                    categoriaDespesaSpinner.setSelection(getIndex(categoriaDespesaSpinner, gasto.getTipo()));
                }
            }

        } else {
            Toast.makeText(this, "Ainda não há nenhum Meio de Transporte cadastrado!", Toast.LENGTH_SHORT).show();
            setResult(getResources().getInteger(R.integer.NO_SUCESS));
            finish();
        }
    }

    /**
     * Chamada ao clicar no botão de Salvar
     */
    public void salvar(View view) {

        String categoriaDespesa = ((Spinner) findViewById(R.id.categoriaDespesaSpinner)).getSelectedItem().toString();
        String dataDespesa = ((EditText) findViewById(R.id.dataDespesa)).getText().toString();
        String valorDespesa = ((EditText) findViewById(R.id.valorDespesa)).getText().toString();
        String meioTransporteDespesaSpinner = ((Spinner) findViewById(R.id.meioTransporteEventoSpinner2)).getSelectedItem().toString();
        String descricao = ((EditText) findViewById(R.id.descricaoDespesa)).getText().toString();

        if (categoriaDespesa.equals("") || dataDespesa.equals("") || valorDespesa.equals("") || meioTransporteDespesaSpinner.equals("") || descricao.equals("")) {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
        } else {
            if (item == null) {
                try {
                    Gasto g = new Gasto();
                    g.setData(dataDespesa);
                    g.setMeiodetransporte_id(daoMeioTransporte.findIDByDescricao(meioTransporteDespesaSpinner));
                    g.setObservacao(descricao);
                    g.setValor(Float.parseFloat(valorDespesa.replace(",", ".")));
                    g.setTipo(categoriaDespesa);
                    dao.insert(g);
                    setResult(getResources().getInteger(R.integer.SUCESS));
                    finish();
                    Toast.makeText(this, "Despesa registrada com sucesso!", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Informe apenas números no valor e não coloque separação de milhares.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Gasto g = new Gasto();
                g.setId(item.getId());
                g.setMeiodetransporte_id(item.getMeiodetransporte_id());
                g.setData(dataDespesa);
                g.setMeiodetransporte_id(daoMeioTransporte.findIDByDescricao(meioTransporteDespesaSpinner));
                g.setObservacao(descricao);
                g.setValor(Float.parseFloat(valorDespesa.replace(",", ".")));
                g.setTipo(categoriaDespesa);
                long id = dao.update(g);
                if(id != -1L){
                    setResult(getResources().getInteger(R.integer.SUCESS));
                    finish();
                    Toast.makeText(this, "Despesa alterada com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    setResult(getResources().getInteger(R.integer.NO_SUCESS));
                    finish();
                    Toast.makeText(this, "Falha ao Alterar Despesa!", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getItemAtPosition(position).toString()) {
            case "Viagem": {
                Intent intent = new Intent(this, addEventoViagemActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION  | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                String dataDespesa = ((EditText) findViewById(R.id.dataDespesa)).getText().toString();
                intent.putExtra("data", dataDespesa);
                intent.putExtra("meioTransporte", ((Spinner) findViewById(R.id.meioTransporteEventoSpinner2)).getSelectedItem().toString());
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
