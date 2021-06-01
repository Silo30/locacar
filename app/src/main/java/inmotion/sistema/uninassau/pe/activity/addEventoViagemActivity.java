package inmotion.sistema.uninassau.pe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import inmotion.sistema.uninassau.pe.daos.eventos.ViagemDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.MeiosDeTransporteDAO;
import inmotion.sistema.uninassau.pe.model.eventos.Evento;
import inmotion.sistema.uninassau.pe.model.eventos.Gasto;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;
import inmotion.sistema.uninassau.pe.model.eventos.Viagem;
import inmotion.sistema.uninassau.pe.move.R;


public class addEventoViagemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MeiosDeTransporteDAO daoMeioTransporte;
    ViagemDAO dao;
    Evento item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evento_viagem);
        daoMeioTransporte = new MeiosDeTransporteDAO(getApplicationContext());
        dao = new ViagemDAO(getApplicationContext());

        if (daoMeioTransporte.readAllSpecific().size() > 0) {

            Spinner tipoEventosSpinner = (Spinner) findViewById(R.id.tipoEventosSpinner);
            tipoEventosSpinner.setSelection(0);
            tipoEventosSpinner.setOnItemSelectedListener(this);

            Spinner meioTransporteEventoSpinner = (Spinner) findViewById(R.id.meioTransporteEventoSpinner);

            List<MeioDeTransporte> listaMeios = daoMeioTransporte.readAllSpecific();
            ArrayAdapter<MeioDeTransporte> adapter =
                    new ArrayAdapter<MeioDeTransporte>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listaMeios);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            meioTransporteEventoSpinner.setAdapter(adapter);
            meioTransporteEventoSpinner.setSelection(getIndex(meioTransporteEventoSpinner, getIntent().getStringExtra("meioTransporte")));

            Intent intent = getIntent();
            String data = intent.getStringExtra("data");
            EditText editText = (EditText) findViewById(R.id.dataEvento);
            editText.setText(data);

            item = (Evento) intent.getSerializableExtra("item");
            EditText horaInicial = (EditText) findViewById(R.id.horaInicial);
            EditText horaFinal = (EditText) findViewById(R.id.horaFinal);
            EditText distancia = (EditText) findViewById(R.id.distanciaEvento);
            if (item != null) {
                tipoEventosSpinner.setEnabled(false);
                Button button = (Button) findViewById(R.id.btnSalvarEvento);
                button.setText("Confirmar Alterações");
                if (item instanceof Viagem) {
                    Viagem viagem = (Viagem) item;
                    meioTransporteEventoSpinner.setSelection(getIndex(meioTransporteEventoSpinner, daoMeioTransporte.readByID(viagem.getMeiodetransporte_id()).getDescricao()));
                    horaInicial.setText(viagem.getInicio());
                    horaFinal.setText(viagem.getFim());
                    distancia.setText(viagem.getDistancia() + "");
                } else {
                    //Gasto
                    Gasto gasto = (Gasto) item;
                    meioTransporteEventoSpinner.setSelection(getIndex(meioTransporteEventoSpinner, daoMeioTransporte.readByID(gasto.getMeiodetransporte_id()).getDescricao()));
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
        String horaInicial = ((EditText) findViewById(R.id.horaInicial)).getText().toString();
        String horaFinal = ((EditText) findViewById(R.id.horaFinal)).getText().toString();
        String dataEvento = ((EditText) findViewById(R.id.dataEvento)).getText().toString();
        String distanciaEvento = ((EditText) findViewById(R.id.distanciaEvento)).getText().toString();
        String meioTransporteEventoSpinner = ((Spinner) findViewById(R.id.meioTransporteEventoSpinner)).getSelectedItem().toString();
        if (horaInicial.equals("") || horaFinal.equals("") || dataEvento.equals("") || distanciaEvento.equals("") || meioTransporteEventoSpinner.equals("")) {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
        } else {
            if (item == null) {
                try {
                    Viagem v = new Viagem();
                    v.setDistancia(Float.parseFloat(distanciaEvento.replace(",", ".")));
                    v.setFim(horaFinal);
                    v.setInicio(horaInicial);
                    v.setData(dataEvento);
                    v.setMeiodetransporte_id(daoMeioTransporte.findIDByDescricao(meioTransporteEventoSpinner));
                    dao.insert(v);
                    setResult(getResources().getInteger(R.integer.SUCESS));
                    finish();
                    Toast.makeText(this, "Evento registrado com sucesso!", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Informe apenas números na distância e não coloque separação de milhares.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Viagem v = new Viagem();
                v.setId(item.getId());
                v.setData(item.getData());
                v.setMeiodetransporte_id(item.getMeiodetransporte_id());
                v.setDistancia(Float.parseFloat(distanciaEvento.replace(",", ".")));
                v.setFim(horaFinal);
                v.setInicio(horaInicial);
                v.setData(dataEvento);
                v.setMeiodetransporte_id(daoMeioTransporte.findIDByDescricao(meioTransporteEventoSpinner));
                long id = dao.update(v);
                if(id != -1L){
                    setResult(getResources().getInteger(R.integer.SUCESS));
                    finish();
                    Toast.makeText(this, "Viagem alterada com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    setResult(getResources().getInteger(R.integer.NO_SUCESS));
                    finish();
                    Toast.makeText(this, "Falha ao Alterar Viagem!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getItemAtPosition(position).toString()) {
            case "Despesa": {
                Intent intent = new Intent(this, addEventoDespesaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                String dataEvento = ((EditText) findViewById(R.id.dataEvento)).getText().toString();
                intent.putExtra("data", dataEvento);
                intent.putExtra("meioTransporte", ((Spinner) findViewById(R.id.meioTransporteEventoSpinner)).getSelectedItem().toString());
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
