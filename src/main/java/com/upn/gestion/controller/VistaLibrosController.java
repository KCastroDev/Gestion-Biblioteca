package com.upn.gestion.controller;

import com.upn.gestion.model.Autor;
import com.upn.gestion.model.Categoria;
import com.upn.gestion.model.Libro;
import com.upn.gestion.service.AutorService;
import com.upn.gestion.service.CategoriaService;
import com.upn.gestion.service.LibroService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistaLibrosController {

    // Vista catálogo
    @FXML private HBox panelHeader;
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, Long> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, Integer> colStock;

    // Vista Registro
    @FXML private VBox panelRegistro;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtStock;
    @FXML private ComboBox<Autor> cmbAutor;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private Label lblMensajeRegistro;

    @Autowired
    private LibroService libroService;
    @Autowired
    private AutorService autorService;
    @Autowired
    private CategoriaService categoriaService;

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
        tablaLibros.setItems(
                FXCollections.observableArrayList(libroService.listarTodos())
        );
    }

    private void cargarAutoresYCategorias() {
        cmbAutor.setItems(FXCollections.observableArrayList(autorService.listarTodos()));
        cmbCategoria.setItems(FXCollections.observableArrayList(categoriaService.listarTodas()));

        // cómo se muestran en el combo (toString de Lombok usa todos los campos, mejor solo el nombre)
        cmbAutor.setCellFactory(listView -> new ListCell<Autor>() {
            @Override
            protected void updateItem(Autor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
            }
        });
        cmbAutor.setButtonCell(new ListCell<Autor>() {
            @Override
            protected void updateItem(Autor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
            }
        });

        cmbCategoria.setCellFactory(listView -> new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
            }
        });
        cmbCategoria.setButtonCell(new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
            }
        });
    }

    // -------- Cambio de "vista" dentro del mismo FXML --------

    @FXML
    public void mostrarVistaRegistroLibro() {
        // Ocultar catálogo
        panelHeader.setVisible(false);
        panelHeader.setManaged(false);
        tablaLibros.setVisible(false);
        tablaLibros.setManaged(false);

        // Mostrar formulario
        panelRegistro.setVisible(true);
        panelRegistro.setManaged(true);

        // Limpiar campos y mensaje
        txtTitulo.clear();
        txtIsbn.clear();
        txtStock.clear();
        cmbAutor.getSelectionModel().clearSelection();
        cmbCategoria.getSelectionModel().clearSelection();
        lblMensajeRegistro.setText("");
    }

    @FXML
    public void volverACatalogo() {
        mostrarCatalogo();
        cargarLibros();
    }

    private void mostrarCatalogo() {
        panelHeader.setVisible(true);
        panelHeader.setManaged(true);
        tablaLibros.setVisible(true);
        tablaLibros.setManaged(true);

        panelRegistro.setVisible(false);
        panelRegistro.setManaged(false);
    }

    // -- Lógica de guardado --

    @FXML
    public void guardarLibro() {
        String titulo = txtTitulo.getText();
        String isbn = txtIsbn.getText();
        String stockStr = txtStock.getText();
        Autor autorSeleccionado = cmbAutor.getValue();
        Categoria categoriaSeleccionada = cmbCategoria.getValue();

        if (titulo.isEmpty() || isbn.isEmpty() || stockStr.isEmpty()) {
            lblMensajeRegistro.setText("Complete título, ISBN y stock.");
            lblMensajeRegistro.setStyle("-fx-text-fill: red;");
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            lblMensajeRegistro.setText("El stock debe ser un número.");
            lblMensajeRegistro.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setIsbn(isbn);
            libro.setStock(stock);
            libro.setDisponible(stock > 0);

            if (autorSeleccionado != null) {
                libro.setAutor(autorSeleccionado);
            }
            if (categoriaSeleccionada != null) {
                libro.setCategoria(categoriaSeleccionada);
            }

            libroService.registrarLibro(libro);

            lblMensajeRegistro.setText("Libro registrado correctamente.");
            lblMensajeRegistro.setStyle("-fx-text-fill: green;");

            txtTitulo.clear();
            txtIsbn.clear();
            txtStock.clear();
            cmbAutor.getSelectionModel().clearSelection();
            cmbCategoria.getSelectionModel().clearSelection();

        } catch (IllegalArgumentException ex) {
            lblMensajeRegistro.setText(ex.getMessage());
            lblMensajeRegistro.setStyle("-fx-text-fill: red;");
        }
    }

    // -- Botones "+" para crear rápido --

    @FXML
    public void nuevoAutorRapido() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo Autor");
        dialog.setHeaderText("Registrar nuevo autor");
        dialog.setContentText("Nombre del autor:");

        dialog.showAndWait().ifPresent(nombre -> {
            if (nombre == null || nombre.isBlank()) {
                lblMensajeRegistro.setText("El nombre del autor no puede estar vacío.");
                lblMensajeRegistro.setStyle("-fx-text-fill: red;");
                return;
            }
            try {
                Autor autor = new Autor();
                autor.setNombre(nombre.trim());
                Autor guardado = autorService.registrarAutor(autor);
                cargarAutoresYCategorias(); // recargar listas
                cmbAutor.getSelectionModel().select(guardado);
                lblMensajeRegistro.setText("Autor registrado.");
                lblMensajeRegistro.setStyle("-fx-text-fill: green;");
            } catch (IllegalArgumentException ex) {
                lblMensajeRegistro.setText(ex.getMessage());
                lblMensajeRegistro.setStyle("-fx-text-fill: red;");
            }
        });
    }

    @FXML
    public void nuevaCategoriaRapida() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Categoría");
        dialog.setHeaderText("Registrar nueva categoría");
        dialog.setContentText("Nombre de la categoría:");

        dialog.showAndWait().ifPresent(nombre -> {
            if (nombre == null || nombre.isBlank()) {
                lblMensajeRegistro.setText("El nombre de la categoría no puede estar vacío.");
                lblMensajeRegistro.setStyle("-fx-text-fill: red;");
                return;
            }
            try {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombre.trim());
                Categoria guardada = categoriaService.registrarCategoria(categoria);
                cargarAutoresYCategorias(); // recargar listas
                cmbCategoria.getSelectionModel().select(guardada);
                lblMensajeRegistro.setText("Categoría registrada.");
                lblMensajeRegistro.setStyle("-fx-text-fill: green;");
            } catch (IllegalArgumentException ex) {
                lblMensajeRegistro.setText(ex.getMessage());
                lblMensajeRegistro.setStyle("-fx-text-fill: red;");
            }
        });
    }
}