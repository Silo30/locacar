package inmotion.sistema.uninassau.pe.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import inmotion.sistema.uninassau.pe.daos.meiosdetransporte.MeiosDeTransporteDAO;
import inmotion.sistema.uninassau.pe.model.eventos.Evento;
import inmotion.sistema.uninassau.pe.model.eventos.Gasto;
import inmotion.sistema.uninassau.pe.model.eventos.Viagem;
import inmotion.sistema.uninassau.pe.move.R;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class EventoAdapter extends RecyclerView.Adapter<TwoLineViewHolder> {

    public interface OnEventoClickListener {
        void onItemClick(Evento item);
    }

    private final List<Evento> lista;
    private final OnEventoClickListener listener;
    private MeiosDeTransporteDAO dao;
    Context context;

    public EventoAdapter(List<Evento> lista, OnEventoClickListener listener, Context context) {
        this.lista = lista;
        this.listener = listener;
        this.context = context;
        dao = new MeiosDeTransporteDAO(context);
    }

    @Override
    public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TwoLineViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_view_with_avatar, parent, false));
    }

    @Override
    public void onBindViewHolder(TwoLineViewHolder holder, int position) {
        final Evento item = lista.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

        if (item instanceof Viagem) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.avatar.setBackgroundTintList(context.getColorStateList(R.color.viagem));
            } else{
                holder.avatar.setBackgroundTintList(ColorStateList.valueOf(GREEN));
            }
            holder.avatar_text.setText("V");

            Viagem it = (Viagem) item;

            String linha1 = "Viagem de ";
            linha1 += String.format("%1$,.2f", it.getDistancia());
            linha1 += " Km";
            holder.linha1.setText(linha1);

            if (dao.readByID(it.getMeiodetransporte_id()) != null) {
                holder.linha2.setText(dao.readByID(it.getMeiodetransporte_id()).toString());
            }

        } else if (item instanceof Gasto) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.avatar.setBackgroundTintList(context.getColorStateList(R.color.despesa));
            } else{
                holder.avatar.setBackgroundTintList(ColorStateList.valueOf(RED));
            }
            holder.avatar_text.setText("$");

            Gasto it = (Gasto) item;

            String linha1 = "Gasto de R$";
            linha1 += String.format("%1$,.2f", it.getValor());
            holder.linha1.setText(linha1);

            if (dao.readByID(it.getMeiodetransporte_id()) != null) {
                holder.linha2.setText(dao.readByID(it.getMeiodetransporte_id()).toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }
}
