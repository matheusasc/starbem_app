package com.ifam.pdm.starbemapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ifam.pdm.starbemapp.R;
import com.ifam.pdm.starbemapp.model.Atividade;
import com.ifam.pdm.starbemapp.model.AtividadeDao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeViewHolder> {

    private List<Atividade> atividades;
    private Context context;
    private AtividadeDao atividadeDao;
    private Handler handler = new Handler(Looper.getMainLooper()); // Para postergar atualização

    public AtividadeAdapter(List<Atividade> atividades, Context context) {
        this.atividades = atividades;
        this.context = context;
        this.atividadeDao = new AtividadeDao(context);
        ordenarLista(); // Ordena inicialmente
    }

    @NonNull
    @Override
    public AtividadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_atividade, parent, false);
        return new AtividadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeViewHolder holder, int position) {
        final Atividade atividade = atividades.get(position);
        holder.txtDescricao.setText(atividade.getDescricao());

        // Garante que o estado da checkbox reflita o correto
        holder.checkboxAtividade.setOnCheckedChangeListener(null);
        holder.checkboxAtividade.setChecked(atividade.isConcluida());
        atualizarVisual(holder, atividade.isConcluida());

        holder.checkboxAtividade.setOnCheckedChangeListener((buttonView, isChecked) -> {
            atividadeDao.marcarComoConcluida(atividade.getId(), isChecked);
            atividade.setConcluida(isChecked);

            atualizarVisual(holder, isChecked);

            // Usa Handler para postergar a atualização da lista
            handler.post(() -> ordenarLista());
        });

        holder.iconEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Editar Atividade");

            final EditText input = new EditText(context);
            input.setText(atividade.getDescricao());
            builder.setView(input);

            builder.setPositiveButton("Salvar", (dialog, which) -> {
                String novaDescricao = input.getText().toString();
                atividadeDao.atualizarAtividade(atividade.getId(), novaDescricao);
                atividade.setDescricao(novaDescricao);
                notifyItemChanged(position);
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    // Atualiza a aparência da atividade quando concluída/não concluída
    private void atualizarVisual(AtividadeViewHolder holder, boolean concluida) {
        if (concluida) {
            holder.txtDescricao.setTextColor(Color.GRAY);
            holder.txtDescricao.setPaintFlags(holder.txtDescricao.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.cardAtividade.setCardBackgroundColor(Color.parseColor("#D3D3D3"));
            holder.cardAtividade.setAlpha(0.5f);
        } else {
            holder.txtDescricao.setTextColor(Color.BLACK);
            holder.txtDescricao.setPaintFlags(holder.txtDescricao.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.cardAtividade.setCardBackgroundColor(Color.parseColor("#BBDB9B"));
            holder.cardAtividade.setAlpha(1.0f);
        }
    }

    // Ordena a lista para que atividades concluídas fiquem no final
    private void ordenarLista() {
        Collections.sort(atividades, Comparator.comparing(Atividade::isConcluida));
        notifyDataSetChanged(); // Notifica que os dados mudaram
    }

    public static class AtividadeViewHolder extends RecyclerView.ViewHolder {

        TextView txtDescricao;
        CheckBox checkboxAtividade;
        ImageView iconEdit;
        CardView cardAtividade;

        public AtividadeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.textViewDescricao);
            checkboxAtividade = itemView.findViewById(R.id.checkboxAtividade);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            cardAtividade = itemView.findViewById(R.id.cardAtividade);
        }
    }
}
