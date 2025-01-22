package org.sergio.melado.m13.controllers;

import com.sothawo.mapjfx.Projection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.sergio.melado.m13.classes.AppConfigSingleton;
import org.sergio.melado.m13.classes.Constants;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.classes.DefaultDBControllerUsuari;
import org.sergio.melado.m13.enums.TipusMapa;
import org.sergio.melado.m13.models.Config;
import org.sergio.melado.m13.utils.Utilitats;

import java.util.List;

public class ConfigController extends DefaultDBControllerUsuari {

    @FXML
    private ComboBox<String> cbAnotacioMapa;
    @FXML
    private TextField tfNumIntents;
    @FXML
    private TextField tfCaractersContrasenya;
    @FXML
    private TextField tfAssumpteCorreu;
    @FXML
    private TextArea taCosCorreu;
    @FXML
    private CheckBox cbEnableSsl;
    @FXML
    private TextField tfInfluxDBHost;
    @FXML
    private TextField tfInfluxDBPort;
    @FXML
    private TextField tfInfluxDBUsuari;
    @FXML
    private PasswordField pfInfluxDBPassword;

    private boolean update = true;
    DAOHelper<Config> helper;
    private Config config;
    ObservableList<String> llistaAnotacions = FXCollections.observableArrayList(TipusMapa.AMERICANA.getNom(), TipusMapa.EUROPEA.getNom());

    /**
     * Metode que s'utilitza per carregar la connexio a la base de dades, consultar el registre de configuracio i carregar-lo
     * Si no hi ha cap registre en la base de dades, el formulari passa a mode d'alta per omplir els camps i guardar-los
     */
    public void inicia () {

        helper = new DAOHelper<>(this.getEmf(), Config.class);
        List<Config> llista = helper.getAll();
        cbAnotacioMapa.setValue(llistaAnotacions.get(0));
        cbAnotacioMapa.setItems(llistaAnotacions);

        if (llista != null && !llista.isEmpty()) {

            config = llista.get(0);
            tfNumIntents.setText(String.valueOf(config.getIntents()));
            tfCaractersContrasenya.setText((String.valueOf(config.getMaxPassword())));
            tfAssumpteCorreu.setText(config.getAssumpteMail());
            taCosCorreu.setText(config.getCosMail() + Utilitats.getIP());

            if (config.getAnotacioMapa() == TipusMapa.AMERICANA.getProjection()) {

                cbAnotacioMapa.setValue(TipusMapa.AMERICANA.getNom());

            }else {

                cbAnotacioMapa.setValue(TipusMapa.EUROPEA.getNom());
            }

            tfInfluxDBHost.setText(config.getInfluxdbHost());
            tfInfluxDBPort.setText(String.valueOf(config.getInfluxdbPort()));
            tfInfluxDBUsuari.setText(config.getInfluxdbUser());
            pfInfluxDBPassword.setText(config.getInfluxdbPassword());
            cbEnableSsl.setSelected(config.isInfluxdbSsl());

        } else {

            update = false;
        }

    }

    /**
     * @param event Parametre refernciat a fer clic en el boto
     *
     * Metode que s'encarrega de fer el INSERT o el UPDATE a la base de dades (depenent si hi ha un registre existent o no)
     * Els camps buits es gestionen amb una alerta avisant que certs camps no poden estar buits
     */

    @FXML
    public void btGuardarConfigOnAction(ActionEvent event) {

        if(tfNumIntents.getLength() == 0 || tfCaractersContrasenya.getLength() == 0 || tfAssumpteCorreu.getLength() == 0 || taCosCorreu.getLength() == 0
            || cbAnotacioMapa.getSelectionModel().getSelectedItem() == null || tfInfluxDBHost.getLength() == 0 || tfInfluxDBPort.getLength() == 0
            || tfInfluxDBUsuari.getLength() == 0 || pfInfluxDBPassword.getLength() == 0) {

            Utilitats.alertaGeneralWarning(Constants.TITOL_CONFIGURACIO,Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);

        }else {

            if (update){

                config.setIntents(Integer.parseInt(tfNumIntents.getText()));
                config.setMaxPassword(Integer.parseInt(tfCaractersContrasenya.getText()));
                config.setAssumpteMail(tfAssumpteCorreu.getText());
                config.setCosMail(taCosCorreu.getText() + Utilitats.getIP());

                if (cbAnotacioMapa.getSelectionModel().getSelectedItem().equals(TipusMapa.AMERICANA.getNom())) {

                    config.setAnotacioMapa(Projection.WGS_84);

                }else {

                    config.setAnotacioMapa(Projection.WEB_MERCATOR);
                }

                config.setInfluxdbHost(tfInfluxDBHost.getText());
                config.setInfluxdbPort(Integer.parseInt(tfInfluxDBPort.getText()));
                config.setInfluxdbUser(tfInfluxDBUsuari.getText());
                config.setInfluxdbPassword(pfInfluxDBPassword.getText());
                config.setInfluxdbSsl(cbEnableSsl.isSelected());

                helper.update(config);

            } else {

                Config c = new Config();
                c.setIntents(Integer.parseInt(tfNumIntents.getText()));
                c.setMaxPassword(Integer.parseInt(tfCaractersContrasenya.getText()));
                c.setAssumpteMail(tfAssumpteCorreu.getText());
                c.setCosMail(taCosCorreu.getText() + Utilitats.getIP());

                if (cbAnotacioMapa.getSelectionModel().getSelectedItem().equals(TipusMapa.AMERICANA.getNom())) {

                    c.setAnotacioMapa(Projection.WGS_84);

                }else {

                    c.setAnotacioMapa(Projection.WEB_MERCATOR);
                }

                c.setInfluxdbHost(tfInfluxDBHost.getText());
                c.setInfluxdbPort(Integer.parseInt(tfInfluxDBPort.getText()));
                c.setInfluxdbUser(tfInfluxDBUsuari.getText());
                c.setInfluxdbPassword((pfInfluxDBPassword.getText()));
                c.setInfluxdbSsl(cbEnableSsl.isSelected());

                try {

                    helper.insert(c);

                }catch (Exception e){

                    e.printStackTrace();
                }

                AppConfigSingleton.getInstancia().setIntents(Integer.parseInt(tfNumIntents.getText()));
                AppConfigSingleton.getInstancia().setMaxPassword(Integer.parseInt(tfCaractersContrasenya.getText()));
                AppConfigSingleton.getInstancia().setAssumpteMail(tfAssumpteCorreu.getText());
                AppConfigSingleton.getInstancia().setCosMail(taCosCorreu.getText() + Utilitats.getIP());

                if (cbAnotacioMapa.getSelectionModel().getSelectedItem().equals(TipusMapa.AMERICANA.getNom())) {

                    AppConfigSingleton.getInstancia().setAnotacioMapa(Projection.WGS_84);

                }else {

                    AppConfigSingleton.getInstancia().setAnotacioMapa(Projection.WEB_MERCATOR);
                }

                AppConfigSingleton.getInstancia().setInfluxDBHost(tfInfluxDBHost.getText());
                AppConfigSingleton.getInstancia().setInfluxDBPort(Integer.parseInt(tfInfluxDBPort.getText()));
                AppConfigSingleton.getInstancia().setInfluxDBUser(tfInfluxDBUsuari.getText());
                AppConfigSingleton.getInstancia().setInfluxDBPassword(pfInfluxDBPassword.getText());
                AppConfigSingleton.getInstancia().setInfluxDBSsl(cbEnableSsl.isSelected());

                update = true;
            }

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        }
    }
}
