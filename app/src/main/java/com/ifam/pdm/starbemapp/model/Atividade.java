package com.ifam.pdm.starbemapp.model;

public class Atividade {

    private int id;
    private String descricao;
    private boolean concluida;  // Agora é um booleano

    public Atividade(int id, String descricao, boolean concluida) {
        this.id = id;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    // Método para atualizar o valor de 'concluida'
    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}

