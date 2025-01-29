package com.ifam.pdm.starbemapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AtividadeDao {

    private SQLiteDatabase db;

    public AtividadeDao(Context context) {
        BDAtividades bdAtividades = new BDAtividades(context);
        db = bdAtividades.getWritableDatabase();
    }

    // Inserir uma nova atividade no banco
    public void inserirAtividade(String descricao, boolean concluida) {
        ContentValues valores = new ContentValues();
        valores.put("descricao", descricao);
        valores.put("concluida", concluida ? 1 : 0);
        db.insert("atividade", null, valores);
    }

    // Listar atividades ordenando para que as concluídas fiquem no final
    public List<Atividade> listarAtividades() {
        List<Atividade> atividades = new ArrayList<>();

        // Agora ordenamos as tarefas: primeiro as não concluídas, depois as concluídas
        Cursor cursor = db.rawQuery("SELECT * FROM atividade ORDER BY concluida ASC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                boolean concluida = cursor.getInt(cursor.getColumnIndexOrThrow("concluida")) == 1;

                Atividade atividade = new Atividade(id, descricao, concluida);
                atividades.add(atividade);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return atividades;
    }

    // Atualizar descrição da atividade
    public void atualizarAtividade(int id, String novaDescricao) {
        ContentValues valores = new ContentValues();
        valores.put("descricao", novaDescricao);
        db.update("atividade", valores, "id = ?", new String[]{String.valueOf(id)});
    }

    // Atualizar estado concluída da atividade
    public void marcarComoConcluida(int id, boolean concluida) {
        ContentValues valores = new ContentValues();
        valores.put("concluida", concluida ? 1 : 0);
        db.update("atividade", valores, "id = ?", new String[]{String.valueOf(id)});
    }

    // Alternar estado concluída/não concluída
    public void alternarEstadoConcluida(int id) {
        Cursor cursor = db.rawQuery("SELECT concluida FROM atividade WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            boolean concluida = cursor.getInt(cursor.getColumnIndexOrThrow("concluida")) == 1;
            marcarComoConcluida(id, !concluida);
        }
        cursor.close();
    }
}
