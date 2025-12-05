package com.upn.gestion.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String dni;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    // Agregado según nuestra auditoría de seguridad
    private String password;
}