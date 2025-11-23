package com.upn.gestion.repository;

import com.upn.gestion.model.Prestamo;
import com.upn.gestion.model.EstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // --- MÉTODOS  DE SPRING DATA ---

    // Buscar préstamos por DNI del usuario (Útil para ver historial)
    List<Prestamo> findByUsuarioDni(String dni);

    // Buscar préstamos por Estado (Útil para reportes de "Pendientes" o "Con Mora")
    List<Prestamo> findByEstado(EstadoPrestamo estado);

    // Buscar todos los préstamos de una fecha específica
    //List<Prestamo> findByFechaPrestamo(LocalDate fecha);
}