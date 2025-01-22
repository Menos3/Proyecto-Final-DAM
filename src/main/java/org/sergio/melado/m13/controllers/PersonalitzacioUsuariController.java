package org.sergio.melado.m13.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.sergio.melado.m13.classes.Constants;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.classes.DefaultDBControllerUsuari;
import org.sergio.melado.m13.classes.DefaultDBControllerUsuariModes;
import org.sergio.melado.m13.models.Usuari;
import org.sergio.melado.m13.utils.Utilitats;

import java.io.IOException;

public class PersonalitzacioUsuariController extends DefaultDBControllerUsuariModes {

    //TEXTFIELD ---> CAMPS A OMPLIR
    @FXML
    Label lbTitol;

    @FXML
    TextField tfLogin;

    @FXML
    TextField tfNom;

    @FXML
    TextField tfCognoms;

    @FXML
    TextField tfEmail;

    @FXML
    TextField tfTelefon;

    @FXML
    TextField tfAdreca;

    @FXML
    TextField tfCodiPostal;

    @FXML
    TextField tfPoblacio;

    @FXML
    TextField tfPais;

    @FXML
    Button btCanviContrasenya;

    private DAOHelper<Usuari> helper;
    private boolean tancarFormulari = false;

    /**
     * Metode d'inicia  s'encarrega de carregar la connexio a la base de dades, revent el mode amb el que s'inicia el formulari.
     *
     */
    public void inicia () {

        helper = new DAOHelper<>(getEmf(), Usuari.class);
        switch (getMode()) {
            case CERCA: modificarUsuari(); break;
            case ALTA: altaUsuari(); break;
        }
    }

    /**
     * Metode modificarUsuari  mostra totes les dades del usuari  i s'encarrega de comprobar si els camps no obligatoris estan buits o no.
     * Si es diferent a null es carreguen les dades rebudes. Si es null, el TextField apareix buit.
     */
    private void modificarUsuari() {
        lbTitol.setText("Personalitzacio d'Usuari");
        tfLogin.setText(getUsuari().getUsername());
        tfNom.setText(getUsuari().getNom());
        tfCognoms.setText(getUsuari().getCognom());
        tfEmail.setText(getUsuari().getEmail());

        if (getUsuari().getTelefon () != null) {
            tfTelefon.setText (getUsuari().getTelefon());
        }
        if (getUsuari().getAdreca () != null) {
            tfCodiPostal.setText (getUsuari().getCodiPostal ());
        }
        if (getUsuari().getPoblacio () != null) {
            tfAdreca.setText (getUsuari().getAdreca ());
        }
        if (getUsuari().getCodiPostal () != null) {
            tfPoblacio.setText (getUsuari().getPoblacio ());
        }
        if (getUsuari().getPais () != null) {
            tfPais.setText (getUsuari().getPais ());
        }
    }

    /**
     * Metode altaUsuari s'encarrega de mostrar el titol d'alta usuari, deshabilitem el username i desactivem el canvi de contraseña
     */
    private void altaUsuari() {
        lbTitol.setText("Alta d'Usuari");
        tfLogin.setDisable(false);
        btCanviContrasenya.setDisable(true);
    }

    /**
     *
     * @param usuari usuari que rep a través del login o a través de l'administracio d'usuaris en cas de que sigui cerca i un nou usuari en el
     * cas de alta
     * @return usuari amb les dades plenes
     */
    private Usuari adaptaAltaCerca(Usuari usuari){
        usuari.setUsername(tfLogin.getText());
        usuari.setNom(tfNom.getText());
        usuari.setCognom(tfCognoms.getText());
        usuari.setEmail(tfEmail.getText());
        usuari.setTelefon(tfTelefon.getText());
        usuari.setIntents(usuari.getIntents());
        usuari.setAdreca(tfAdreca.getText());
        usuari.setCodiPostal(tfCodiPostal.getText());
        usuari.setPoblacio(tfPoblacio.getText());
        usuari.setPais(tfPais.getText());

        if (usuari.isAdmin()) {
            usuari.setAdmin(true);
        } else {
            usuari.setAdmin(false);
        }

        usuari.setBloquejat(false);

        return usuari;
    }

