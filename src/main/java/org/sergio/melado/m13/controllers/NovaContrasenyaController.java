package org.sergio.melado.m13.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.classes.DefaultDBControllerUsuari;
import org.sergio.melado.m13.classes.EncryptHelper;
import org.sergio.melado.m13.models.Usuari;

public class NovaContrasenyaController extends DefaultDBControllerUsuari {

    @FXML
    PasswordField pfNovaContrasenya;

    private DAOHelper<Usuari> helper;

    @Override
    public void inicia() {
        helper = new DAOHelper<>(getEmf(), Usuari.class);
    }

    @FXML
    public void btGuardarContrasenyaOnAction(ActionEvent event) {

        try{
            EncryptHelper eh = new EncryptHelper();
            getUsuari().setPasswd(eh.AESEncrypt(pfNovaContrasenya.getText(), true));
            helper.update(getUsuari());
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
