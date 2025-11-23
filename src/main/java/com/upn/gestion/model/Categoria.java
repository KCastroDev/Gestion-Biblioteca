package com.upn.gestion.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria; // Agregamos ID

    private String nombre; // Ej: "Ingeniería", "Literatura", "Terror"
}