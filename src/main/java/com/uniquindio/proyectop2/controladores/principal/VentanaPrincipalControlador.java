package com.uniquindio.proyectop2.controladores.principal;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.controladores.eventos.ListaEventosControlador;
import com.uniquindio.proyectop2.controladores.principal.InicioControlador;
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
        Usuario usuarioLogueado = SesionActual.getInstance().getUsuarioActual();

        if (lblBienvenida != null && usuarioLogueado != null) {
            lblBienvenida.setText("Bienvenido(a), " + usuarioLogueado.getNombreCompleto());
        }

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
