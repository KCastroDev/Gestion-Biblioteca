package com.upn.gestion.repository;

import org.springframework.stereotype.Repository;
import com.upn.gestion.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByTitulo(String titulo);
    List<Libro> findByStockGreaterThan(Integer cantidad);
}
