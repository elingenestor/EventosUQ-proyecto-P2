package com.uniquindio.proyectop2.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CargadorVistas {
    private static final String FXML_PATH = "/com/uniquindio/proyectop2/vistas/";
    private static final String CSS_PATH = "/estilos/estilos.css";

    public static Parent cargarFXML(String nombreFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(CargadorVistas.class.getResource(FXML_PATH + nombreFXML));
        return loader.load();
    }

    public static <T> T cargarControlador(String nombreFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(CargadorVistas.class.getResource(FXML_PATH + nombreFXML));
        loader.load();
        return loader.getController();
    }

    public static void cambiarVista(Stage stage, String nombreFXML, String titulo) throws IOException {
        Parent root = cargarFXML(nombreFXML);
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                CargadorVistas.class.getResource(CSS_PATH).toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle(titulo);
        stage.show();
    }
}