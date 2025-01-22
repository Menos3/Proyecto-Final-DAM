package org.sergio.melado.m13.jasper;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * An simple approach to JasperViewer in JavaFX.
 *
 * @author Gustavo Fragoso
 * @version 3.3
 *
 * Adaptat per
 * @author Rubén Nadal
 *
 * IES Eugeni d'Ors
 * Departament d'Informàtica
 *
 * M15-DUAL - Curs 2018 - 2019
 *
 * source code -->  https://github.com/gbfragoso/JasperViewerFX
 */
public class JasperViewerFX extends Dialog<Void>{

    private static final String INVALID_NUMBER = "Número invalid";
    private static final String SAVE = "Guardar...";
    private static final String CURRENT_PAGE = "Pagina actual";
    private static final int FIRST_PAGE = 1;
    private static final int DEFAULT_WINDOW_WIDTH = 1024;
    private static final int DEFAULT_WINDOW_HEIGHT = 768;
    // Documents
    private static final String PDF_DESC = "Portable Document Format";
    private static final String HTML_DESC = "HyperText Markup Language";
    private static final String XML_DESC = "eXtensible Markup Language";
    private static final String EXCEL2007_DESC = "Microsoft Excel 2007";
    private static final String EXCEL2016_DESC = "Microsoft Excel 2016";
    // Extensions
    private static final String PDF_EXT = "*.pdf";
    private static final String HTML_EXT = "*.html";
    private static final String XML_EXT = "*.xml";
    private static final String EXCEL2007_EXT = "*.xls";
    private static final String EXCEL2016_EXT = "*.xlsx";

    private Button print;
    private Button save;
    private Button backPage;
    private Button firstPage;
    private Button nextPage;
    private Button lastPage;
    private Button zoomIn;
    private Button zoomOut;
    private ImageView report;
    private Label lblReportPages;
    private Stage view;
    private TextField txtPage;

    private JasperPrint jasperPrint;

    private SimpleIntegerProperty currentPage;
    private int imageHeight = 0;
    private int imageWidth = 0;
    private int reportPages = 0;
    private DialogPane dialogPane;

    private String styleSheet = getClass().getResource("/jasper/jasper_style.css").toExternalForm();

    public JasperViewerFX() {
        initModality(Modality.WINDOW_MODAL);
        setResizable(true);

        dialogPane = getDialogPane();
        dialogPane.getStylesheets().add(styleSheet);
        dialogPane.setContent(createContentPane());
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        dialogPane.lookupButton(ButtonType.CLOSE).setVisible(false);

        currentPage = new SimpleIntegerProperty(this, CURRENT_PAGE, FIRST_PAGE);
    }

    // ***********************************************
    // Escena (scene) i actions dels botons
    // ***********************************************
    private BorderPane createContentPane() {

        setButtons();

        backAction();
        nextAction();
        firstPageAction();
        lastPageAction();
        zoomInAction();
        zoomOutAction();
        printAction();
        saveAction();

        txtPage = new TextField("1");
        txtPage.setPrefSize(40, 30);
        txtPage.setOnAction(event -> {
            try {
                int p = Integer.parseInt(txtPage.getText());
                setCurrentPage((p > 0 && p <= reportPages) ? p : 1);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, INVALID_NUMBER, ButtonType.OK).show();
            }
        });

        lblReportPages = new Label("/ 1");

