package org.sergio.melado.m13.classes;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.sergio.melado.m13.models.DummyData;
import org.sergio.melado.m13.utils.Utilitats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TascaGenerarGrafica implements Runnable{

    private final LineChart<String, Double> lcGraficaTempsReal;
    private final List<DummyData> dummyDataList;

    public TascaGenerarGrafica(LineChart<String, Double> lcGraficaTempsReal, List<DummyData> dummyDataList) {

        this.lcGraficaTempsReal = lcGraficaTempsReal;
        this.dummyDataList = dummyDataList;
    }

    @Override
    public void run() {

        if (dummyDataList != null && !dummyDataList.isEmpty()) {

            dummyDataList.forEach(System.out::println);
            XYChart.Series<String, Double> temperatura = new XYChart.Series<>();
            XYChart.Series<String, Double> humitat = new XYChart.Series<>();
            int i;

            for (i = 0; i < dummyDataList.size(); i++) {

                Date d = Date.from(dummyDataList.get(i).getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                String formatData = dateFormat.format(d);
                temperatura.getData().add(new XYChart.Data(formatData,dummyDataList.get(i).getTemperatura()));
                humitat.getData().add(new XYChart.Data(formatData, dummyDataList.get(i).getHumetat()));
            }

            Platform.runLater(() -> {

                lcGraficaTempsReal.getData().clear();
                lcGraficaTempsReal.setAnimated(true);
                lcGraficaTempsReal.setCreateSymbols(false);
                lcGraficaTempsReal.getData().addAll(temperatura, humitat);
            });

        } else {

            Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_LLISTA_BUIDA, Constants.CAPCALERA_ALERTA_LLISTA_BUIDA, Constants.MISSATGE_ALERTA_LLISTA_BUIDA);
        }
    }
}
