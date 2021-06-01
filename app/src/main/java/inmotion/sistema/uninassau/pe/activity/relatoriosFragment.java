package inmotion.sistema.uninassau.pe.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import inmotion.sistema.uninassau.pe.daos.RelatoriosDAO;
import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.MeiosDeTransporteDAO;
import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;
import inmotion.sistema.uninassau.pe.model.relatorios.EstatisticasGeral;
import inmotion.sistema.uninassau.pe.model.relatorios.EstatisticasPorMeioDeTransporte;
import inmotion.sistema.uninassau.pe.move.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class relatoriosFragment extends Fragment {

    MeiosDeTransporteDAO daoMeioTransporte;
    RelatoriosDAO dao;
    private static SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");


    public relatoriosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relatorios, container, false);
        daoMeioTransporte = new MeiosDeTransporteDAO(getActivity().getApplicationContext());
        dao = new RelatoriosDAO(getActivity().getApplicationContext());

        Spinner meioTransporteRelatorioSpinner = (Spinner) view.findViewById(R.id.meioTransporteRelatorioSpinner);

        List<MeioDeTransporte> listaMeios = new ArrayList<>();
        MeioDeTransporte todos = new MeioDeTransporte();
        todos.setDescricao("Todos");
        listaMeios.add(todos);
        listaMeios.addAll(daoMeioTransporte.readAllSpecific());
        ArrayAdapter<MeioDeTransporte> adapter =
                new ArrayAdapter<MeioDeTransporte>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listaMeios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meioTransporteRelatorioSpinner.setAdapter(adapter);
        meioTransporteRelatorioSpinner.setSelection(getIndex(meioTransporteRelatorioSpinner, "Todos"));

        Button btnGerarRelatorio = (Button) view.findViewById(R.id.btnGerarRelatorio);
        btnGerarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String meioTransporteRelatorioSpinner = ((Spinner) getActivity().findViewById(R.id.meioTransporteRelatorioSpinner)).getSelectedItem().toString();
                String relatorioDataInicial = ((EditText) getActivity().findViewById(R.id.relatorioDataInicial)).getText().toString();
                String relatorioDataFinal = ((EditText) getActivity().findViewById(R.id.relatorioDataFinal)).getText().toString();
                if (relatorioDataInicial.equals("") || relatorioDataFinal.equals("") || meioTransporteRelatorioSpinner.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
                } else {
                    Timestamp data1, data2;
                    try {
                        data1 = new Timestamp(out.parse(relatorioDataInicial).getTime());
                        data2 = new Timestamp(out.parse(relatorioDataFinal).getTime());
                        Intent intent;
                        if (meioTransporteRelatorioSpinner.equalsIgnoreCase("Todos")) {
                            EstatisticasGeral item = dao.relatorioGeral(data1, data2);
                            intent = new Intent(getActivity().getApplicationContext(), exibirRelatorioGeralActivity.class);
                            intent.putExtra("item", item);
                        } else {
                            EstatisticasPorMeioDeTransporte item = dao.relatorioIndividual(daoMeioTransporte.findIDByDescricao(meioTransporteRelatorioSpinner), data1, data2);
                            intent = new Intent(getActivity().getApplicationContext(), exibirRelatorioIndividualActivity.class);
                            intent.putExtra("item", item);
                        }
                        startActivity(intent);
                    } catch (ParseException pe2) {
                        Toast.makeText(getActivity().getApplicationContext(), "Formato de data inválido para gerar estatísticas!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return view;
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