        HBox menu = new HBox(10);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(5));
        menu.setPrefHeight(50.0);
        menu.getChildren().addAll(print, save, firstPage, backPage, txtPage, lblReportPages, nextPage, lastPage, zoomIn, zoomOut);

        // This imageview will preview the pdf inside scrollpane
        report = new ImageView();
        report.setFitHeight(imageHeight);
        report.setFitWidth(imageWidth);

        // Centralizing the ImageView on Scrollpane
        Group contentGroup = new Group();
        contentGroup.getChildren().add(report);

        StackPane stack = new StackPane(contentGroup);
        stack.setAlignment(Pos.CENTER);
        stack.setStyle("-fx-background-color: gray");

        ScrollPane scroll = new ScrollPane(stack);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        BorderPane root = new BorderPane();
        root.setTop(menu);
        root.setCenter(scroll);
        root.setPrefSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

        return root;
    }

    private void setButtons() {
        print = setButton("jasper-button-print", "jasper-button");
        save = setButton("jasper-button-save", "jasper-button");
        backPage = setButton("jasper-button-back", "jasper-button");
        firstPage = setButton("jasper-button-first", "jasper-button");
        nextPage = setButton("jasper-button-next", "jasper-button");
        lastPage = setButton("jasper-button-last", "jasper-button");
        zoomIn = setButton("jasper-button-zoomIn", "jasper-button");
        zoomOut = setButton("jasper-button-zoomOut", "jasper-button");
    }

    private Button setButton(String styleClass, String classId) {
        Button boto = new Button(null);
        boto.getStyleClass().clear();
        boto.getStyleClass().add(styleClass);
        boto.setId(classId);
        return boto;
    }

    private void backAction() {
        backPage.setOnAction((ActionEvent event) -> {
            int newValue = getCurrentPage() - 1;
            setCurrentPage(newValue);

            // Turn foward buttons on again
            if (nextPage.isDisabled()) {
                nextPage.setDisable(false);
                lastPage.setDisable(false);
            }
        });
    }

    private void firstPageAction() {
        firstPage.setOnAction((ActionEvent event) -> {
            setCurrentPage(1);

            // Turn foward buttons on again
            if (nextPage.isDisabled()) {
                nextPage.setDisable(false);
                lastPage.setDisable(false);
            }
        });
    }

    private void nextAction() {
        nextPage.setOnAction((ActionEvent event) -> {
            int newValue = getCurrentPage() + 1;
            setCurrentPage(newValue);

            // Turn previous button on again
            if (backPage.isDisabled()) {
                backPage.setDisable(false);
                firstPage.setDisable(false);
            }
        });
    }

    private void lastPageAction() {
        lastPage.setOnAction((ActionEvent event) -> {
            setCurrentPage(reportPages);

            // Turn previous button on again
            if (backPage.isDisabled()) {
                backPage.setDisable(false);
                firstPage.setDisable(false);
            }
        });
    }

    private void printAction() {
        print.setOnAction((ActionEvent event) -> {
            try {
                JasperPrintManager.printReport(jasperPrint, true);
                close();
            } catch (JRException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void saveAction() {
        save.setOnAction((ActionEvent event) -> {

            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter pdf = new FileChooser.ExtensionFilter(PDF_DESC, PDF_EXT);
            FileChooser.ExtensionFilter html = new FileChooser.ExtensionFilter(HTML_DESC, HTML_EXT);
            FileChooser.ExtensionFilter xml = new FileChooser.ExtensionFilter(XML_DESC, XML_EXT);
            FileChooser.ExtensionFilter xls = new FileChooser.ExtensionFilter(EXCEL2007_DESC, EXCEL2007_EXT);
            FileChooser.ExtensionFilter xlsx = new FileChooser.ExtensionFilter(EXCEL2016_DESC, EXCEL2016_EXT);
            chooser.getExtensionFilters().addAll(pdf, html, xml, xls, xlsx);

            chooser.setTitle(SAVE);
            chooser.setSelectedExtensionFilter(pdf);
            File file = chooser.showSaveDialog(view);

            if (file != null) {
                List<String> box = chooser.getSelectedExtensionFilter().getExtensions();

                switch (box.get(0)) {
                    case PDF_EXT:
                        exportToPdf(file);
                        break;
                    case HTML_EXT:
                        exportToHtml(file);
                        break;
                    case XML_EXT:
                        exportToXml(file);
                        break;
                    case EXCEL2007_EXT:
                        exportToXls(file);
                        break;
                    case EXCEL2016_EXT:
                        exportToXlsx(file);
                        break;
                    default:
                        exportToPdf(file);
                }
            }
        });
    }

    /**
     * Export report to html file
     */
    public void exportToHtml(File file) {
        try {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, file.getPath());
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }
// com.lowagie.text.DocumentException
    /**
     * Export report to Pdf file
     */
    public void exportToPdf(File file) {
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath());
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Export report to old Microsoft Excel file
     */
    public void exportToXls(File file) {
        try {
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
            exporter.exportReport();
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Export report to Microsoft Excel file
     */
    public void exportToXlsx(File file) {
        try {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
            exporter.exportReport();
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param file
     */
    public void exportToXml(File file) {
        try {
            JasperExportManager.exportReportToXmlFile(jasperPrint, file.getPath(), false);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void zoomInAction() {
        zoomIn.setOnAction(event -> zoom(0.15));
    }

    private void zoomOutAction() {
        zoomOut.setOnAction(event -> zoom(-0.15));
    }

    /**
     * Set the currentPage property and render report page
     * @param page Page number
     */
    public void setCurrentPage(int page) {
        try {
            if(page > 0 && page <= reportPages) {
                currentPage.set(page);
                txtPage.setText(Integer.toString(page));

                if (page == 1) {
                    backPage.setDisable(true);
                    firstPage.setDisable(true);
                }

                if (page == reportPages) {
                    nextPage.setDisable(true);
                    lastPage.setDisable(true);
                }

                // Rendering the current page
                float zoom = (float) 1.33;
                BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, page - 1, zoom);
                WritableImage fxImage = new WritableImage(imageHeight, imageWidth);
                report.setImage(SwingFXUtils.toFXImage(image, fxImage));
            }
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get the current page
     * @return Current page value
     */
    public int getCurrentPage() {
        return currentPage.get();
    }

    /**
     * Get the currentPage property
     * @return
     */
    public SimpleIntegerProperty currentPageProperty() {
        return currentPage;
    }

    /**
     * Load report from JasperPrint
     * @param title Dialog title
     * @param jasperPrint JasperPrint object
     */
    public void viewReport(String title, JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;

        // Report rendered image properties
        imageHeight = jasperPrint.getPageHeight() + 284;
        imageWidth = jasperPrint.getPageWidth() + 201;
        reportPages = jasperPrint.getPages().size();
        lblReportPages.setText("/ " + reportPages);

        setCurrentPage(1);

        // With only one page those buttons are unnecessary
        if (reportPages == 1) {
            nextPage.setDisable(true);
            lastPage.setDisable(true);
        }

        setTitle(title);
        show();
    }

    /**
     * Scale image from ImageView
     * @param factor Zoom factor
     */
    public void zoom(double factor) {
        report.setScaleX(report.getScaleX() + factor);
        report.setScaleY(report.getScaleY() + factor);
        report.setFitHeight(imageHeight + factor);
        report.setFitWidth(imageWidth + factor);
    }
}