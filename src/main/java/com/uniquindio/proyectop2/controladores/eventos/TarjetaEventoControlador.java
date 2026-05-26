package com.uniquindio.proyectop2.controladores.eventos;

import com.uniquindio.proyectop2.Enums.EstadoEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class TarjetaEventoControlador {

    @FXML private Label lblRecinto;
    @FXML private Label lblCategoria;
    @FXML private Label lblNombre;
    @FXML private Label lblCiudad;
    @FXML private Label lblFecha;
    @FXML private Button btnComprar;

    private Evento evento;
    private Usuario usuario;

    public void configurarTarjeta(Evento evento) {
        this.evento = evento;

        lblNombre.setText(evento.getNombre());
        lblCategoria.setText(evento.getCategoria() != null ? evento.getCategoria().name() : "OTRO");
        lblCiudad.setText(evento.getCiudad());
        lblFecha.setText(evento.getFechaHora() != null ? evento.getFechaHora().toString() : "Sin fecha");
        lblRecinto.setText(evento.getRecinto() != null ? evento.getRecinto().getNombre() : "Sin recinto configurado");

        boolean esInvalido = evento.getEstado() != EstadoEvento.PUBLICADO
                || evento.getFechaHora() == null
                || evento.getFechaHora().isBefore(LocalDateTime.now());

        if (esInvalido) {
            btnComprar.setDisable(true);
            btnComprar.setText("No disponible");
            btnComprar.setStyle("-fx-background-color: #aaaaaa; -fx-text-fill: #ffffff;");
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private void onComprarClick(ActionEvent event) {
        if (evento == null) return;

        String cliente = (usuario != null) ? usuario.getNombreCompleto() : "Anónimo";
        System.out.println("=================================================");
        System.out.println("INICIANDO FLUJO DE COMPRA DIRECTA");
        System.out.println("Usuario adquirente: " + cliente);
        System.out.println("Evento seleccionado: " + evento.getNombre());
        System.out.println("Ubicación: " + lblRecinto.getText() + " (" + evento.getCiudad() + ")");
        System.out.println("=================================================");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/compras/seleccion_asientos.fxml"));
            javafx.scene.Parent root = loader.load();

            com.uniquindio.proyectop2.controladores.compras.SeleccionAsientosControlador controladorAsientos = loader.getController();
            if (controladorAsientos != null) {
                controladorAsientos.inicializar(usuario, evento);
            }

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Selección de Asientos - " + evento.getNombre());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/estilos/estilos.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error crítico al abrir seleccion_asientos.fxml: " + e.getMessage());
            e.printStackTrace();

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error de Navegación");
            alert.setHeaderText("No se pudo abrir la selección de asientos");
            alert.setContentText("Verifica las rutas de las vistas FXML.\nDetalle: " + e.getMessage());
            alert.showAndWait();
        }
    }

}