package com.upn.gestion.controller;

import com.upn.gestion.model.Autor;
import com.upn.gestion.model.Categoria;
import com.upn.gestion.model.Libro;
import com.upn.gestion.service.AutorService;
import com.upn.gestion.service.CategoriaService;
import com.upn.gestion.service.LibroService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VistaLibrosController {

    @FXML private HBox panelHeader;
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, Long> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, Integer> colStock;
    @FXML private TextField txtBuscar;

    // FORMULARIO
    @FXML private VBox panelRegistro;
    @FXML private Label lblTituloFormulario;
    @FXML private Button btnGuardar;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtStock;
    @FXML private ComboBox<Autor> cmbAutor;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private Label lblMensajeRegistro;

    @Autowired private LibroService libroService;
    @Autowired private AutorService autorService;
    @Autowired private CategoriaService categoriaService;

    private ObservableList<Libro> listaLibrosMaster = FXCollections.observableArrayList();
    private Libro libroEnEdicion = null;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarLibros();
        cargarAutoresYCategorias();
        mostrarCatalogo();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    @FXML
    public void cargarLibros() {
        listaLibrosMaster.setAll(libroService.listarTodos());
        FilteredList<Libro> datosFiltrados = new FilteredList<>(listaLibrosMaster, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            datosFiltrados.setPredicate(libro -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return libro.getTitulo().toLowerCase().contains(lowerCaseFilter) ||
                        libro.getIsbn().toLowerCase().contains(lowerCaseFilter) ||
                        (libro.getAutor() != null && libro.getAutor().getNombre().toLowerCase().contains(lowerCaseFilter));
            });
        });

        SortedList<Libro> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tablaLibros.comparatorProperty());
        tablaLibros.setItems(datosOrdenados);
    }

    // --- AGREGAR NUEVO LIBRO ---
    @FXML
    public void mostrarVistaRegistroLibro() {
        libroEnEdicion = null;
        limpiarFormulario();


        lblTituloFormulario.setText("Registrar Nuevo Libro");
        btnGuardar.setText(" Guardar Libro");
        btnGuardar.setStyle("-fx-background-color: #3498db;"); // Azul

        cambiarVista(true);
    }

    // --- EDITAR LIBRO ---
    @FXML
    public void editarLibroAction() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un libro para editar.");
            return;
        }

        libroEnEdicion = seleccionado;

        //  campos
        txtTitulo.setText(seleccionado.getTitulo());
        txtIsbn.setText(seleccionado.getIsbn());
        txtStock.setText(String.valueOf(seleccionado.getStock()));
        cmbAutor.getSelectionModel().select(seleccionado.getAutor());
        cmbCategoria.getSelectionModel().select(seleccionado.getCategoria());

        // CAMBIO  DE TEXTOS
        lblTituloFormulario.setText("Editar Libro #" + seleccionado.getIdLibro());
        btnGuardar.setText(" Actualizar Datos");
        btnGuardar.setStyle("-fx-background-color: #f39c12;"); // color del botón naranja
        lblMensajeRegistro.setText("");

        cambiarVista(true);
    }

    @FXML
    public void guardarLibro() {
        try {
            if (txtTitulo.getText().isEmpty() || txtStock.getText().isEmpty()) {
                lblMensajeRegistro.setText("Complete los campos obligatorios.");
                lblMensajeRegistro.setStyle("-fx-text-fill: red;");
                return;
            }

            int stock = Integer.parseInt(txtStock.getText());

            Libro libro;
            boolean esEdicion = (libroEnEdicion != null);

            if (esEdicion) {
                libro = libroEnEdicion;
            } else {
                libro = new Libro();
            }

            libro.setTitulo(txtTitulo.getText());
            libro.setIsbn(txtIsbn.getText());
            libro.setStock(stock);
            libro.setDisponible(stock > 0);
            libro.setAutor(cmbAutor.getValue());
            libro.setCategoria(cmbCategoria.getValue());

            libroService.registrarLibro(libro);

            // Mensaje de éxito
            String mensajeExito = esEdicion ? "Libro editado correctamente." : "Libro registrado correctamente.";
            mostrarAlerta(Alert.AlertType.INFORMATION, mensajeExito);

            volverACatalogo();

        } catch (NumberFormatException e) {
            lblMensajeRegistro.setText("El stock debe ser un número.");
            lblMensajeRegistro.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            lblMensajeRegistro.setText("Error: " + e.getMessage());
            lblMensajeRegistro.setStyle("-fx-text-fill: red;");
        }
    }
    // --- ELIMINAR LIBRO ---
    @FXML public void eliminarLibro() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un libro."); return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Eliminar '" + seleccionado.getTitulo() + "'?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                libroService.eliminarLibro(seleccionado.getIdLibro());
                cargarLibros();
            } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR, "No se puede eliminar."); }
        }
    }

    private void cambiarVista(boolean mostrarFormulario) {
        panelHeader.setVisible(!mostrarFormulario); panelHeader.setManaged(!mostrarFormulario);
        tablaLibros.setVisible(!mostrarFormulario); tablaLibros.setManaged(!mostrarFormulario);
        panelRegistro.setVisible(mostrarFormulario); panelRegistro.setManaged(mostrarFormulario);
    }

    @FXML public void volverACatalogo() {
        libroEnEdicion = null;
        cambiarVista(false);
        cargarLibros();
    }

    private void mostrarCatalogo() { cambiarVista(false); }

    private void cargarAutoresYCategorias() {
        cmbAutor.setItems(FXCollections.observableArrayList(autorService.listarTodos()));
        cmbCategoria.setItems(FXCollections.observableArrayList(categoriaService.listarTodos()));

        StringConverter<Autor> ac = new StringConverter<>() {
            @Override public String toString(Autor o) { return o!=null?o.getNombre():""; }
            @Override public Autor fromString(String s) { return null; }
        };
        cmbAutor.setConverter(ac);

        StringConverter<Categoria> cc = new StringConverter<>() {
            @Override public String toString(Categoria o) { return o!=null?o.getNombre():""; }
            @Override public Categoria fromString(String s) { return null; }
        };
        cmbCategoria.setConverter(cc);
    }

    private void limpiarFormulario() {
        txtTitulo.clear(); txtIsbn.clear(); txtStock.clear();
        cmbAutor.getSelectionModel().clearSelection();
        cmbCategoria.getSelectionModel().clearSelection();
        lblMensajeRegistro.setText("");
    }

    private void mostrarAlerta(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML public void nuevoAutorRapido() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo Autor");
        dialog.setHeaderText("Crear Autor");
        dialog.setContentText("Nombre:");
        dialog.showAndWait().ifPresent(nombre -> {
            if (!nombre.isBlank()) {
                Autor a = new Autor(); a.setNombre(nombre);
                Autor guardado = autorService.registrarAutor(a);
                cargarAutoresYCategorias();
                cmbAutor.getSelectionModel().select(guardado);
            }
        });
    }

    @FXML public void nuevaCategoriaRapida() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Categoría");
        dialog.setHeaderText("Crear Categoría");
        dialog.setContentText("Nombre:");
        dialog.showAndWait().ifPresent(nombre -> {
            if (!nombre.isBlank()) {
                Categoria c = new Categoria(); c.setNombre(nombre);
                Categoria guardada = categoriaService.registrarCategoria(c);
                cargarAutoresYCategorias();
                cmbCategoria.getSelectionModel().select(guardada);
            }
        });
    }
}