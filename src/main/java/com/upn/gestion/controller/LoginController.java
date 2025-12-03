package com.upn.gestion.controller;

import com.upn.gestion.model.UsuarioSistema;
import com.upn.gestion.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    @Autowired private UsuarioService usuarioService;
    @Autowired private ApplicationContext applicationContext; // Necesario para abrir la otra ventana

    @FXML
    public void ingresar() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        UsuarioSistema usuario = usuarioService.login(email, password);

        if (usuario != null) {
            // Login Exitoso: Abrir Main
            abrirMenuPrincipal();
        } else {
            lblMensaje.setText("Credenciales incorrectas");
        }
    }

    @FXML
    public void registrar() {
        abrirRegistroUsuario();
    }

    private void abrirRegistroUsuario() {
        try {
            // 1. Cargar la vista de  registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            loader.setControllerFactory(applicationContext::getBean); // Importante para Spring
            Parent root = loader.load();

            // 2. Crear nueva escena
            Stage stage = new Stage();
            stage.setTitle("Sistema de Gestión de Biblioteca - Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Mantiene la ventana ON-TOP
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirMenuPrincipal() {
        try {
            // 1. Cargar la vista principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            loader.setControllerFactory(applicationContext::getBean); // Importante para Spring
            Parent root = loader.load();

            // 2. Crear nueva escena
            Stage stage = new Stage();
            stage.setTitle("Sistema de Gestión de Biblioteca - Panel Principal");
            stage.setScene(new Scene(root));
            stage.show();

            // 3. Cerrar la ventana de Login actual
            Stage loginStage = (Stage) txtEmail.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}