package com.upn.gestion.controller;


import com.upn.gestion.model.Administrador;
import com.upn.gestion.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RegisterController {

    @FXML private TextField txtRegNombre;
    @FXML private TextField txtRegApellido;
    @FXML private TextField txtRegDNI;
    @FXML private TextField txtRegTelf;
    @FXML private TextField txtRegEmail;
    @FXML private PasswordField txtRegPassword;
    @FXML private PasswordField txtRegPasswordRepeat;

    @FXML private Label lblMensaje;

    @Autowired private UsuarioService usuarioService;
    @Autowired private ApplicationContext applicationContext; // Necesario para abrir la otra ventana

    @FXML
    public void registrar() {
        // Limpiar mensaje anterior
        lblMensaje.setText("");

        // 1. Validar que todos los campos estén llenos
        if (txtRegNombre.getText().trim().isEmpty() ||
                txtRegApellido.getText().trim().isEmpty() ||
                txtRegDNI.getText().trim().isEmpty() ||
                txtRegTelf.getText().trim().isEmpty() ||
                txtRegEmail.getText().trim().isEmpty() ||
                txtRegPassword.getText().isEmpty() ||
                txtRegPasswordRepeat.getText().isEmpty()) {

            lblMensaje.setText("Por favor, complete todos los campos");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        // 2. Validar formato de email básico
        if (!txtRegEmail.getText().contains("@")) {
            lblMensaje.setText("Por favor, ingrese un email válido");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        // 3. Validar que las contraseñas coincidan
        if (!txtRegPassword.getText().equals(txtRegPasswordRepeat.getText())) {
            lblMensaje.setText("Las contraseñas no coinciden");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        // 4. Validar longitud mínima de contraseña
        if (txtRegPassword.getText().length() < 6) {
            lblMensaje.setText("La contraseña debe tener al menos 6 caracteres");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            // 5. Crear el administrador
            Administrador administrador = new Administrador();
            administrador.setNombre(txtRegNombre.getText().trim());
            administrador.setApellidos(txtRegApellido.getText().trim());
            administrador.setDni(txtRegDNI.getText().trim());
            administrador.setTelefono(txtRegTelf.getText().trim());
            administrador.setEmail(txtRegEmail.getText().trim());
            administrador.setPassword(txtRegPassword.getText()); // TODO: Encriptar en producción

            // Valores por defecto para administrador
            administrador.setCodigoTrabajador("ADM-" + txtRegDNI.getText().trim());
            administrador.setTurno("Mañana"); // Valor por defecto

            // 6. Guardar en la base de datos
            usuarioService.save(administrador);

            // 7. Mostrar mensaje de éxito
            lblMensaje.setText("¡Usuario registrado exitosamente!");
            lblMensaje.setStyle("-fx-text-fill: green;");

            // 8. Limpiar formulario
            limpiarCampos();

            // Cerrar ventana después de 2 segundos
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                javafx.application.Platform.runLater(this::cancelar);
            }).start();

        } catch (Exception e) {
            lblMensaje.setText("Error al registrar: " + e.getMessage());
            lblMensaje.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelar() {
        Stage stage = (Stage) txtRegNombre.getScene().getWindow();
        stage.close();
    }

    private void limpiarCampos() {
        txtRegNombre.clear();
        txtRegApellido.clear();
        txtRegDNI.clear();
        txtRegTelf.clear();
        txtRegEmail.clear();
        txtRegPassword.clear();
        txtRegPasswordRepeat.clear();
    }
}