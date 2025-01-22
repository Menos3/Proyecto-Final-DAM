package org.sergio.melado.m13.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.sergio.melado.m13.Main;
import org.sergio.melado.m13.classes.*;
import org.sergio.melado.m13.enums.LoginStates;
import org.sergio.melado.m13.models.Usuari;
import org.sergio.melado.m13.utils.DBUtils;
import org.sergio.melado.m13.utils.Utilitats;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends DefaultController implements Initializable {

    @FXML
    private TextField tfUsuari;

    @FXML
    private TextField tfPassword;

    private boolean tanca;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfUsuari.clear();
        tfPassword.clear();
        tfUsuari.requestFocus();

    }

    /**
     *
     * @param mouseEvent event de ratolí que detecta quan es fa clic
     *
     * Aquesta funcio es l'esdeveniment quan es fa clic en el botó de Sign In
     */
    @FXML
    public void btSignInOnMouseClicked(MouseEvent mouseEvent) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {

        tanca = login(tfUsuari.getText(), tfPassword.getText());

        if (tanca) {

            ((Stage) ((Button) mouseEvent.getSource()).getScene().getWindow()).close();
        }

    }

    @FXML
    public void btSurtOnMouseClicked(MouseEvent mouseEvent) {
        Platform.exit();
    }

    /**
     *
     * @param keyEvent esdeveniment que detecta la pulsació d'una tecla
     *
     * Aquesta funcio detecta quan es prem la tecla Enter en el TextField d'introducció del usuari i fa un focus al TextField de la contrasenya
     */
    @FXML
    public void tfUsuariSetOnKeyPressed(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.ENTER) {
            tfPassword.requestFocus();
        }
    }

    /**
     *
     * @param keyEvent esdeveniment que detecta la pulsació d'una tecla
     *
     * Aquesta funcio detecta quan es prem la tecla Enter en el TextField de la contrasenya, comprova el login i tenca la finestra si el login es validat
     */
    @FXML
    public void tfPasswordSetOnKeyPressed(KeyEvent keyEvent) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        if (keyEvent.getCode() == KeyCode.ENTER && !tfUsuari.getText().isEmpty()) {
            tanca = login(tfUsuari.getText(), tfPassword.getText());

            if (tanca) {

                ((Stage) ((TextField) keyEvent.getSource()).getScene().getWindow()).close();
            }

        }
    }

    /**
     *
     * @param tfUsuariText text que se li passa a través del TextField Usuari
     * @param tfPasswordText text que se li passa a través del TextField Password
     *
     * Aquesta funcio s'encarrega de validar el login a través del usuari i contrasenya proporcionats per un usuari i comprova diferents casos:
     * que els TextFiels estiguin en blanc, que un dels dos estigui en blanc, que la contrasenya sigui incorrecta i que despres d'un numero de X intents
     * es bloqueja l'usuari
     */
    private boolean login(String tfUsuariText, String tfPasswordText) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        if(tfUsuariText.isEmpty()) {

            Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_LOGIN, Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);

        }else {

            if(tfPasswordText.isEmpty ()) {
                Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_LOGIN, Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);

            } else {

                EncryptHelper eh = new EncryptHelper();
                String contrasenyaEncriptada = eh.AESEncrypt(tfPasswordText, true);
                DBUtils dbUtils = new DBUtils(this.getEmf());
                LoginStates states = dbUtils.checkPassword(tfUsuariText, contrasenyaEncriptada);
                UsuariHelper usuariHelper = new UsuariHelper(this.getEmf(), Usuari.class);
                Usuari u = usuariHelper.getUsuariByUsername(tfUsuariText);

                switch (states) {

                    case GRANTED:
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));

                        try {
                            Parent root = loader.load();
                            DefaultDBControllerUsuari controller = loader.getController();
                            controller.setEmf(this.getEmf());
                            controller.setUsuari(u);
                            Stage stage = new Stage();
                            Scene scene = new Scene(root);
                            stage.initStyle(StageStyle.DECORATED);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setTitle("Menu Principal");
                            stage.setScene(scene);
                            controller.inicia();
                            stage.show();
                            return true;

                        } catch (IOException ex) {

                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;
                    case FAILED:
                        Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_LOGIN, Constants.CAPCALERA_ALERTA_DADES_INCORRECTES, "Usuari i/o contrasenya no valids, recordi que te "+(AppConfigSingleton.getInstancia().getIntents() - u.getIntents())+" intents per a entrar al sistema, despres d'aixo el seu usuari sera bloquejat.");
                        break;
                    case LOCKED:
                        Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_LOGIN, Constants.CAPCALERA_ALERTA_BLOQUEIG, Constants.MISSATGE_ALERTA_BLOQUEIG);
                        GMailHelper.send(u.getEmail(), AppConfigSingleton.getInstancia().getAssumpteMail(), AppConfigSingleton.getInstancia().getCosMail());
                        break;

                    case NOEXIST:
                        Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_LOGIN, Constants.CAPCALERA_ALERTA_DADES_INCORRECTES, states.getMessage());
                        break;
                }

            }
        }

        return false;
    }
}

