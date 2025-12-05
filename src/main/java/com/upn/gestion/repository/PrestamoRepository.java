package com.upn.gestion.repository;
import java.util.Collection;
import com.upn.gestion.model.Prestamo;
import com.upn.gestion.model.EstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    // Buscar préstamos por DNI del usuario
    List<Prestamo> findByUsuarioDni(String dni);
    // Buscar préstamos por Estado
    List<Prestamo> findByEstado(EstadoPrestamo estado);

    List<Prestamo> findByEstadoIn(Collection<EstadoPrestamo> estados);

}