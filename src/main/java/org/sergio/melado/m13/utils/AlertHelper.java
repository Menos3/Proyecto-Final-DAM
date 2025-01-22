package org.sergio.melado.m13.utils;

import javafx.scene.control.Alert;

public class AlertHelper {

    private static final String TITLE = "MapJFX Desktop App";

    private static void alertaMapa(String title, String header, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(message);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void alerta(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void alertInfo(String message) {
        alerta(TITLE, message, Alert.AlertType.INFORMATION);
    }

}
