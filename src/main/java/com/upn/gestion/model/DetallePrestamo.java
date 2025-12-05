package com.upn.gestion.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DetallePrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    // Relación con el Libro (Un detalle mira a un libro)
    @ManyToOne
    @JoinColumn(name = "id_libro")
    private Libro libro;

    // Relación Inversa con Prestamo (para que Spring sepa quién es el padre)
    @ManyToOne
    @JoinColumn(name = "id_prestamo")
    private Prestamo prestamo;
    private String estadoLibro;
}