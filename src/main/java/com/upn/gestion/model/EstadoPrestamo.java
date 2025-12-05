package com.upn.gestion.model;

public enum EstadoPrestamo {
    ACTIVO,      // libro en préstamo
    FINALIZADO,  // Ya lo devolvió
    CON_MORA     // Se pasó de la fecha
}