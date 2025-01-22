package org.sergio.melado.m13.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.sergio.melado.m13.classes.*;
import org.sergio.melado.m13.models.DispositiuAdaptat;
import org.sergio.melado.m13.models.DummyData;
import org.sergio.melado.m13.utils.Utilitats;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class MonitoritzacioDispositiusController extends DefaultDBControllerDispositiuUsuariModes {

    @FXML
    private DatePicker dpDesDe;
    @FXML
    private DatePicker dpFinsA;
    @FXML
    private TextField tfDesDeHora;
    @FXML
    private TextField tfFinsAHora;
    @FXML
    private Slider slRegistresGraficaDates;
    @FXML
    private LineChart<String, Double> lcGraficaDates;
    @FXML
    private TextField tfNombreRegistresGraficaDates;
    @FXML
    private Slider slRegistresGraficaTempsReal;
    @FXML
    private TextField tfNombreRegistresGraficaTempsReal;
    @FXML
    private Slider slTempsActualitzacio;
    @FXML
    private TextField tfNombreMinuts;
    @FXML
    private LineChart<String, Double> lcGraficaTempsReal;
    @FXML
    private Button btGenerarGraficaTempsReal;

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private int comptador = 0;
    private ScheduledFuture scheduledFuture;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void inicia() {

        slRegistresGraficaDates.valueProperty().addListener((observable, oldValue, newValue) -> tfNombreRegistresGraficaDates.setText(String.valueOf((int)Math.round((Double) newValue))));
        slRegistresGraficaTempsReal.valueProperty().addListener(((observable, oldValue, newValue) -> tfNombreRegistresGraficaTempsReal.setText(String.valueOf((int)Math.round((Double) newValue)))));
        slTempsActualitzacio.valueProperty().addListener((observable, oldValue, newValue) -> tfNombreMinuts.setText(String.valueOf((int)Math.round((Double) newValue))));
    }

    @FXML
    public void btObtenirDadesOnAction(ActionEvent actionEvent) {

        if (testAlertesGraficaEntreDuesDates()) {

            int nombreRegistres = (int) slRegistresGraficaDates.getValue();

            LocalTime horaDesde = LocalTime.parse(tfDesDeHora.getText());
            LocalTime horaFinsA = LocalTime.parse(tfFinsAHora.getText());

            String desde = format.format(LocalDateTime.of(dpDesDe.getValue(), horaDesde));
            String finsA = format.format(LocalDateTime.of(dpFinsA.getValue(), horaFinsA));

            List<DummyData> dummyDataList = getDades(desde, finsA, nombreRegistres);

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

                lcGraficaDates.getData().clear();
                lcGraficaDates.setCreateSymbols(false);
                lcGraficaDates.getData().addAll(temperatura, humitat);

            } else {

                Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_LLISTA_BUIDA, Constants.CAPCALERA_ALERTA_LLISTA_BUIDA, Constants.MISSATGE_ALERTA_LLISTA_BUIDA);
            }
        }

    }

    public void btGenerarInformeOnAction(ActionEvent actionEvent) {

        if (testAlertesGraficaEntreDuesDates()) {

            LocalTime horaDesde = LocalTime.parse(tfDesDeHora.getText());
            LocalTime horaFinsA = LocalTime.parse(tfFinsAHora.getText());

            String desde = format.format(LocalDateTime.of(dpDesDe.getValue(), horaDesde));
            String finsA = format.format(LocalDateTime.of(dpFinsA.getValue(), horaFinsA));

            Integer idDispositiuSeleccionat = getDispositiu().getId();
            List<DummyData> dummyDataList = getDades(desde, finsA, (int) slRegistresGraficaDates.getValue());
            DispositiuAdaptat dAdaptat = new DispositiuAdaptat();

            dAdaptat.setId(idDispositiuSeleccionat);
            dAdaptat.setDescripcio("Descripcio del dispositiu " + idDispositiuSeleccionat);

            DispositiuReportGenerator generador = new DispositiuReportGenerator(dAdaptat);

            try {

                generador.generateReport(desde, finsA, (int) slRegistresGraficaDates.getValue());

            } catch (IOException e) {

                e.printStackTrace();
            }

            if (dummyDataList != null) {

                dummyDataList.forEach(System.out::println);

            } else {

                System.out.println(Constants.MISSATGE_ALERTA_LLISTA_BUIDA);
            }
        }
    }

    public void btGenerarGraficaTempsRealOnAction(ActionEvent actionEvent) {

        comptador++;

        if (comptador % 2 != 0) {

            if (testAlertesGraficaTempsReal()) {

                int nombreRegistres = (int) slRegistresGraficaTempsReal.getValue();
                LocalTime horaActual = LocalTime.now();
                LocalDate dataActual = LocalDate.now();
                String dataHoraActual = format.format(LocalDateTime.of(dataActual, horaActual));
                List<DummyData> dummyDataList = getDades(dataHoraActual, nombreRegistres);

                try {
                    scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new TascaGenerarGrafica(lcGraficaTempsReal, dummyDataList),
                            0, (long) (slTempsActualitzacio.getValue()), TimeUnit.SECONDS);

                } catch (RejectedExecutionException ree) {

                    Utilitats.alertaGeneralWarning(Constants.TITOL_MONITORITZACIO_DISPOSITIUS, Constants.CAPCALERA_ALERTA_GRAFICA_ATURADA, Constants.MISSATGE_ALERTA_GRAFICA_ATURADA);
                }

                btGenerarGraficaTempsReal.setStyle("-fx-background-image: url('img/btparargraficainfinita.png')");
            }

        } else {

            scheduledExecutorService.schedule(() -> {

                scheduledFuture.cancel(true);
                scheduledExecutorService.shutdown();

            }, 0, TimeUnit.SECONDS);

            btGenerarGraficaTempsReal.setStyle("-fx-background-image: url('img/btgenerargraficainfinita.png')");

            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle(Constants.TITOL_MONITORITZACIO_DISPOSITIUS);
            alertInfo.setHeaderText(Constants.CAPCALERA_INFO_GRAFICA_ATURADA);
            alertInfo.setContentText(Constants.MISSATGE_INFO_GRAFICA_ATURADA);
            alertInfo.getDialogPane().setPrefSize(400, 300);
            alertInfo.showAndWait();

        }

    }

    @FXML
    public void btSortirOnAction(ActionEvent actionEvent) {

        ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow()).close();
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }

    private List<DummyData> getDades(String desde, String finsA, int maxRegistres) {

        InfluxDBHelper influxDBHelper = new InfluxDBHelper();

        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * FROM dummyData WHERE time >= $desde AND time <= $finsA AND counter = $id " + Constants.ORDENATS_PER + " LIMIT " + maxRegistres)
             .forDatabase("iwinedb")
             .bind("desde", desde)
             .bind("finsA", finsA)
                .bind("id", getDispositiu().getId())
             .create();

        QueryResult queryResult = influxDBHelper.query(query);

        influxDBHelper.closeClient();

        if (queryResult.hasError()) {

            return null;

        } else {

            InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
            return resultMapper.toPOJO(queryResult, DummyData.class);
        }
    }

    private List<DummyData> getDades(String dataSistema, int maxRegistres) {

        InfluxDBHelper influxDBHelper = new InfluxDBHelper();

        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * FROM dummyData WHERE counter = $id AND time <= $dataSistema " + Constants.ORDENATS_PER + " LIMIT " + maxRegistres)
                .forDatabase("iwinedb")
                .bind("id", getDispositiu().getId())
                .bind("dataSistema", dataSistema)
                .create();

        QueryResult queryResult = influxDBHelper.query(query);

        influxDBHelper.closeClient();

        if (queryResult.hasError()) {

            return null;

        } else {

            InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
            return resultMapper.toPOJO(queryResult, DummyData.class);
        }
    }

    private boolean testAlertesGraficaEntreDuesDates() {

        if (slRegistresGraficaDates.getValue() == 0) {

            Utilitats.alertaGeneralWarning(Constants.TITOL_MONITORITZACIO_DISPOSITIUS, Constants.CAPCALERA_ALERTA_SLIDER_NULL_REGISTRES, Constants.MISSATGE_ALERTA_SLIDER_NULL_REGISTRES);
            slRegistresGraficaDates.requestFocus();
            return false;

        } else if (dpDesDe.getValue() == null || dpFinsA == null) {

            Utilitats.alertaGeneralWarning(Constants.TITOL_MONITORITZACIO_DISPOSITIUS, Constants.CAPCALERA_ALERTA_DATEPICKERS_BUITS, Constants.MISSATGE_ALERTA_DATEPICKERS_BUITS);
            if (dpDesDe.getValue() == null) {

                dpDesDe.requestFocus();

            } else {

                dpFinsA.requestFocus();
            }

            return false;

        } else if (tfDesDeHora.getText().isEmpty() || tfFinsAHora.getText().isEmpty()) {

            Utilitats.alertaGeneralWarning(Constants.TITOL_MONITORITZACIO_DISPOSITIUS, Constants.CAPCALERA_ALERTA_TEXTFIELD_DATA_BUIT, Constants.MISSATGE_ALERTA_TEXTFIELD_DATA_BUIT);
            if (tfDesDeHora.getText().isEmpty()) {

                tfDesDeHora.requestFocus();

            } else {

                tfFinsAHora.requestFocus();
            }

            return false;

        }

        return true;
    }

    private boolean testAlertesGraficaTempsReal() {

        if (slRegistresGraficaTempsReal.getValue() == 0) {

            Utilitats.alertaGeneralWarning(Constants.TITOL_MONITORITZACIO_DISPOSITIUS, Constants.CAPCALERA_ALERTA_SLIDER_NULL_REGISTRES, Constants.MISSATGE_ALERTA_SLIDER_NULL_REGISTRES);
            slRegistresGraficaTempsReal.requestFocus();
            return false;

        } else if (slTempsActualitzacio.getValue() == 0) {

            Utilitats.alertaGeneralWarning(Constants.TITOL_MONITORITZACIO_DISPOSITIUS, Constants.CAPCALERA_ALERTA_SLIDER_NULL_TEMPS, Constants.MISSATGE_ALERTA_SLIDER_NULL_TEMPS);
            slTempsActualitzacio.requestFocus();
            return false;
        }

        return true;
    }
}