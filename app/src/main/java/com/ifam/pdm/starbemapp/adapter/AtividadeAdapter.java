package com.ifam.pdm.starbemapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ifam.pdm.starbemapp.R;
import com.ifam.pdm.starbemapp.model.Atividade;
import com.ifam.pdm.starbemapp.model.AtividadeDao;

import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeViewHolder> {

    private List<Atividade> atividades;
    private Context context;
    private AtividadeDao atividadeDao;

    public AtividadeAdapter(List<Atividade> atividades, Context context) {
        this.atividades = atividades;
        this.context = context;
        this.atividadeDao = new AtividadeDao(context);

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

        holder.checkboxAtividade.setChecked(atividade.isConcluida());
        holder.checkboxAtividade.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AtividadeDao dao = new AtividadeDao(context);
            dao.alternarEstadoConcluida(atividade.getId());
            dao.marcarComoConcluida(atividade.getId(), isChecked);
            if (isChecked) {
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

        });

        holder.iconEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Editar Atividade");

            final EditText input = new EditText(context);
            input.setText(atividade.getDescricao());
            builder.setView(input);

            builder.setPositiveButton("Salvar", (dialog, which) -> {
                String novaDescricao = input.getText().toString();
                // Atualiza no banco de dados
                atividadeDao.atualizarAtividade(atividade.getId(), novaDescricao);

                // Atualiza a lista de atividades localmente e notifica o adapter
                atividade.setDescricao(novaDescricao);
                notifyItemChanged(position);  // Atualiza a atividade no RecyclerView
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }


    @Override
    public int getItemCount() {
        return atividades.size();
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
