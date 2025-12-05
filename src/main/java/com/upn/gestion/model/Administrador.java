package com.upn.gestion.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Administrador extends UsuarioSistema {

    private String codigoTrabajador;
    private String turno;
}