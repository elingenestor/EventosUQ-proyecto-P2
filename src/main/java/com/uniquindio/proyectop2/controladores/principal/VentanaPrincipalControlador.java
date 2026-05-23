package com.uniquindio.proyectop2.controladores.principal;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.controladores.eventos.ListaEventosControlador;
import com.uniquindio.proyectop2.controladores.compras.MisComprasControlador;
import com.uniquindio.proyectop2.controladores.usuario.PerfilUsuarioControlador;
import com.uniquindio.proyectop2.util.SesionActual; // IMPORTAMOS EL SINGLETON
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class VentanaPrincipalControlador {

    @FXML
    private BorderPane contenedorPrincipal;

    @FXML
    private Label lblBienvenida;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    private void initialize() {
        // 1. Obtenemos el usuario directamente del Singleton de forma segura
        Usuario usuarioLogueado = SesionActual.getInstance().getUsuarioActual();

        // 2. Colocamos el texto de bienvenida de inmediato
        if (lblBienvenida != null && usuarioLogueado != null) {
            lblBienvenida.setText("Bienvenido(a), " + usuarioLogueado.getNombreCompleto());
        }

        // 3. Cargamos la vista inicial (ahora sí llevará el usuario correctamente)
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

    private void cargarVista(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Node vista = loader.load();

            if (contenedorPrincipal != null) {
                contenedorPrincipal.setCenter(vista);
            }

            if (lblTituloSeccion != null) {
                lblTituloSeccion.setText(titulo);
            }

            // 4. Recuperamos el usuario del Singleton para inyectarlo en las sub-vistas
            Usuario usuarioLogueado = SesionActual.getInstance().getUsuarioActual();
            Object controladorDestino = loader.getController();

            if (controladorDestino != null && usuarioLogueado != null) {
                if (controladorDestino instanceof InicioControlador) {
                    ((InicioControlador) controladorDestino).setUsuario(usuarioLogueado);
                }
                else if (controladorDestino instanceof ListaEventosControlador) {
                    ((ListaEventosControlador) controladorDestino).setUsuario(usuarioLogueado);
                }
                else if (controladorDestino instanceof MisComprasControlador) {
                    ((MisComprasControlador) controladorDestino).setUsuario(usuarioLogueado);
                }
                else if (controladorDestino instanceof PerfilUsuarioControlador) {
                    ((PerfilUsuarioControlador) controladorDestino).setUsuario(usuarioLogueado);
                }
            }

        } catch (IOException e) {
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
