package com.upn.gestion.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@Component
public class MainController {

    @FXML
    private StackPane contenedorCentral;

    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    public void initialize() {
        // Vista por defecto
        mostrarVistaLibros();
    }

    @FXML
    public void mostrarVistaLibros() {
        cargarVistaEnCentro("/fxml/vistaLibros.fxml");
    }

    @FXML
    public void mostrarVistaPrestamo() {
        cargarVistaEnCentro("/fxml/vistaPrestamo.fxml");
    }

    private void cargarVistaEnCentro(String rutaFxml) {
        try {
            // Si vas a integrar completamente con Spring, aquí se puede usar un FXMLLoader
            // configurado con el ApplicationContext. Por ahora usamos el estándar:
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));

            loader.setControllerFactory(applicationContext::getBean);

            Node vista = loader.load();
            contenedorCentral.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}