package com.ifam.pdm.starbemapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ifam.pdm.starbemapp.LoginActivity;
import com.ifam.pdm.starbemapp.R;
import com.ifam.pdm.starbemapp.adapter.AtividadeAdapter;
import com.ifam.pdm.starbemapp.model.Atividade;
import com.ifam.pdm.starbemapp.model.AtividadeDao;

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
        TextView textViewSaudacao = view.findViewById(R.id.Saudacao);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", requireContext().MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Matheus");

        textViewSaudacao.setText("Olá, " + userName);

        recyclerView = view.findViewById(R.id.recyclerViewAtividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        atualizarLista();

        ImageButton btnAdicionarAtividades = view.findViewById(R.id.btnAdicionarAtividades);
        btnAdicionarAtividades.setOnClickListener(v -> showAddActivityDialog());

        // Configurar o botão de logout
        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> {
            // Limpar os dados de login
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Redirecionar para a LoginActivity
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

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
                        boolean concluida = false;
                        atividadeDao.inserirAtividade(description, concluida);

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
        List<Atividade> atividades = atividadeDao.listarAtividades();
        adapter = new AtividadeAdapter(atividades, getContext());
        recyclerView.setAdapter(adapter);
    }
}
