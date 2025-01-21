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
        String sql = "CREATE TABLE atividade (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                "concluida INTEGER DEFAULT 0" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS atividade");

        String sql = "CREATE TABLE atividade (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                "concluida INTEGER DEFAULT 0" +
                ")";
        db.execSQL(sql);
    }

    public void excluirBancoDeDados(Context context) {
        context.deleteDatabase(NOME_BANCO);
    }
}
