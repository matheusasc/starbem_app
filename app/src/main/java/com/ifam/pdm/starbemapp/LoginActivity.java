package com.ifam.pdm.starbemapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.ifam.pdm.starbemapp.MainActivity;
import com.ifam.pdm.starbemapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar se o usuário já está logado
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Usuário já está logado, vá direto para a MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return; // Impedir que o restante do código seja executado
        }

        // Exibir a tela de login
        setContentView(R.layout.activity_login);

        EditText editTextUserName = findViewById(R.id.editTextUserName);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(view -> {
            String userName = editTextUserName.getText().toString();
            if (!userName.isEmpty()) {
                // Salvar o nome do usuário e marcar o login como feito
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", userName); // Salva o nome do usuário
                editor.putBoolean("isLoggedIn", true); // Marca como logado
                editor.apply();

                // Ir para a MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
