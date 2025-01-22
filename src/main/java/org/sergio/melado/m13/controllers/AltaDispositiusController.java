package org.sergio.melado.m13.controllers;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.sergio.melado.m13.classes.Constants;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.classes.DefaultDBControllerDispositiuUsuariModes;
import org.sergio.melado.m13.enums.Modes;
import org.sergio.melado.m13.models.Dispositiu;
import org.sergio.melado.m13.utils.AlertHelper;
import org.sergio.melado.m13.utils.Patterns;
import org.sergio.melado.m13.utils.Utilitats;

import java.util.regex.Pattern;

import static org.sergio.melado.m13.classes.Constants.ZOOM_DEFAULT;
import static org.sergio.melado.m13.classes.Constants.ZOOM_LOCATED;

public class AltaDispositiusController extends DefaultDBControllerDispositiuUsuariModes {

    @FXML
    private Label lbTitol;

    @FXML
    private TextField tfDescripcio;

    @FXML
    private TextField tfLatitud;

    @FXML
    private TextField tfLongitud;

    @FXML
    private MapView mvMapaLocalitzacioDispositiu;

    private DAOHelper<Dispositiu> helper;
    private boolean tancarFormulari = false;
    private static final Coordinate coordVilafrancaPenedes = new Coordinate(Constants.DEFAULT_LATITUD, Constants.DEFAULT_LONGITUD);
    private static final Marker.Provided DEFAULT_MARKER_COLOR = Marker.Provided.BLUE;
    private static IntegerProperty zoomValue = new SimpleIntegerProperty(ZOOM_DEFAULT);


