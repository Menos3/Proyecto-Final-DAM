package org.sergio.melado.m13.controllers;

import com.sothawo.mapjfx.Projection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.sergio.melado.m13.Main;
import org.sergio.melado.m13.classes.AppConfigSingleton;
import org.sergio.melado.m13.classes.Constants;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.classes.DefaultDBControllerUsuari;
import org.sergio.melado.m13.enums.Modes;
import org.sergio.melado.m13.enums.TipusMapa;
import org.sergio.melado.m13.models.Dispositiu;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministracioDispositiusController extends DefaultDBControllerUsuari implements Initializable {

    @FXML
    private TableView<Dispositiu> tvDispositius;
    @FXML
    private TableColumn<Dispositiu, Integer> tcIdDispositiu;
    @FXML
    private TableColumn<Dispositiu, String> tcUsuariPropietari;
    @FXML
    private TableColumn<Dispositiu, String> tcDescripcio;
    @FXML
    private TableColumn<Dispositiu, String> tcLatitud;
    @FXML
    private TableColumn<Dispositiu, String> tcLongitud;
    @FXML
    private TextField tfBuscador;

    private DAOHelper<Dispositiu> dispositiuHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configurarColumnes();

        tvDispositius.setRowFactory(tv -> {

            TableRow<Dispositiu> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == Constants.DOUBLE_CLICK && (!row.isEmpty()) && event.getButton() == MouseButton.PRIMARY) {

                    modificarDispositiuSeleccionat();
                }
            });

            return row;
        });
    }

    @Override
    public void inicia() {

        dispositiuHelper = new DAOHelper<>(getEmf(), Dispositiu.class);
        actualitzarTaula(getUsuari().isAdmin());
        goTableItem(0);

    }

    private void goTableItem(int fila) {

        tvDispositius.requestFocus();
        tvDispositius.scrollTo(fila);
        tvDispositius.getSelectionModel().select(fila);
        tvDispositius.getFocusModel().focus(fila);
    }

    private void actualitzarTaula(boolean admin) {

        if (admin) {

            tvDispositius.setItems(dispositiuHelper.getAllToObservableList());

        } else {

            tvDispositius.setItems(dispositiuHelper.getAllToObservableList().filtered(dispositiu -> dispositiu.getUsuari().getNom().equals(getUsuari().getNom())));
        }

        filtrarTaula();
    }

    private void filtrarTaula() {

        FilteredList<Dispositiu> filteredData = new FilteredList<>(tvDispositius.getItems(), p -> true);

        tfBuscador.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(dispositiu -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (dispositiu.getId().toString().contains(lowerCaseFilter)) {

                    return true;

                }else if (dispositiu.getDescripcio().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                } else {

                    return false;
                }
            });
        });

        SortedList<Dispositiu> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tvDispositius.comparatorProperty());

        tvDispositius.setItems(sortedData);
    }

    private void configurarColumnes() {

        tcIdDispositiu.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcUsuariPropietari.setCellValueFactory(new PropertyValueFactory<>("usuari"));

        tcUsuariPropietari.setCellValueFactory(dadesCela -> {
            Dispositiu valorCela = dadesCela.getValue();
            StringProperty property = new SimpleStringProperty();
            property.setValue(valorCela.getUsuari().getNom());
            return property;
        });

        tcDescripcio.setCellValueFactory(new PropertyValueFactory<>("descripcio"));
        tcLatitud.setCellValueFactory(new PropertyValueFactory<>("latitud"));
        tcLongitud.setCellValueFactory(new PropertyValueFactory<>("longitud"));
    }

    private void modificarDispositiuSeleccionat() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/alta_dispositius.fxml"));
            Parent root = loader.load();
            AltaDispositiusController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setMode(Modes.CERCA);
            controller.setUsuari(getUsuari());
            controller.setDispositiu(tvDispositius.getSelectionModel().getSelectedItem());

            if (AppConfigSingleton.getInstancia().getAnotacioMapa() == TipusMapa.AMERICANA.getProjection()) {

                controller.initMapAndControls(Projection.WGS_84);

            }else {

                controller.initMapAndControls(Projection.WEB_MERCATOR);
            }

            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle("Modificar Dispositiu");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    actualitzarTaula(getUsuari().isAdmin());
                }
            });

            stage.show();

        }catch (IOException ioe) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al inciar", ioe);
        }
    }

    public void btNouDispositiuOnAction(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/alta_dispositius.fxml"));
            Parent root = loader.load();
            AltaDispositiusController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setMode(Modes.ALTA);
            controller.setUsuari(getUsuari());

            if (AppConfigSingleton.getInstancia().getAnotacioMapa() == TipusMapa.AMERICANA.getProjection()) {

                controller.initMapAndControls(Projection.WGS_84);

            } else {

                controller.initMapAndControls(Projection.WEB_MERCATOR);
            }

            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle("Nou Dispositiu");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setOnHidden(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    actualitzarTaula(getUsuari().isAdmin());
                }
            });
            stage.showAndWait();

        } catch (IOException ioe) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ioe);
        }

    }

    public void btModificarDispositiuOnAction(ActionEvent actionEvent) {

        modificarDispositiuSeleccionat();

    }

    public void btEliminarDispositiuOnAction(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Constants.TITOL_ADMINISTRACIO_DISPOSITIUS);
        alert.setHeaderText(Constants.CONFIRMAR_ACCIO_ELIMINAR_DISPOSITIU);
        alert.setContentText(Constants.SEGUR_QUE_VOLS_ELIMINAR_DISPOSITIU + "\n\n " +
        "[" + tvDispositius.getSelectionModel().getSelectedItem().getId() + "]");

        ButtonType btNo = new ButtonType(Constants.BTN_NO);
        ButtonType btSi = new ButtonType(Constants.BTN_SI);
        alert.getButtonTypes().setAll(btNo, btSi);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btSi) {

            int dispositiuSeleccionat = tvDispositius.getSelectionModel().getSelectedIndex();
            dispositiuHelper.delete(tvDispositius.getSelectionModel().getSelectedItem().getId());
            actualitzarTaula(getUsuari().isAdmin());

            if (dispositiuSeleccionat > 0) {

                goTableItem(dispositiuSeleccionat - 1);

            } else {

                goTableItem(0);
            }
        }
    }

    public void btMonitoritzarDispositiusOnAction(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/monitoritzacio_dispositius.fxml"));
            Parent root = loader.load();
            MonitoritzacioDispositiusController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setDispositiu(tvDispositius.getSelectionModel().getSelectedItem());
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle("Monitoritzacio de Dispositius");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        }catch (IOException ioe) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ioe);
        }
    }

    public void btSortirOnAction(ActionEvent actionEvent) {
        ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow()).close();
    }
}
