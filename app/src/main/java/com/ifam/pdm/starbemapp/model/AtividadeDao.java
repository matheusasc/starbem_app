package com.ifam.pdm.starbemapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifam.pdm.starbemapp.model.BDAtividades;

import java.util.ArrayList;
import java.util.List;

public class AtividadeDao {

    private SQLiteDatabase db;

    public AtividadeDao(Context context) {
        BDAtividades bdAtividades = new BDAtividades(context);
        db = bdAtividades.getWritableDatabase();
    }

    public void inserirAtividade(String descricao, boolean concluida) {
        ContentValues valores = new ContentValues();
        valores.put("descricao", descricao);
        valores.put("concluida", concluida ? 1 : 0);

        db.insert("atividade", null, valores);
    }


    public List<Atividade> listarAtividades() {
        List<Atividade> atividades = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM atividade", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                int concluidaInt = cursor.getInt(cursor.getColumnIndexOrThrow("concluida"));

                boolean concluida = (concluidaInt == 1);

                Atividade atividade = new Atividade(id, descricao, concluida);
                atividades.add(atividade);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return atividades;
    }


    public void atualizarAtividade(int id, String novaDescricao) {
        ContentValues valores = new ContentValues();
        valores.put("descricao", novaDescricao);

        db.update("atividade", valores, "id = ?", new String[]{String.valueOf(id)});
    }

    public void marcarComoConcluida(int id, boolean concluida) {
        ContentValues valores = new ContentValues();
        valores.put("concluida", concluida ? 1 : 0);

        db.update("atividade", valores, "id = ?", new String[]{String.valueOf(id)});
    }


    public void alternarEstadoConcluida(int id) {
        Cursor cursor = db.rawQuery("SELECT concluida FROM atividade WHERE id = ?", new String[]{String.valueOf(id)});
        boolean concluida = false;

        if (cursor.moveToFirst()) {
            concluida = cursor.getInt(cursor.getColumnIndexOrThrow("concluida")) == 1;
        }
        cursor.close();

        // Alterna o estado
        ContentValues valores = new ContentValues();
        valores.put("concluida", concluida ? 0 : 1);

        db.update("atividade", valores, "id = ?", new String[]{String.valueOf(id)});
    }


}
