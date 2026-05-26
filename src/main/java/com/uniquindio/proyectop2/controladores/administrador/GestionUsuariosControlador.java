package com.uniquindio.proyectop2.controladores.administrador;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.interfaces.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionUsuariosControlador {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colTelefono;

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtPassword;

    private final AdminService adminService = DAOFactory.getAdminService();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tablaUsuarios.setItems(usuarios);
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                txtId.setText(seleccionado.getIdUsuario());
                txtNombre.setText(seleccionado.getNombreCompleto());
                txtEmail.setText(seleccionado.getEmail());
                txtTelefono.setText(seleccionado.getTelefono());
                txtPassword.setText(seleccionado.getPassword());
            }
        });
        cargarUsuarios();
    }

    @FXML
    private void cargarUsuarios() {
        try {
            usuarios.setAll(adminService.listarUsuarios());
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void guardarUsuario() {
        try {
            String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
            String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";
            String telefono = txtTelefono.getText() != null ? txtTelefono.getText().trim() : "";
            String password = txtPassword.getText() != null ? txtPassword.getText() : "";

            if (nombre.isBlank() || email.isBlank() || telefono.isBlank() || password.isBlank()) {
                mostrarError("Todos los campos (Nombre, Email, Teléfono y Contraseña) son obligatorios.");
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(txtId.getText());
            usuario.setNombreCompleto(nombre);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setPassword(password);
            usuario.setAdmin(false);

            if (usuario.getIdUsuario() == null || usuario.getIdUsuario().trim().isEmpty()) {
                adminService.crearUsuario(usuario);
                mostrarInfo("Usuario creado correctamente.");
            } else {
                adminService.actualizarUsuario(usuario);
                mostrarInfo("Usuario actualizado correctamente.");
            }
            limpiarFormulario();
            cargarUsuarios();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void eliminarUsuario() {
        try {
            String id = txtId.getText();
            if (id == null || id.trim().isEmpty()) {
                mostrarError("Selecciona un usuario de la tabla para eliminar.");
                return;
            }
            adminService.eliminarUsuario(id);
            mostrarInfo("Usuario eliminado correctamente.");
            limpiarFormulario();
            cargarUsuarios();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtPassword.clear();
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
