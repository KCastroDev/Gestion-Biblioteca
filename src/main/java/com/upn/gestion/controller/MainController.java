package com.upn.gestion.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {

    // --- VISTAS ---
    @FXML private VBox vistaLibros;
    @FXML private VBox vistaPrestamo;
    @FXML private VBox vistaHistorial;

    //  NECESARIO PARA ABRIR LA VENTANA DE LOGIN OTRA VEZ
    @Autowired private ApplicationContext context;

    @FXML
    public void initialize() {
        mostrarVistaLibros();
    }

    // --- NAVEGACIÓN ---
    @FXML
    public void mostrarVistaLibros() {
        vistaLibros.setVisible(true);
        vistaPrestamo.setVisible(false);
        vistaHistorial.setVisible(false);
    }

    @FXML
    public void mostrarVistaPrestamo() {
        vistaLibros.setVisible(false);
        vistaPrestamo.setVisible(true);
        vistaHistorial.setVisible(false);
    }

    @FXML
    public void mostrarVistaHistorial() {
        vistaLibros.setVisible(false);
        vistaPrestamo.setVisible(false);
        vistaHistorial.setVisible(true);
    }

    // CERRAR SESIÓN ---
    @FXML
    public void cerrarSesion() {
        try {
            // 1. Cerramos la ventana actual (Main)
            // O en cualquier vista que nos encontremos
            Stage currentStage = (Stage) vistaLibros.getScene().getWindow();
            currentStage.close();

            // 2. Cargar el Login de nuevo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(context::getBean); // Conectamos con Spring
            Parent root = loader.load();

            // 3. Mostrar la ventana de Login
            Stage loginStage = new Stage();
            loginStage.setTitle("Sistema de Gestión de Biblioteca - Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}