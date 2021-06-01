package inmotion.sistema.uninassau.pe.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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

class MeioTransporteAdapter extends RecyclerView.Adapter<TwoLineViewHolder> {

    public interface OnMeioDeTransporteClickListener {
        void onItemClick(MeioDeTransporte item);
    }

    private final List<MeioDeTransporte> lista;
    private final OnMeioDeTransporteClickListener listener;
    Context context;

    public MeioTransporteAdapter(List<MeioDeTransporte> lista, OnMeioDeTransporteClickListener listener, Context context) {
        this.lista = lista;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public TwoLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TwoLineViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_view_with_avatar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TwoLineViewHolder holder, int position) {
        final MeioDeTransporte item = lista.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

        if (item.getDescricao() != null) {
            holder.linha1.setText(item.getDescricao());
            if (item.getDescricao().length() > 0) {
                holder.avatar_text.setText(item.getDescricao().substring(0, 1).toUpperCase());
            } else {
                holder.avatar_text.setText("");
            }
        } else {
            holder.linha1.setText("");
            holder.avatar_text.setText("");
        }

        if (item instanceof Alugado) {
            holder.linha2.setText("Alugado");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.avatar.setBackgroundTintList(context.getColorStateList(R.color.alugado));
            } else {
                holder.avatar.setBackgroundTintList(ColorStateList.valueOf(GREEN));
            }

        } else {
            if (item instanceof Publico) {
                holder.linha2.setText("Público");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.avatar.setBackgroundTintList(context.getColorStateList(R.color.publico));
                } else {
                    holder.avatar.setBackgroundTintList(ColorStateList.valueOf(CYAN));
                }
            } else {
                if (item instanceof Compartilhado) {
                    holder.linha2.setText("Compartilhado");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.avatar.setBackgroundTintList(context.getColorStateList(R.color.compartilhado));
                    } else {
                        holder.avatar.setBackgroundTintList(ColorStateList.valueOf(RED));
                    }
                } else {
                    if (item instanceof Particular) {
                        holder.linha2.setText("Particular");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            holder.avatar.setBackgroundTintList(context.getColorStateList(R.color.particular));
                        } else {
                            holder.avatar.setBackgroundTintList(ColorStateList.valueOf(YELLOW));
                        }
                    } else {
                        holder.linha2.setText("");
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }
}
