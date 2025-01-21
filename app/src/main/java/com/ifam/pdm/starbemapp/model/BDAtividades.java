package com.ifam.pdm.starbemapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDAtividades extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "bd_atividades.db";
    private static final int VERSAO = 6;


    public BDAtividades(@Nullable Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela com a coluna 'concluida'
        String sql = "CREATE TABLE atividade (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                "concluida INTEGER DEFAULT 0" + // Adicionando o campo 'concluida'
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Exclui a tabela atividade se ela existir
        db.execSQL("DROP TABLE IF EXISTS atividade");

        // Cria a tabela novamente
        String sql = "CREATE TABLE atividade (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                "concluida INTEGER DEFAULT 0" + // Adicionando a coluna 'concluida'
                ")";
        db.execSQL(sql);
    }

    // Método para excluir o banco de dados e forçar a recriação
    public void excluirBancoDeDados(Context context) {
        context.deleteDatabase(NOME_BANCO);
    }
}
