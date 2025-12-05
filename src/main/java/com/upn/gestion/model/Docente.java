package com.upn.gestion.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Docente extends UsuarioSistema {

    private String codigoDocente;
    private String especialidad;
    private String titulo;
}