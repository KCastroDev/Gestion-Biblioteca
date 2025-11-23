package com.upn.gestion.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutor; // Agregamos ID para la base de datos

    private String nombre;
    private String nacionalidad;


}