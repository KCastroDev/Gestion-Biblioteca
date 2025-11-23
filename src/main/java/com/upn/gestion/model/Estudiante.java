package com.upn.gestion.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // Incluye los atributos del padre en el equals
@Entity
public class Estudiante extends UsuarioSistema {

    private String codigoEstudiante;
    private String carrera;

}