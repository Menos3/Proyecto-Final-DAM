package org.sergio.melado.m13.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.sergio.melado.m13.Main;
import org.sergio.melado.m13.classes.*;
import org.sergio.melado.m13.enums.Modes;
import org.sergio.melado.m13.models.Usuari;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministracioUsuarisController extends DefaultDBControllerUsuari implements Initializable {

    //primero declaro todas las variables que voy a hacer servir
    @FXML
    private TableView<Usuari> tvUsuari;
    @FXML
    private TableColumn<Usuari, String> tcLogin;
    @FXML
    private TableColumn<Usuari, String> tcNom;
    @FXML
    private TableColumn<Usuari, String> tcCognoms;
    @FXML
    private TableColumn<Usuari, String> tcEmail;
    @FXML
    private TableColumn<Usuari, String> tcTelefon;
    @FXML
    private TableColumn<Usuari, String> tcAdreca;
    @FXML
    private TableColumn<Usuari, String> tcCodiPostal;
    @FXML
    private TableColumn<Usuari, String> tcPoblacio;
    @FXML
    private TableColumn<Usuari, String> tcPais;
    @FXML
    private TableColumn<Usuari, Boolean> tcBloquejat;
    @FXML
    private TextField tfBuscador;

    private DAOHelper<Usuari> helper;

    /**
     *
     * @param location
     * @param resources
     *
     * Metode que s'encarrega de fer un inici diferit. Se li posa un EventFilter al TableView per detectar si se li fa doble click
     * sobre una fila. Aquest EventFilter fara que s'obri el formulari de Personalitzacio en mode modificar i cambiara el estat d'usuari
     * bloquejat de true a false o al reves.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configurarColumnes();

        tvUsuari.setRowFactory(tv -> {
            TableRow<Usuari> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == Constants.DOUBLE_CLICK && (!row.isEmpty())) {
                    Usuari u = tvUsuari.getSelectionModel().getSelectedItem();
                    if (!u.isAdmin()) {
                        u.setBloquejat(!u.isBloquejat());
                        u.setIntents(0);
                        helper.update(u);
                        tvUsuari.refresh();

                        if (u.isBloquejat()) {
                            GMailHelper.send(u.getEmail(), AppConfigSingleton.getInstancia().getAssumpteMail(), Constants.COS_CORREU_BLOQUEIG_ADMIN);
                        }

                    }
                }
            });

            return row;
        });
    }

    /**
     * Metode d'inici diferit. Aquest s'encarrega de la connexio a la base de dades abans que es carregui e formulari.
     */
    @Override
    public void inicia() {

        helper = new DAOHelper<>(getEmf(), Usuari.class);
        actualizarTaula();
        goTableItem(0);

    }

    /**
     *
     * @param fila fila del TableView
     *
     * Metode que s'encarrega de fer focus a la fila que se li passi per parametre
     */
    private void goTableItem(int fila) {
        tvUsuari.requestFocus();
        tvUsuari.scrollTo(fila);
        tvUsuari.getSelectionModel().select(fila);
        tvUsuari.getFocusModel().focus(fila);
    }

    /**
     * Metode que s'encarrega de fer un SELECT a la base de dades i carregar tots els registres en una ObservableList
     */
    private void actualizarTaula() {
        tvUsuari.setItems(helper.getAllToObservableList());
        filtrarTaula();
    }

    /**
     * Metode que s'encarrega de filtrar la taula per el valor que posem en el TextField de filtratge
     */
    private void filtrarTaula() {

        FilteredList<Usuari> filteredData = new FilteredList<>(tvUsuari.getItems(), p -> true);

        tfBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(usuari -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (usuari.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (usuari.getNom().contains(lowerCaseFilter)) {
                    return true;
                }else {
                    return false;
                }
            });
        });

        SortedList<Usuari> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tvUsuari.comparatorProperty());

        tvUsuari.setItems(sortedData);
    }

    /**
     * Metode que s'encarrega de configurar les columnes del TableView, indicant-li a cada cel·la el camp de la base de dades a la
     * que es referencia i indicant-li que aparegui un CheckBox on hi hagi un boolean
     */
    private void configurarColumnes() {

        tcLogin.setCellValueFactory(new PropertyValueFactory<>("username"));
        tcNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tcCognoms.setCellValueFactory(new PropertyValueFactory<>("cognom"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        tcAdreca.setCellValueFactory(new PropertyValueFactory<>("adreca"));
        tcCodiPostal.setCellValueFactory(new PropertyValueFactory<>("codi_postal"));
        tcPoblacio.setCellValueFactory(new PropertyValueFactory<>("poblacio"));
        tcPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        tcBloquejat.setCellFactory(column -> new CheckBoxTableCell());

        tcBloquejat.setCellValueFactory(dadesCela -> {
            Usuari valorCela = dadesCela.getValue();
            BooleanProperty property = new SimpleBooleanProperty();
            property.setValue(valorCela.isBloquejat());
            return property;
        });
    }

    /**
     * Metode que s'encarrega de cridar al formulari de Personalitzacio en mode modificar
     */
    private void accionsPerModificar() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/personalitzacio_usuari.fxml"));
            Parent root = loader.load();
            PersonalitzacioUsuariController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setMode(Modes.CERCA);
            controller.setUsuari(tvUsuari.getSelectionModel().getSelectedItem());
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle("Modifica Usuari");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    actualizarTaula();
                }
            });
            stage.show();

        }catch (IOException ioe) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ioe);
        }
    }

    /**
     * @param event
     *
     * Metode que s'encarrega de controlar quan se li fa clic al boto de Sortir. Aquest tencara el formulari.
     */
    @FXML
    public void btSalirOnAction(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * @param event
     *
     * Metode que s'encarrega de cotrolar quan se li fa clic al boto de Modificar. Aquest crida al metode accionsPerModificar().
     */
    @FXML
    public void btModificarUsuariOnAction(ActionEvent event) {

        accionsPerModificar();
    }

    /**
     * @param event
     *
     * Metode que s'encarrega de controlar quan se li fa clic al boto de Eliminar. Aquest preguntara si es vol eliminar el usuari amb
     * tal nom i si se li fa clic a SI, aquest sera eliminat de la base de dades.
     */
    @FXML
    public void btEliminarUsuariOnAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Constants.TITOL_ADMINSTRACIO_USUARIS);
        alert.setHeaderText(Constants.CONFIRMAR_ACCIO_ELIMINAR_USUARI);
        alert.setContentText(Constants.SEGUR_QUE_VOLS_ELIMINAR_USUARI + "\n\n " +
                "[" + tvUsuari.getSelectionModel().getSelectedItem().getUsername() + "]");

        ButtonType btNo = new ButtonType(Constants.BTN_NO);
        ButtonType btSi = new ButtonType(Constants.BTN_SI);
        alert.getButtonTypes().setAll(btNo, btSi);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btSi) {
            int itemActual = tvUsuari.getSelectionModel().getSelectedIndex();
            helper.delete(tvUsuari.getSelectionModel().getSelectedItem().getId());
            actualizarTaula();
            if (itemActual > 0) {
                goTableItem(itemActual -1);
            }else {
                goTableItem(0);
            }
        }
    }

    @FXML
    public void btNouUsuariOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/personalitzacio_usuari.fxml"));
            Parent root = loader.load();
            PersonalitzacioUsuariController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setMode(Modes.ALTA);
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle("Nou Usuari");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    actualizarTaula();
                }
            });
            stage.show();

        }catch (IOException ioe) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ioe);
        }
    }
}
