package inmotion.sistema.uninassau.pe.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import inmotion.sistema.uninassau.pe.daos.eventos.EventoDAO;
import inmotion.sistema.uninassau.pe.model.eventos.Evento;
import inmotion.sistema.uninassau.pe.move.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class listEventoFragment extends Fragment {

    RecyclerView recyclerView;
    EventoDAO dao;


    public listEventoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_eventos, container, false);
        dao = new EventoDAO(getActivity().getApplicationContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.eventosLista);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<Evento> lista = dao.readAllSpecific();
        if (!lista.isEmpty()) {
            TextView semEvento = (TextView) view.findViewById(R.id.semEvento);
            semEvento.setVisibility(View.INVISIBLE);
        }
        recyclerView.setAdapter(new EventoAdapter(lista, new EventoAdapter.OnEventoClickListener() {
            @Override
            public void onItemClick(Evento item) {
                Intent intent = new Intent(getActivity().getApplicationContext(), readEventoActivity.class);
                intent.putExtra("item2", item);
                startActivityForResult(intent, getResources().getInteger(R.integer.INTENT_ADD_EVENTO));
            }
        }, getActivity().getApplicationContext()));

        FloatingActionButton btnAddMeioDeTranporte = (FloatingActionButton) view.findViewById(R.id.addEventoDespesa);
        btnAddMeioDeTranporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), addEventoViagemActivity.class);
                startActivityForResult(intent, getResources().getInteger(R.integer.INTENT_ADD_EVENTO));
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getResources().getInteger(R.integer.INTENT_ADD_EVENTO) && resultCode == getResources().getInteger(R.integer.SUCESS)) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}
