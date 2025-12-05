package com.upn.gestion.model;

import jakarta.persistence.*; // herramientas de base de datos
import lombok.Data;

@Data      //  getters, setters, toString automáticamente (Lombok)
@Entity    // Le dice a Spring que esto es una tabla en MySQL
public class Libro {

    @Id // Llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-incremental
    private Long idLibro;
    private String titulo;
    private String isbn;
    private Integer stock;
    private boolean disponible; // true = se puede prestar

    // --- RELACIONES

    // Un libro tiene UN autor
    // Spring creará una columna 'autor_id' en la base de datos
    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;

    // Un libro pertenece a UNA categoría
    // Spring creará una columna 'categoria_id' en la base de datos
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    // --- MÉTODOS DE NEGOCIO (
    //  lógica extra

    public void disminuirStock() {
        if (this.stock > 0) {
            this.stock--;
            if (this.stock == 0) {
                this.disponible = false;
            }
        }
    }

    public void aumentarStock() {
        this.stock++;
        this.disponible = true; // Si vuelve a haber stock, está disponible
    }
}