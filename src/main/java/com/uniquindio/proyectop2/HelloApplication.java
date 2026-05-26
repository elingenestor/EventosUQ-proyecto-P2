package com.uniquindio.proyectop2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/com/uniquindio/proyectop2/vistas/autenticacion/iniciar_sesion.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 980, 620);

        scene.getStylesheets().add(
                HelloApplication.class.getResource("/estilos/estilos.css").toExternalForm()
        );

        stage.setTitle("Eventos UQ - Iniciar sesión");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}