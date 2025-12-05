package com.upn.gestion.controller;

import com.upn.gestion.model.Prestamo;
import com.upn.gestion.service.PrestamoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
public class VistaPrestamoController {

    // pestaña 1: REGISTRAR PRÉSTAMO
    @FXML private TextField txtCodigoPrestamo;
    @FXML private TextField txtIdLibroPrestamo;
    @FXML private DatePicker dpFechaDevolucion;
    @FXML private Label lblMensaje;

    // pestaña 2: HISTORIAL DE PRÉSTAMOS
    @FXML private TableView<Prestamo> tablaPrestamos;
    @FXML private TableColumn<Prestamo, Long> colId;
    @FXML private TableColumn<Prestamo, String> colEstudiante;
    @FXML private TableColumn<Prestamo, String> colFecha;
    @FXML private TableColumn<Prestamo, String> colVencimiento;
    @FXML private TableColumn<Prestamo, String> colEstado;

    @Autowired
    private PrestamoService prestamoService;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarPrestamos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucionPrevista"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Mostramos nombre del estudiante
        colEstudiante.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getUsuario().getNombre() + " " +
                                cellData.getValue().getUsuario().getApellidos()
                )
        );
    }

    @FXML
    public void cargarPrestamos() {
        // Usamos listarPendientes() para NO ver los ya devueltos
        tablaPrestamos.setItems(FXCollections.observableArrayList(prestamoService.listarPendientes()));
    }

    // --- ACCIÓN: PRESTAR LIBRO ---
    @FXML
    public void registrarPrestamoAction() {
        try {
            String codigo = txtCodigoPrestamo.getText();
            String idLibroStr = txtIdLibroPrestamo.getText();
            LocalDate fechaDevolucion = dpFechaDevolucion.getValue();

            // Validaciones
            if (codigo.isEmpty() || idLibroStr.isEmpty() || fechaDevolucion == null) {
                mostrarMensaje("Complete todos los campos y la fecha", true);
                return;
            }

            if (fechaDevolucion.isBefore(LocalDate.now()) || fechaDevolucion.isEqual(LocalDate.now())) {
                mostrarMensaje("La fecha de devolución debe ser futura", true);
                return;
            }

            Long idLibro = Long.parseLong(idLibroStr);

            // Llamamos al servicio
            prestamoService.registrarPrestamo(codigo, Collections.singletonList(idLibro), fechaDevolucion);

            mostrarMensaje("Préstamo registrado con éxito", false);

            // Limpiamos campos
            txtCodigoPrestamo.clear();
            txtIdLibroPrestamo.clear();
            dpFechaDevolucion.setValue(null);

            // Actualizar tabla automáticamente
            cargarPrestamos();

        } catch (NumberFormatException e) {
            mostrarMensaje("El ID del libro debe ser un número", true);
        } catch (RuntimeException e) {
            mostrarMensaje("Error: " + e.getMessage(), true);
        }
    }

    // ---  DEVOLVER LIBRO ---
    @FXML
    public void procesarDevolucionAction() {
        Prestamo seleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atención");
            alert.setContentText("Seleccione un préstamo de la tabla para devolver.");
            alert.showAndWait();
            return;
        }

        try {
            Prestamo actualizado = prestamoService.registrarDevolucion(seleccionado.getIdPrestamo());

            String info = "Devolución registrada correctamente.";
            if (actualizado.getTotalMulta() > 0) {
                info += "\n SE GENERÓ MULTA: S/ " + actualizado.getTotalMulta();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Libro devuelto al stock");
            alert.setContentText(info);
            alert.showAndWait();

            // Al refrescar, el libro devuelto DESAPARECERÁ de la lista
            cargarPrestamos();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}