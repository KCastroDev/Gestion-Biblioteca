package com.upn.gestion.controller;

import com.upn.gestion.model.Prestamo;
import com.upn.gestion.service.PrestamoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class VistaPrestamoController {

    @FXML private TextField txtDniPrestamo;
    @FXML private TextField txtIdLibroPrestamo;
    @FXML private Label lblMensajePrestamo;

    @Autowired
    private PrestamoService prestamoService;

    @FXML
    public void initialize() {
        // Si necesitas algo al cargar la vista, agrégalo aquí
        lblMensajePrestamo.setText("");
    }

    @FXML
    public void registrarPrestamoAction() {
        try {
            String dni = txtDniPrestamo.getText();
            String idLibroStr = txtIdLibroPrestamo.getText();

            if (dni.isEmpty() || idLibroStr.isEmpty()) {
                lblMensajePrestamo.setText(" Por favor complete todos los campos");
                lblMensajePrestamo.setStyle("-fx-text-fill: red;");
                return;
            }

            Long idLibro = Long.parseLong(idLibroStr);

            Prestamo nuevoPrestamo =
                    prestamoService.registrarPrestamo(dni, Collections.singletonList(idLibro));

            lblMensajePrestamo.setText(" Préstamo registrado con éxito. ID: " + nuevoPrestamo.getIdPrestamo());
            lblMensajePrestamo.setStyle("-fx-text-fill: green;");

            txtDniPrestamo.clear();
            txtIdLibroPrestamo.clear();

        } catch (NumberFormatException e) {
            lblMensajePrestamo.setText(" El ID del libro debe ser un número");
            lblMensajePrestamo.setStyle("-fx-text-fill: red;");
        } catch (RuntimeException e) {
            lblMensajePrestamo.setText("Error: " + e.getMessage());
            lblMensajePrestamo.setStyle("-fx-text-fill: red;");
        }
    }
}