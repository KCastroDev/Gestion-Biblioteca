package com.upn.gestion.service;

import com.upn.gestion.model.*;
import com.upn.gestion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    @Autowired private PrestamoRepository prestamoRepository;
    @Autowired private LibroRepository libroRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    // --- MÉTODO PRINCIPAL: REGISTRAR UN PRÉSTAMO ---
    @Transactional // Importante: Si algo falla, deshace todos los cambios (rollback)
    public Prestamo registrarPrestamo(String dniUsuario, List<Long> idsLibros) {

        // 1. Buscar al Usuario
        UsuarioSistema usuario = usuarioRepository.findByDni(dniUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dniUsuario));

        // 2. Crear la Cabecera del Préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionPrevista(LocalDate.now().plusDays(7)); // 7 días de plazo por defecto
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        // Lista para guardar los detalles
        List<DetallePrestamo> detalles = new ArrayList<>();

        // 3. Procesar cada libro solicitado
        for (Long idLibro : idsLibros) {
            Libro libro = libroRepository.findById(idLibro)
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado ID: " + idLibro));

            // VALIDACIÓN DE STOCK (Regla de Negocio)
            if (libro.getStock() <= 0) {
                throw new RuntimeException("El libro '" + libro.getTitulo() + "' no tiene stock disponible.");
            }

            // DISMINUIR STOCK (Actualizamos el libro)
            libro.disminuirStock();
            libroRepository.save(libro);

            // Crear el Detalle
            DetallePrestamo detalle = new DetallePrestamo();
            detalle.setLibro(libro);
            detalle.setPrestamo(prestamo); // Enlazamos con el padre
            detalle.setEstadoLibro("Buen Estado");

            detalles.add(detalle);
        }

        // 4. Asignar detalles al préstamo y guardar TODO en cascada
        prestamo.setDetalles(detalles);
        return prestamoRepository.save(prestamo);
    }

    // --- LISTAR PRÉSTAMOS ACTIVOS ---
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }
}