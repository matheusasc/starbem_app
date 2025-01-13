package com.ifam.pdm.starbemapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}