package com.upn.gestion.controller;

import com.upn.gestion.model.Libro;
import com.upn.gestion.model.Prestamo;
import com.upn.gestion.service.LibroService;
import com.upn.gestion.service.PrestamoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections; // Para crear listas de un solo elemento

@Component
public class MainController {

    // --- ELEMENTOS VISUALES (FXML) ---

    // Vistas (Paneles)
    @FXML private VBox vistaLibros;
    @FXML private VBox vistaPrestamo;

    // Tabla Libros
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, Long> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, Integer> colStock;

    // Formulario Préstamo
    @FXML private TextField txtDniPrestamo;
    @FXML private TextField txtIdLibroPrestamo;
    @FXML private Label lblMensajePrestamo;

    // --- SERVICIOS (LÓGICA) ---
    @Autowired private LibroService libroService;
    @Autowired private PrestamoService prestamoService;

    @FXML
    public void initialize() {
        configurarTabla();
        mostrarVistaLibros(); // Iniciar viendo el catálogo
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    // --- NAVEGACIÓN DEL MENÚ ---

    @FXML
    public void mostrarVistaLibros() {
        vistaLibros.setVisible(true);
        vistaPrestamo.setVisible(false);
        cargarLibros();
    }

    @FXML
    public void mostrarVistaPrestamo() {
        vistaLibros.setVisible(false);
        vistaPrestamo.setVisible(true);
        lblMensajePrestamo.setText(""); // Limpiar mensajes anteriores
    }

    // --- ACCIONES (BOTONES) ---

    @FXML
    public void cargarLibros() {
        tablaLibros.setItems(FXCollections.observableArrayList(libroService.listarTodos()));
    }

    @FXML
    public void registrarPrestamoAction() {
        try {
            String dni = txtDniPrestamo.getText();
            String idLibroStr = txtIdLibroPrestamo.getText();

            // Validaciones básicas de interfaz
            if (dni.isEmpty() || idLibroStr.isEmpty()) {
                lblMensajePrestamo.setText(" Por favor complete todos los campos");
                lblMensajePrestamo.setStyle("-fx-text-fill: red;");
                return;
            }

            Long idLibro = Long.parseLong(idLibroStr);

            // LLAMADA AL SERVICIO (Aquí ocurre la magia de tu UML)
            // Enviamos una lista con un solo libro usando Collections.singletonList
            Prestamo nuevoPrestamo = prestamoService.registrarPrestamo(dni, Collections.singletonList(idLibro));

            // Éxito
            lblMensajePrestamo.setText(" Préstamo registrado con éxito. ID: " + nuevoPrestamo.getIdPrestamo());
            lblMensajePrestamo.setStyle("-fx-text-fill: green;");

            // Limpiar campos
            txtDniPrestamo.clear();
            txtIdLibroPrestamo.clear();

        } catch (NumberFormatException e) {
            lblMensajePrestamo.setText(" El ID del libro debe ser un número");
            lblMensajePrestamo.setStyle("-fx-text-fill: red;");
        } catch (RuntimeException e) {
            // Aquí atrapamos los errores del servicio (Ej: "No hay stock", "Usuario no existe")
            lblMensajePrestamo.setText("Error: " + e.getMessage());
            lblMensajePrestamo.setStyle("-fx-text-fill: red;");
        }
    }
}