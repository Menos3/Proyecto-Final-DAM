package org.sergio.melado.m13.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.sergio.melado.m13.classes.Constants;
import org.sergio.melado.m13.classes.DefaultDBControllerUsuari;
import org.sergio.melado.m13.classes.DefaultDBControllerUsuariModes;
import org.sergio.melado.m13.enums.Modes;

import java.io.IOException;
import java.util.Optional;

public class MainController extends DefaultDBControllerUsuari {

    @FXML
    MenuItem miAdministracioUsuaris, miConfiguracio;

    @Override
    public void inicia() {
        miAdministracioUsuaris.setVisible(this.getUsuari().isAdmin());
        miConfiguracio.setVisible(this.getUsuari().isAdmin());
    }

    @FXML
    public void miConfiguracioOnAction(ActionEvent event) {
        cargarFormulariosNormales("/views/config.fxml", "Configuracio del sistema", false);
    }

    @FXML
    public void miAdministracioUsuarisOnAction(ActionEvent event) {
        cargarFormulariosNormales("/views/administracio_usuaris.fxml", "Administracio d'Usuaris", false);
    }

    @FXML
    public void miPersonalitzacioOnAction(ActionEvent event) {
        cargarFormulariosConModo("/views/personalitzacio_usuari.fxml", Modes.CERCA, "Personalitzacio del usuari", true);
    }

    public void miAdministracioDispositiusOnAction(ActionEvent actionEvent) {
        cargarFormulariosNormales("/views/administracio_dispositius.fxml", "Administracio de Dispositius", false);
    }

    @FXML
    public void miSurtOnAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Constants.TITOL_ALERTA_SORTIR);
        alert.setHeaderText(Constants.CAPCALERA_ALERTA_SORTIR);
        alert.setContentText(Constants.MISSATGE_ALERTA_SORTIR);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //L'usuari fa clic al boto OK
            Platform.exit();
        }
    }

    @FXML
    public void miSobreAppOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/sobreapp.fxml"));
        try {
            Parent root = loader.load();
            //getclass i get resorce son les rutes on ha d'anar a buscar quan crei l'executable. l'exectutable ho buscarà dintre del .jar
            Stage stage = new Stage();
            stage.setTitle("Sobre la App...");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable (false);  //  ------> NO AMPLIAR PANTALLA
            stage.show();//mostra les tres línies anteriors
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void cargarFormulariosNormales(String path, String title, boolean onCloseRequest) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent root = loader.load();
            //getclass i get resorce son les rutes on ha d'anar a buscar quan crei l'executable. l'exectutable ho buscarà dintre del .jar
            DefaultDBControllerUsuari controller = loader.getController();
            controller.setEmf(this.getEmf());
            controller.setUsuari(getUsuari());
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene (root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable (false);  //  ------> NO AMPLIAR PANTALLA
            if (onCloseRequest) {
                stage.setOnCloseRequest (Event::consume);
            }
            stage.show();//mostra les tres línies anteriors
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void cargarFormulariosConModo(String path, Modes mode, String title, boolean onCloseRequest) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent root = loader.load();
            //getclass i get resorce son les rutes on ha d'anar a buscar quan crei l'executable. l'exectutable ho buscarà dintre del .jar
            DefaultDBControllerUsuariModes controller = loader.getController();
            controller.setEmf(this.getEmf());
            controller.setMode(mode);
            controller.setUsuari(getUsuari());
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene (root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable (false);  //  ------> NO AMPLIAR PANTALLA
            if (onCloseRequest) {
                stage.setOnCloseRequest (event -> event.consume ());
            }
            stage.show();//mostra les tres línies anteriors
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
