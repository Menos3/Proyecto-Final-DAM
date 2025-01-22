package org.sergio.melado.m13.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.sergio.melado.m13.classes.*;
import org.sergio.melado.m13.models.Usuari;
import org.sergio.melado.m13.utils.Utilitats;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class CanviContrasenyaController extends DefaultDBControllerUsuari implements Initializable {

    @FXML
    PasswordField pfContrasenyaActual;

    @FXML
    PasswordField pfNovaContrasenya;

    @FXML
    PasswordField pfConfirmaContrasenya;

    private Usuari usuari;
    private DAOHelper<Usuari> helper;
    private EncryptHelper eh = new EncryptHelper();

    @Override
    public void inicia() {
        helper = new DAOHelper<>(getEmf(), Usuari.class);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     *
     * @param actionEvent Si accepta el nou password, tanca la finestra
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    @FXML
    public void btConfirmarCanvisOnAction(ActionEvent actionEvent) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
//        String pass;
//        pass=eh.AESEncrypt(tfContrasenyaActual.getText());
        if (passwordAccepted())
            GMailHelper.send(getUsuari().getEmail(), Constants.ASSUMPTE_CORREU_CONTRASENYA, Constants.COS_CORREU_CONTRASENYA + pfNovaContrasenya.getText());
        ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow()).close();
    }


    /**
     *
     * @param mouseEvent Tanca la finestra
     */
    @FXML
    public void btCancelarOnAction(ActionEvent mouseEvent) {
        ((Stage) ((Button) mouseEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     *
     * @return El paràmetre return retorna un valor booleà (true o false).
     * Primer agafa el password actual i l'encripta.
     * A continuació, procedeix a comprovar una serie de condicions.
     * Si la funció testOldPassword cumpleix les condicions, vol dir que tot es correcte i saltarà a la segúent condició.
     * Si la funció testNewPassword cumpleix les condicions, executarà la funció actualitzaBD on li passem la confirmació de la contrasenya.
     * Si alguna de les condicions no es cumpleix, el valor booleà canviarà a false i el programa petarà
     *
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private boolean passwordAccepted() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        //actualitzaBD(pfConfirmaContrasenya.getText());
        String oldPassEnc = eh.AESEncrypt(pfContrasenyaActual.getText(), true);

        if (testOldPassword(oldPassEnc)) {
            if (testNewPassword()) {
                actualitzaBD(pfConfirmaContrasenya.getText());
            } else
                return false;
        } else
            return false;
        return true;
    }

    /**
     *
     * @return El paràmetre return retorna un valor booleà (true o false)
     *  Per defecte el valor booleà esta definit com a true dins la variable result.
     *  Seguidament s'han de complir unes condicions per a que el valor booleà no canvii a false.
     *
     *  Condició 1 --> La nova contrasenya no pot estar buida, de lo contrari, el booleà canviarà a false i saltarà una alerta.
     *  Condició 2 --> La confirmació de la contrasenya estar buida, de lo contrari, el booleà canviarà a false i saltarà una alerta.
     *  Condició 3 --> La contrasenya actual, la nova contrasenya i la confirmació de la nova contrasenya han de tenir menys o igual longitud que la especificada per constant.
     *  Condició 4 --> La nova contrasenya i la confirmació de la nova contrasenya han de coincidir, de lo contrari, el booleà canviarà a false i saltarà una alerta.
     */
    private boolean testNewPassword() {
        boolean result = true;
        if (pfNovaContrasenya.getText().isEmpty()) {
            Utilitats.alertaGeneralWarning(Constants.TITOL_CANVI_CONTRASENYA, Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);
            result = false;
        } else if (pfConfirmaContrasenya.getText().isEmpty()) {
            Utilitats.alertaGeneralWarning(Constants.TITOL_CANVI_CONTRASENYA, Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);
            result = false;
        } else if (pfContrasenyaActual.getLength() <= AppConfigSingleton.getInstancia().getMaxPassword()|| pfNovaContrasenya.getLength() <= AppConfigSingleton.getInstancia().getMaxPassword()
                || pfConfirmaContrasenya.getLength() <= AppConfigSingleton.getInstancia().getMaxPassword()) {
            Utilitats.alertaGeneralWarning(Constants.TITOL_CANVI_CONTRASENYA, Constants.CAPCALERA_ALERTA_LONGITUD, "La longitud minima ha de ser de" + AppConfigSingleton.getInstancia().getMaxPassword());
            result = false;
        } else if (!pfNovaContrasenya.getText().equals(pfConfirmaContrasenya.getText())) {
            Utilitats.alertaGeneralWarning(Constants.TITOL_CANVI_CONTRASENYA, Constants.CAPCALERA_ALERTA_CONTRASENYES_DIFERENTS, Constants.MISSATGE_CONTRASENYES_DIFERENTS);
            result = false;
        }
        return result;
    }

    /**
     *
     * @param contrasenyaEncriptada Li passem la contrasenya actual de l'usuari encriptada.
     * Si la contrasenya encriptada esta buida saltarà l'alerta de que no pot estar buida.
     * Si la contrasenya encriptada no coincideix amb la contrasenya encriptada de l'usuari de la BBDD, saltarà una alerta.
     *
     * @return Retorna un resultat true o false depenent de les condicions especificades anteriorment
     *  Si retorna true, s'executarà la funció. Si retorna false, el programa petarà.
     */
    private boolean testOldPassword(String contrasenyaEncriptada) {
        boolean result = true;
        if (contrasenyaEncriptada.isEmpty()) {
            Utilitats.alertaGeneralWarning(Constants.TITOL_CANVI_CONTRASENYA, Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);

        } else if (!contrasenyaEncriptada.equals(usuari.getPasswd())) {
            Utilitats.alertaGeneralWarning(Constants.TITOL_CANVI_CONTRASENYA, Constants.CAPCALERA_ALERTA_CONTRASENYES_DIFERENTS, Constants.MISSATGE_CONTRASENYES_NO_COINCIDEIXEN);
            result = false;
        }
        return result;
    }

    /**
     *
     * @param contrasenya Li passem la nova contrasenya confirmada que ha introduit l'usuari però sense encriptar.
     *  Si el password de l'usuari de la base de dades és diferent a la nova contrasenya.
     *  Agafa la nova contrasenya, la encripta i la defineix com a nou password de l'usuari.
     *  Finalment, s'actualitza la contrasenya encriptada de la BBDD per la nova.
     *  En cas d'error, saltarà una alerta.
     */
    private void actualitzaBD(String contrasenya) {
        String pass;
        try {
            Usuari u = this.usuari;
            if (!usuari.getPasswd().equals(contrasenya)) {
                pass = eh.AESEncrypt(contrasenya, true);
                u.setPasswd(pass);
                helper.update(u);

            } else {
                Utilitats.alertaGeneralWarning("ERROR", "ERROR", "La contrasenya es incorrecta");
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
}
