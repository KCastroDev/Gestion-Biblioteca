package com.upn.gestion.controller;

import com.upn.gestion.model.DetallePrestamo;
import com.upn.gestion.model.Prestamo;
import com.upn.gestion.model.UsuarioSistema;
import com.upn.gestion.service.PrestamoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistorialController {

    @FXML private TableView<Prestamo> tablaHistorial;
    @FXML private TableColumn<Prestamo, Long> colId;
    @FXML private TableColumn<Prestamo, String> colEstudiante;
    @FXML private TableColumn<Prestamo, String> colLibro;
    @FXML private TableColumn<Prestamo, String> colFechaPrestamo;
    @FXML private TableColumn<Prestamo, String> colFechaDevolucion;
    @FXML private TableColumn<Prestamo, Double> colMulta;

    @Autowired
    private PrestamoService prestamoService;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));

        // 1. Fecha Préstamo
        colFechaPrestamo.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getFechaPrestamo().toString()));

        // 2. Fecha Devolución Real y Muestra "Pendiente" si aún no devuelve, aunque en historial ya debería estar devuelto
        colFechaDevolucion.setCellValueFactory(cell -> {
            if (cell.getValue().getFechaDevolucionReal() != null) {
                return new SimpleStringProperty(cell.getValue().getFechaDevolucionReal().toString());
            }
            return new SimpleStringProperty("Pendiente");
        });

        // 3. Multa
        colMulta.setCellValueFactory(new PropertyValueFactory<>("totalMulta"));

        // 4. Nombre Estudiante (Navegamos Prestamo -> Usuario)
        colEstudiante.setCellValueFactory(cell -> {
            UsuarioSistema usuario = cell.getValue().getUsuario();
            return new SimpleStringProperty(usuario.getNombre() + " " + usuario.getApellidos());
        });

        // 5. Título del Libro (CORREGIDO)
        // Como 'detalles' es una lista, unimos los títulos con comas si hay más de uno.
        colLibro.setCellValueFactory(cell -> {
            List<DetallePrestamo> detalles = cell.getValue().getDetalles();

            if (detalles != null && !detalles.isEmpty()) {
                // convertimos la lista de libros en un texto: "Libro1, Libro2, ..."
                String titulos = detalles.stream()
                        .map(d -> d.getLibro().getTitulo())
                        .collect(Collectors.joining(", "));
                return new SimpleStringProperty(titulos);
            }
            return new SimpleStringProperty("Sin Info");
        });
    }

    // Método público para refrescar la tabla
    public void cargarDatos() {
        // Usamos el método que creamos en tu PrestamoService
        tablaHistorial.setItems(FXCollections.observableArrayList(prestamoService.listarHistorial()));
    }
}