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
            // 1. Cargamos tu vista FXML donde el usuario selecciona asientos/boletas
            // NOTA: Asegúrate de cambiar la ruta de abajo por el nombre real de tu FXML de compras
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/un..iquindio/proyectop2/vistas/eventos/detalle_evento.fxml"));
            javafx.scene.Parent root = loader.load();

            // 2. Obtenemos el controlador de tu pantalla de compras para inyectarle los datos
            // NOTA: Cambia 'DetalleEventoControlador' por el nombre de la clase controladora de tu vista de compras
            DetalleEventoControlador controladorCompra = loader.getController();
            if (controladorCompra != null) {
                controladorCompra.setUsuario(usuario);
                controladorCompra.inicializar(evento); // O el método que uses para pasarle el evento
            }

            // 3. Levantamos la ventana de manera Modal (Bloquea la de atrás hasta que compre o cierre)
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Proceso de Compra - " + evento.getNombre());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error al abrir la ventana de compras: " + e.getMessage());
            e.printStackTrace();

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error de Navegación");
            alert.setHeaderText("No se pudo abrir la pasarela de compra");
            alert.setContentText("Verifica las rutas de las vistas FXML.");
            alert.showAndWait();
        }
    }

}