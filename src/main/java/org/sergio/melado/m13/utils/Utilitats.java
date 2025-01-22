package org.sergio.melado.m13.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.sergio.melado.m13.classes.Constants.BTN_DACORD;

public class Utilitats {

    /**
     *
     * @param titolAlerta  Titol de la alerta
     * @param capcalera  capcalera de la alerta
     * @param missatge  El error del que s'esta avisant
     *
     * Metode per generar alertes de tipus WARNING
     * Es reben els tres missatges per omplir la finestra de la alerta i es mostra al usuari
     *
     */

    public static void alertaGeneralWarning(String titolAlerta, String capcalera, String missatge) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolAlerta);
        alert.setHeaderText(capcalera);
        alert.setContentText(missatge);
        alert.getDialogPane().setPrefSize(400, 300);
        ButtonType btDacord = new ButtonType(BTN_DACORD);
        alert.getButtonTypes().setAll(btDacord);
        alert.showAndWait();
    }

    /**
     * @return la ip del ordenador
     */
    public static String getIP(){

        String ip="";

        try(final DatagramSocket socket = new DatagramSocket()){

            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();

        } catch (UnknownHostException | SocketException e) {

            e.printStackTrace();
        }

        return ip;
    }


}
