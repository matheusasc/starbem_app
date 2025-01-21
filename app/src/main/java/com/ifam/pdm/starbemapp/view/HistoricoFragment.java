package com.ifam.pdm.starbemapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.ifam.pdm.starbemapp.R;
import com.ifam.pdm.starbemapp.model.Atividade;
import com.ifam.pdm.starbemapp.model.AtividadeDao;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class HistoricoFragment extends Fragment {

    private PieChartView pieChart;

    private AtividadeDao atividadeDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        atividadeDao = new AtividadeDao(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        pieChart = view.findViewById(R.id.pieChart);

        atualizarGrafico();

        return view;
    }

    private void atualizarGrafico() {
        // Obtém as atividades do banco de dados
        List<Atividade> atividades = atividadeDao.listarAtividades();

        // Contadores para atividades concluídas e não concluídas
        int atividadesConcluidas = 0;
        int atividadesNaoConcluidas = 0;

        // Percorrer as atividades e contar as concluídas e não concluídas
        for (Atividade atividade : atividades) {
            if (atividade.isConcluida()) {
                atividadesConcluidas++;
            } else {
                atividadesNaoConcluidas++;
            }
        }

        // Calcular as porcentagens
        float total = atividadesConcluidas + atividadesNaoConcluidas;
        float concluidoPercent = (total == 0) ? 0 : (atividadesConcluidas / total) * 100;
        float naoConcluidoPercent = 100 - concluidoPercent;

        // Criar os dados para o gráfico
        List<SliceValue> slices = new ArrayList<>();
        slices.add(new SliceValue(concluidoPercent, getResources().getColor(R.color.green)) // Porcentagem de atividades concluídas
                .setLabel("Concluídas " + concluidoPercent + "%"));
        slices.add(new SliceValue(naoConcluidoPercent, getResources().getColor(R.color.red)) // Porcentagem de atividades não concluídas
                .setLabel("Não Concluídas " + naoConcluidoPercent + "%"));

        // Criar o objeto PieChartData com os dados
        PieChartData pieChartData = new PieChartData(slices);
        pieChart.setPieChartData(pieChartData);
    }


}
