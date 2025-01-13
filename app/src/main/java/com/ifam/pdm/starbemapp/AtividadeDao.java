package com.ifam.pdm.starbemapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ifam.pdm.starbemapp.BDAtividades;

import java.util.ArrayList;
import java.util.List;

public class AtividadeDao {

    private SQLiteDatabase db;

    public AtividadeDao(Context context) {
        BDAtividades bdAtividades = new BDAtividades(context);
        db = bdAtividades.getWritableDatabase();
    }

    public void inserirAtividade(String descricao) {
        ContentValues valores = new ContentValues();
        valores.put("descricao", descricao);
        db.insert("atividade", null, valores);
    }

    public List<String> listarAtividades() {
        List<String> atividades = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM atividade", null);

        if (cursor.moveToFirst()) {
            do {
                String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
                atividades.add(descricao);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return atividades;
    }
}
