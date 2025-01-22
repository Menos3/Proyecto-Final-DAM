package org.sergio.melado.m13;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.configuration.ConfigurationException;
import org.sergio.melado.m13.classes.*;
import org.sergio.melado.m13.controllers.LoginController;
import org.sergio.melado.m13.models.Config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private EntityManagerFactory emf;
    public static String parameters;

    /**
     *
     * @throws Exception Llança un error amb missatge en cas de que no es pogués iniciar la connexió
     *
     *  Aquesta funció s'encarrega d'iniciar la connexió a la BBDD (MariaDB en aquest cas).
     */
    @Override
    public void init() throws Exception {
        super.init();
        emf = Persistence.createEntityManagerFactory("mariaDB",
                getProperties());

        if (emf != null) {
            actualitzaConfigSingleton();
        }
    }

    private void actualitzaConfigSingleton() {
        DAOHelper<Config> daoHelper = new DAOHelper<>(emf, Config.class);
        Config c = daoHelper.getFirst();
        AppConfigSingleton.getInstancia().setIntents(c.getIntents());
        AppConfigSingleton.getInstancia().setMaxPassword(c.getMaxPassword());
        AppConfigSingleton.getInstancia().setAssumpteMail(c.getAssumpteMail());
        AppConfigSingleton.getInstancia().setCosMail(c.getCosMail());
        AppConfigSingleton.getInstancia().setAnotacioMapa(c.getAnotacioMapa());
        AppConfigSingleton.getInstancia().setInfluxDBHost(c.getInfluxdbHost());
        AppConfigSingleton.getInstancia().setInfluxDBPort(c.getInfluxdbPort());
        AppConfigSingleton.getInstancia().setInfluxDBUser(c.getInfluxdbUser());
        AppConfigSingleton.getInstancia().setInfluxDBPassword(c.getInfluxdbPassword());
        AppConfigSingleton.getInstancia().setInfluxDBSsl(c.isInfluxdbSsl());
    }

    /**
     *
     * @throws Exception Llança un error amb missatge en cas de que no es pogués iniciar la connexió
     *
     *  Aquesta funció s'encarrega de tancar la connexió creada amb la BBDD.
     */
    public Map getProperties() throws CryptoException, IOException, PropertiesHelperException, ConfigurationException {
        Map result = new HashMap();
        // Read the properties from a file instead of hard-coding it here.
        // Or pass the password in from the command-line.
        try {
            String passwd = getDecryptedPropsFilePassword("app.properties", "password","enc");
            result.put("javax.persistence.jdbc.password", passwd);
        } catch (PropertiesHelperException e) {
            System.err.println("Error en el procés d'encriptació");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return result;
    }
    private static String getDecryptedPropsFilePassword(String fitxer, String clauAEncriptar,String clauTestEncriptacio) throws PropertiesHelperException, ConfigurationException {
        AppPropertiesHelper helper = new AppPropertiesHelper(fitxer, clauAEncriptar, clauTestEncriptacio);
        return helper.getDecryptedUserPassword();
    }
    @Override
    public void stop() throws Exception {
        if(emf != null && emf.isOpen()) {
            emf.close();
        }

        super.stop();
    }


    /**
     *
     * @param stage Es un objecte que s'encarrega de carregar el formulari amb uns paràmetres prefedinits.
     *
     * Aquesta funció s'encarrega de obrir una connexió a la BBDD un cop s'ha iniciat.
     */

    @Override
    public void start(Stage stage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        Parent root = null;

        try {
            root = loader.load();
            LoginController controller = loader.getController();
            controller.setEmf (emf);
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            System.out.println(parameters);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
