package com.ifam.pdm.starbemapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeViewHolder> {

    private List<String> atividades;
    private Context context;

    public AtividadeAdapter(List<String> atividades, Context context) {
        this.atividades = atividades;
        this.context = context;
    }

    @NonNull
    @Override
    public AtividadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_atividade, parent, false);
        return new AtividadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeViewHolder holder, int position) {
        String descricao = atividades.get(position);
        holder.txtDescricao.setText(descricao);
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    public static class AtividadeViewHolder extends RecyclerView.ViewHolder {

        TextView txtDescricao;

        public AtividadeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.textViewDescricao);
        }
    }
}
