package com.upn.gestion.model;

import jakarta.persistence.*; // Importa las herramientas de base de datos
import lombok.Data;          // Importa la herramienta para ahorrar código

@Data      // 1. Crea getters, setters, toString automáticamente (Lombok)
@Entity    // 2. Le dice a Spring que esto es una tabla en MySQL
public class Libro {

    @Id // 3. Llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental
    private Long idLibro;

    private String titulo;
    private String isbn;

    // --- ATRIBUTOS QUE FALTABAN (Del Diagrama) ---
    private Integer stock;
    private boolean disponible; // true = se puede prestar

    // --- RELACIONES (Del Diagrama: Rombo Blanco) ---

    // Un libro tiene UN autor (Simplificación inicial)
    // Spring creará una columna 'autor_id' en la base de datos
    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;

    // Un libro pertenece a UNA categoría
    // Spring creará una columna 'categoria_id' en la base de datos
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;


    // --- MÉTODOS DE NEGOCIO (Del Diagrama) ---
    // Lombok ya hizo los getters/setters, aquí ponemos la lógica extra

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