package com.ifam.pdm.starbemapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class HomeFragment extends Fragment {

    private AtividadeDao atividadeDao;
    private RecyclerView recyclerView;
    private AtividadeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        atividadeDao = new AtividadeDao(getContext());

        recyclerView = view.findViewById(R.id.recyclerViewAtividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        atualizarLista();

        Button btnAdicionarAtividades = view.findViewById(R.id.btnAdicionarAtividades);
        btnAdicionarAtividades.setOnClickListener(v -> showAddActivityDialog());

        return view;
    }

    private void showAddActivityDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_activity, null);

        EditText editTextActivityDescription = dialogView.findViewById(R.id.editTextActivityDescription);

        new AlertDialog.Builder(requireContext())
                .setTitle("Adicionar Atividade")
                .setView(dialogView)
                .setPositiveButton("Adicionar", (dialog, which) -> {
                    String description = editTextActivityDescription.getText().toString().trim();
                    if (!description.isEmpty()) {
                        // Salvar a atividade no banco de dados
                        atividadeDao.inserirAtividade(description);

                        // Atualizar a lista no RecyclerView
                        atualizarLista();

                        Toast.makeText(getContext(), "Atividade adicionada: " + description, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Por favor, insira uma descrição.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    private void atualizarLista() {
        List<String> atividades = atividadeDao.listarAtividades();
        adapter = new AtividadeAdapter(atividades, getContext());
        recyclerView.setAdapter(adapter);
    }
}
