package com.upn.gestion.repository;

import com.upn.gestion.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    // --- MÉTODOS  DE SPRING DATA ---
    // Genera automáticamente el SQL: SELECT * FROM estudiante WHERE codigo_estudiante
    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);
}