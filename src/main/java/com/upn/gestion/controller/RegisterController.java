package com.upn.gestion.controller;

import com.upn.gestion.model.Estudiante;
import com.upn.gestion.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RegisterController {

    @FXML private TextField txtRegNombre;
    @FXML private TextField txtRegApellido;
    @FXML private TextField txtRegDNI;
    @FXML private TextField txtRegTelf;
    @FXML private TextField txtRegCodigo;
    @FXML private TextField txtRegCarrera;
    @FXML private TextField txtRegEmail;
    @FXML private PasswordField txtRegPassword;
    @FXML private PasswordField txtRegPasswordRepeat;

    @FXML private Label lblMensaje;

    @Autowired private UsuarioService usuarioService;
    @Autowired private ApplicationContext applicationContext;

    @FXML
    public void registrar() {
        // Limpiar mensaje anterior
        lblMensaje.setText("");

        //  Validar que todos los campos estén llenos
        if (txtRegNombre.getText().trim().isEmpty() ||
                txtRegApellido.getText().trim().isEmpty() ||
                txtRegDNI.getText().trim().isEmpty() ||
                txtRegTelf.getText().trim().isEmpty() ||
                txtRegCodigo.getText().trim().isEmpty() ||
                txtRegCarrera.getText().trim().isEmpty() ||
                txtRegEmail.getText().trim().isEmpty() ||
                txtRegPassword.getText().isEmpty() ||
                txtRegPasswordRepeat.getText().isEmpty()) {

            mostrarError("Por favor, complete todos los campos");
            return;
        }

        // Validar  email básico
        if (!txtRegEmail.getText().contains("@")) {
            mostrarError("Por favor, ingrese un email válido");
            return;
        }

        // Validar que las contraseñas coincidan
        if (!txtRegPassword.getText().equals(txtRegPasswordRepeat.getText())) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }

        //  Validar longitud mínima de contraseña
        if (txtRegPassword.getText().length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres");
            return;
        }

        try {
            //  Crear el ESTUDIANTE
            Estudiante estudiante = new Estudiante();

            // Datos generales de la clase padre UsuarioSistema
            estudiante.setNombre(txtRegNombre.getText().trim());
            estudiante.setApellidos(txtRegApellido.getText().trim());
            estudiante.setDni(txtRegDNI.getText().trim());
            estudiante.setTelefono(txtRegTelf.getText().trim());
            estudiante.setEmail(txtRegEmail.getText().trim());
            estudiante.setPassword(txtRegPassword.getText());

            // Datos específicos de la clase hijo estudiante
            estudiante.setCodigoEstudiante(txtRegCodigo.getText().trim());
            estudiante.setCarrera(txtRegCarrera.getText().trim());

            //  Guardar en la base de datos
            usuarioService.guardarUsuario(estudiante);

            // Mostrar mensaje de éxito
            lblMensaje.setText("¡Estudiante registrado! Código: " + estudiante.getCodigoEstudiante());
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
            mostrarError("Error al registrar (Revise si el DNI/Código ya existe)");
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
        txtRegCodigo.clear();
        txtRegCarrera.clear();
        txtRegEmail.clear();
        txtRegPassword.clear();
        txtRegPasswordRepeat.clear();
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: red;");
    }
}