    public void initMapAndControls(Projection projection) {
        // inicialitza MapView-Cache
        final OfflineCache offlineCache = mvMapaLocalitzacioDispositiu.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";

        mvMapaLocalitzacioDispositiu.setCustomMapviewCssURL(getClass().getResource("/styles/mapview.css"));

        // escolta (monitortiza) la propietat que indica que el mapa ha finalitzat la seva inicialització
        mvMapaLocalitzacioDispositiu.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });

        mvMapaLocalitzacioDispositiu.setMapType(MapType.OSM);

        setupEventHandlers();

        mvMapaLocalitzacioDispositiu.initialize(Configuration.builder()
                .projection(projection)
                .build());
    }

    /**
     * Inicialitza els listeners de gestió del mapa i la GUI
     */

    @Override
    public void inicia() {

        helper = new DAOHelper<>(getEmf(), Dispositiu.class);
        switch (getMode()) {
            case CERCA: modificarDispositiu(); break;
            case ALTA: altaDispositiu(); break;
        }

    }

    private void setupEventHandlers() {

        // Gestiona l'esdeveniment de fer clic en el mapa
        mvMapaLocalitzacioDispositiu.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            final Coordinate newPosition = event.getCoordinate().normalize();
            AlertHelper.alertInfo(String.format("[MAP_CLICKED] --> Posició: [%s, %s]", newPosition.getLatitude(), newPosition.getLongitude()));
        });

        // Gestiona l'esdeveniment de fer clic amb el botó dret en el mapa
        mvMapaLocalitzacioDispositiu.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            AlertHelper.alertInfo(String.format("[MAP_RIGHTCLICKED] --> Posició: [%s, %s]", event.getCoordinate().getLatitude(), event.getCoordinate().getLongitude()));
        });

        // Gestiona quan s'ha fet clic a un marcador
        mvMapaLocalitzacioDispositiu.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            AlertHelper.alertInfo(String.format("[MARKER_CLICKED] --> Id del marcador: [%s]", event.getMarker().getId()));
        });

        // Gestiona quan s'ha fet clic amb el botó dret a un marcador
        mvMapaLocalitzacioDispositiu.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            event.consume();
            AlertHelper.alertInfo(String.format("[MARKER_RIGHTCLICKED] --> Id del marcador: [%s]", event.getMarker().getId()));
        });

        // Gestiona quan s'ha fer clic en una etiqueta al mapa
        mvMapaLocalitzacioDispositiu.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            event.consume();
            AlertHelper.alertInfo(String.format("[MAPLABEL_CLICKED] --> Id del marcador: [%s]", event.getMapLabel().getId()));
        });

        // Gestiona quan s'ha fer clic amb el botó dret en una etiqueta al mapa
        mvMapaLocalitzacioDispositiu.addEventHandler(MapLabelEvent.MAPLABEL_RIGHTCLICKED, event -> {
            event.consume();
            AlertHelper.alertInfo(String.format("[MAPLABEL_RIGHTCLICKED] --> Id del marcador: [%s]", event.getMapLabel().getId()));
        });

        // Gestiona el punter al mapa
        mvMapaLocalitzacioDispositiu.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> {
            System.out.println("el punter està en " + event.getCoordinate());
        });
    }

    /**
     * Finalitza la configuració desprès que el mapa estigui inicialitzat
     */
    private void afterMapIsInitialized() {
        mvMapaLocalitzacioDispositiu.setZoom(ZOOM_DEFAULT);
        mvMapaLocalitzacioDispositiu.setCenter(coordVilafrancaPenedes);
        addMarker(true);

    }

    private boolean addMarker (boolean center) {
        boolean result = false;
        if (!tfLatitud.getText().isEmpty() && !tfLongitud.getText().isEmpty()) {
            if (Pattern.matches(Patterns.fpRegex, tfLatitud.getText())) {
                Double lat = Double.valueOf(tfLatitud.getText());
                if (Pattern.matches(Patterns.fpRegex, tfLongitud.getText())) {
                    Double lng = Double.valueOf(tfLongitud.getText());
                    Coordinate c = new Coordinate(lat,lng);
                    addMarker(c, DEFAULT_MARKER_COLOR, center);
                    result = true;
                } else {
                    AlertHelper.alertInfo("El valor de la <longitud> no és correcte");
                }
            } else {
                AlertHelper.alertInfo("El valor de la <latitud> no és correcte");
            }
        }
        return result;
    }

    private void addMarker (Coordinate coordinate, Marker.Provided color, boolean center) {
        Marker marker = Marker.createProvided(color).setPosition(coordinate).setVisible(true);
        String info = String.format("Posició [%s,%s]", coordinate.getLatitude().toString(), coordinate.getLongitude().toString());
        MapLabel lblInfo = new MapLabel(info, 10, -10).setVisible(false).setCssClass("orange-label");
        marker.attachLabel(lblInfo);
        mvMapaLocalitzacioDispositiu.addMarker(marker);
        if (center)
            mvMapaLocalitzacioDispositiu.setCenter(coordinate);
        mvMapaLocalitzacioDispositiu.setZoom(ZOOM_LOCATED);
    }


    /**
     * Finalitza la configuració desprès que el mapa estigui inicialitzat
     */


    private void modificarDispositiu() {
        lbTitol.setText("Modificar Dispositiu");
        tfDescripcio.setText(getDispositiu().getDescripcio());
        tfLatitud.setText(getDispositiu().getLatitud());
        tfLongitud.setText(getDispositiu().getLongitud());
        getDispositiu().setUsuari(getDispositiu().getUsuari());
    }

    private void altaDispositiu() {
        lbTitol.setText("Alta Dispositiu");
    }

    private Dispositiu adaptaAltaCerca(Dispositiu dispositiu) {
        dispositiu.setDescripcio(tfDescripcio.getText());
        dispositiu.setLatitud(tfLatitud.getText());
        dispositiu.setLongitud(tfLongitud.getText());

        if (getMode() == Modes.CERCA) {

            dispositiu.setUsuari(dispositiu.getUsuari());

        }else {

            dispositiu.setUsuari(getUsuari());
        }

        return dispositiu;
    }

    private boolean testAlertesCampsOligatoris() {
        boolean result = true;
        if (tfDescripcio.getLength() == 0 || tfLatitud.getLength() == 0 || tfLongitud.getLength() == 0) {
            Utilitats.alertaGeneralWarning(Constants.TITOL_ALERTA_ALTA_DISPOSITIUS, Constants.CAPCALERA_ALERTA_CAMPS_BUITS, Constants.MISSATGE_ALERTA_CAMPS_BUITS);
            result = false;
            tfDescripcio.requestFocus();
        }

        return result;
    }

    private boolean insertDispositiu() {

        Dispositiu d = adaptaAltaCerca(new Dispositiu());
        try {

            helper.update(d);
            tancarFormulari = true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return tancarFormulari;
    }

    private boolean updateDispositiu() {

        adaptaAltaCerca(getDispositiu());
        try {

            helper.update(getDispositiu());
            tancarFormulari = true;

        }catch (Exception e) {

            e.printStackTrace();
        }

        return tancarFormulari;
    }

    public void btGuardarDispositiuOnAction(ActionEvent actionEvent) {
        if (testAlertesCampsOligatoris()) {

            switch (getMode()) {
                case CERCA:
                    tancarFormulari = updateDispositiu();
                    break;
                case ALTA:
                    tancarFormulari = insertDispositiu();
                    break;
            }

            if (tancarFormulari) {
                ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow()).close();
            }
        }

    }
}
