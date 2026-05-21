package com.uniquindio.proyectop2.controladores.administrador;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.interfaces.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionUsuariosControlador {

    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, String> colId;
    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableColumn<Usuario, String> colEmail;
    @FXML
    private TableColumn<Usuario, String> colTelefono;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnGuardar;

    private final AdminService adminService = DAOFactory.getAdminService();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<Usuario, String>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombreCompleto"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Usuario, String>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Usuario, String>("telefono"));

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
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(txtId.getText());
            usuario.setNombreCompleto(txtNombre.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setTelefono(txtTelefono.getText());
            usuario.setPassword(txtPassword.getText());

            if (usuario.getIdUsuario() == null || usuario.getIdUsuario().trim().isEmpty()) {
                adminService.crearUsuario(usuario);
                mostrarInfo("Usuario creado correctamente");
            } else {
                adminService.actualizarUsuario(usuario);
                mostrarInfo("Usuario actualizado correctamente");
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
                mostrarError("Selecciona un usuario para eliminar");
                return;
            }
            adminService.eliminarUsuario(id);
            mostrarInfo("Usuario eliminado correctamente");
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
