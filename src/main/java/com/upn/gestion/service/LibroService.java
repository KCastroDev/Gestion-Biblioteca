package com.upn.gestion.service;

import com.upn.gestion.model.Libro;
import com.upn.gestion.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 1. Esto le dice a Spring: "Aquí hay lógica de negocio"
public class LibroService {

    @Autowired // 2. Inyección de Dependencias: Trae el repositorio automáticamente
    private LibroRepository libroRepository;

    // --- LISTAR ---
    public List<Libro> listarTodos() {
        return libroRepository.findAll(); // SQL: SELECT * FROM libro
    }

    // --- GUARDAR (Con validación) ---
    public Libro registrarLibro(Libro libro) {
        // Regla de Negocio 1: El stock no puede ser negativo
        if (libro.getStock() < 0) {
            // Aquí podrías lanzar una excepción o corregirlo
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        // Regla de Negocio 2: Validar que el título no esté vacío
        if (libro.getTitulo() == null || libro.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("El libro debe tener un título");
        }

        // Si pasa las validaciones, recién guardamos en BD
        return libroRepository.save(libro); // SQL: INSERT INTO ...
    }

    // --- BUSCAR ---
    public Libro buscarPorId(Long id) {
        return libroRepository.findById(id).orElse(null);
    }

    // --- ELIMINAR ---
    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }
}