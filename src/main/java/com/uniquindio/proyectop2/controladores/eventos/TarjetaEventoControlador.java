package com.uniquindio.proyectop2.controladores.eventos;

import com.uniquindio.proyectop2.Enums.EstadoEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.LocalDateTime;

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

        // Registro formal en consola que demuestra la captura exitosa de datos para el flujo de compra
        String cliente = (usuario != null) ? usuario.getNombreCompleto() : "Anónimo";
        System.out.println("=================================================");
        System.out.println("INICIANDO FLUJO DE COMPRA DIRECTA");
        System.out.println("Usuario adquirente: " + cliente);
        System.out.println("Evento seleccionado: " + evento.getNombre());
        System.out.println("Ubicación: " + lblRecinto.getText() + " (" + evento.getCiudad() + ")");
        System.out.println("=================================================");

        // El puente queda despejado para que tu compañero inserte aquí su FXMLLoader
    }
}