    /**
     * Metode testAlertes s'encarrega de comprobar si els camps  obligatoris  son igual a null.
     * Si la dada no cumpleix els criteris de aceptacio, sortira una finestra d'alerta comunicant el error
     * @return  si result es igual a true vol dir que passa les alertas , si es false vol dir que hi ha algun criteri de acceptacio que no cumpleix
     */
    private boolean testAlertes() {
        boolean result = true;
        //COMRPOBA ELS CAMPS OBLIGATORIS
        if (tfLogin.getLength () == 0 || tfNom.getLength () == 0 || tfCognoms.getLength () == 0 || tfEmail.getLength () == 0 ) {
            //EN CAS  ELS CAMPS SIGUIN IGUAL  A  0  --------> MOSTRA L'ALERTA
            Utilitats.alertaGeneralWarning(Constants.TITOL_PERSONALITZACIO_USUARI, Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);
            result = false;
            tfLogin.requestFocus();  // -------> TEXTO RESALTADO

        } else if (!(tfTelefon.getLength () == 9)) {   // SI LA ALLARGADA DEL TELEFON ES DIFERENT  9   --------> MOSTRA L'ALERTA DEL ERROR
            Utilitats.alertaGeneralWarning(Constants.TITOL_PERSONALITZACIO_USUARI, Constants.CAPCALERA_ERROR_TELEFON,Constants.MISSATGE_NUMERO_TELEFON_INCORRECTE+tfTelefon.getLength ());
            result = false;
            tfTelefon.requestFocus();// -------> TEXTO RESALTADO

        } else if(!tfEmail.getText ().contains ("@") || !tfEmail.getText ().contains (".")) {   // SI EL CORREU NO CONTE UNA  @  y un .  -------->  --------> MOSTRA L'ALERTA DEL ERROR
            //Alerta
            Utilitats.alertaGeneralWarning(Constants.TITOL_PERSONALITZACIO_USUARI, Constants.CAPCALERA_CORREU_INCORRECTE, Constants.MISSATGE_CORREU_INCORRECTE);
            result = false;
            tfEmail.requestFocus(); // -------> TEXTO RESALTADO

        }

        return result;

    }

    /**
     * Metode insertDatos s'encarrega de crear un usuari nou amb el que cridem a adaptaAltaCerca.
     * Posem els intents a zero i a continuacio carreguem el formulari nova_contrasenya
     * @return si es true es tanca la finestra si es false no es tanca
     */
    private boolean insertDatos() {
        Usuari u = adaptaAltaCerca(new Usuari());
        u.setIntents(0);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nova_contrasenya.fxml"));
            Parent root = loader.load();
            NovaContrasenyaController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setUsuari(u);
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle("Nova Contrasenya");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            return true;

        }catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Metode updateDatos s'encarrega de rebre les dades de adaptaAltaCerca i fa l'update
     * @return Sempre retorna true perque d'aquesta manera ens assegurem que es tanqui la finestra
     *
     **/
    //RECIBI LOS DATOS DE FUNCION
    private boolean updateDatos() {

        boolean tancar = true;

        adaptaAltaCerca(getUsuari());
        helper.update(getUsuari());
        return tancar;
    }

    @FXML
    public void btGuardarOnAction (ActionEvent event){
        if (testAlertes()) {

            switch (getMode()) {
                case CERCA:
                    tancarFormulari = updateDatos();
                    break;
                case ALTA:
                    tancarFormulari = insertDatos();
                    break;
            }

            if (tancarFormulari) {
                ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
            }
        }
    }

    @FXML
    public void btCanviContrasenyaOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/canvi_contrasenya.fxml"));
        try {
            Parent root = loader.load();
            //getclass i get resorce son les rutes on ha d'anar a buscar quan crei l'executable. l'exectutable ho buscarà dintre del .jar
            DefaultDBControllerUsuari controller = loader.getController();
            controller.setEmf(this.getEmf());
            controller.setUsuari(getUsuari());
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle("Canvi Contrasenya");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable (false);  //  ------> NO AMPLIAR PANTALLA
            stage.setOnCloseRequest (Event::consume);
            stage.show();//mostra les tres línies anteriors
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
