package com.upn.gestion.model;

public enum EstadoPrestamo {
    ACTIVO,      // El libro está en poder del usuario
    FINALIZADO,  // Ya lo devolvió
    CON_MORA     // Se pasó de la fecha
}