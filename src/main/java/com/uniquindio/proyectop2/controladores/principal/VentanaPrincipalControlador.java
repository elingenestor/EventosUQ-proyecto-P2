package com.uniquindio.proyectop2.controladores.principal;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.controladores.eventos.ListaEventosControlador; // Asegúrate de importar tus controladores
import com.uniquindio.proyectop2.controladores.principal.InicioControlador;       // Ajusta el paquete según tu estructura
import com.uniquindio.proyectop2.controladores.compras.MisComprasControlador;
import com.uniquindio.proyectop2.controladores.usuario.PerfilUsuarioControlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class VentanaPrincipalControlador {

    @FXML
    private BorderPane contenedorPrincipal; // Estructura limpia usando BorderPane

    @FXML
    private Label lblBienvenida; // Saludo personalizado al usuario

    @FXML
    private Label lblTituloSeccion; // Título dinámico de la cabecera

    private Usuario usuario; // Guardamos el estado del usuario logueado

    @FXML
    private void initialize() {
        // La vista inicial se carga por defecto como "Inicio"
        mostrarInicio();
    }

    /**
     * Recibe el usuario desde la pantalla de Login e inicializa la interfaz.
     */
    public void inicializar(Usuario usuario) {
        this.usuario = usuario;
        if (lblBienvenida != null && usuario != null) {
            lblBienvenida.setText("Bienvenido(a), " + usuario.getNombreCompleto());
        }
        // Recargamos el inicio para que el controlador interno reciba el usuario
        mostrarInicio();
    }

    @FXML
    private void mostrarInicio() {
        cargarVista("/com/uniquindio/proyectop2/vistas/principal/inicio.fxml", "Inicio");
    }

    @FXML
    private void mostrarEventos() {
        cargarVista("/com/uniquindio/proyectop2/vistas/eventos/lista_eventos.fxml", "Eventos");
    }

    @FXML
    private void mostrarCompras() {
        cargarVista("/com/uniquindio/proyectop2/vistas/compras/mis_compras.fxml", "Mis Compras");
    }

    @FXML
    private void mostrarPerfil() {
        cargarVista("/com/uniquindio/proyectop2/vistas/usuarios/perfil_usuario.fxml", "Mi Perfil");
    }

    /**
     * Método maestro de navegación: Carga el FXML, actualiza el título,
     * controla errores en pantalla e inyecta los datos del usuario.
     */
    private void cargarVista(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Node vista = loader.load();

            // 1. Cambiamos la vista en el centro del BorderPane
            if (contenedorPrincipal != null) {
                contenedorPrincipal.setCenter(vista);
            }

            // 2. Actualizamos el título de la sección de manera dinámica
            if (lblTituloSeccion != null) {
                lblTituloSeccion.setText(titulo);
            }

            // 3. Inyección dinámica del objeto Usuario según la pantalla activa
            Object controladorDestino = loader.getController();
            if (controladorDestino != null && usuario != null) {

                if (controladorDestino instanceof InicioControlador) {
                    ((InicioControlador) controladorDestino).setUsuario(usuario);
                }
                else if (controladorDestino instanceof ListaEventosControlador) {
                    ((ListaEventosControlador) controladorDestino).setUsuario(usuario);
                }
                else if (controladorDestino instanceof MisComprasControlador) {
                    ((MisComprasControlador) controladorDestino).setUsuario(usuario);
                }
                else if (controladorDestino instanceof PerfilUsuarioControlador) {
                    ((PerfilUsuarioControlador) controladorDestino).setUsuario(usuario);
                }
                // Puedes agregar más "else if" aquí para futuras pantallas (Compras, Perfil, etc.)
            }

        } catch (IOException e) {
            // Control de errores elegante: muestra el error en la interfaz sin romper el programa
            if (contenedorPrincipal != null) {
                contenedorPrincipal.setCenter(new Label("No se pudo cargar la vista: " + rutaFXML));
            }
            if (lblTituloSeccion != null) {
                lblTituloSeccion.setText("Error de Carga");
            }
            e.printStackTrace();
        }
    }
}
