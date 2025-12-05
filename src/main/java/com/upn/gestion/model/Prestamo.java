package com.upn.gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.time.temporal.ChronoUnit;

@Data
@Entity
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrestamo;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;

    @Enumerated(EnumType.STRING) // Guarda el texto "ACTIVO" en la BD
    private EstadoPrestamo estado;

    private Double totalMulta;

    // RELACIÓN 1: Asociación con Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioSistema usuario;

    // RELACIÓN 2: COMPOSICIÓN
    // cascade es igual CascadeType. Si borras Prestamo, borra Detalles.
    // orphanRemoval = true - Si de quita un detalle de la lista, lo borra de la BD.
    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallePrestamo> detalles;

    // Calcular días de retraso
    public long calcularDiasRetraso() {
        if (fechaDevolucionReal != null) return 0; // Ya lo devolvió
        LocalDate hoy = LocalDate.now();

        if (hoy.isAfter(fechaDevolucionPrevista)) {
            return ChronoUnit.DAYS.between(fechaDevolucionPrevista, hoy);
        }
        return 0;
    }

    // Calcular Multa
    public double calcularMulta() {
        long dias = calcularDiasRetraso();
        return dias * 5.0;
    }
}