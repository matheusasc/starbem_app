package com.ifam.pdm.starbemapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ifam.pdm.starbemapp.R;
import com.ifam.pdm.starbemapp.adapter.AtividadeAdapter;
import com.ifam.pdm.starbemapp.model.Atividade;
import com.ifam.pdm.starbemapp.model.AtividadeDao;
import com.ifam.pdm.starbemapp.util.ProfileImageManager;

import java.util.List;


public class HomeFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private AtividadeDao atividadeDao;
    private RecyclerView recyclerView;
    private AtividadeAdapter adapter;
    private ImageView imgProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.status_bar_color)); // Cor barra de status
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Para ícones claros
        }

        atividadeDao = new AtividadeDao(getContext());
        TextView textViewSaudacao = view.findViewById(R.id.Saudacao);
        imgProfile = view.findViewById(R.id.imgProfile);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", requireContext().MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Matheus");

        textViewSaudacao.setText("Olá, \n" + userName);

        recyclerView = view.findViewById(R.id.recyclerViewAtividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        atualizarLista();

        // Configurar clique na imagem do perfil
        imgProfile.setOnClickListener(v -> showProfileOptionsDialog());

        // Carregar imagem salva
        ProfileImageManager.loadProfileImage(imgProfile, requireContext());

        ImageButton btnAdicionarAtividades = view.findViewById(R.id.btnAdicionarAtividades);
        btnAdicionarAtividades.setOnClickListener(v -> showAddActivityDialog());

        return view;
    }

    private void showProfileOptionsDialog() {
        String[] options = {"Visualizar Imagem", "Alterar Imagem"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Opções de Perfil")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Visualizar Imagem
                        viewProfileImage();
                    } else if (which == 1) {
                        // Alterar Imagem
                        openGallery();
                    }
                })
                .show();
    }

    private void viewProfileImage() {
        Bitmap bitmap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
        if (bitmap != null) {
            // Exibir a imagem em um diálogo
            ImageView imageView = new ImageView(requireContext());
            imageView.setImageBitmap(bitmap);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Visualizar Imagem")
                    .setView(imageView)
                    .setPositiveButton("Fechar", null)
                    .show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private void loadProfileImage() {
        ProfileImageManager.loadProfileImage(imgProfile, getContext());
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