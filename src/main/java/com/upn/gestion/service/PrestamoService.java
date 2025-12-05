package com.upn.gestion.service;

import com.upn.gestion.model.*;
import com.upn.gestion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired private PrestamoRepository prestamoRepository;
    @Autowired private LibroRepository libroRepository;
    @Autowired private EstudianteRepository estudianteRepository;

    //  REGISTRAR PRÉSTAMO
    @Transactional
    public Prestamo registrarPrestamo(String codigoEstudiante, List<Long> idsLibros, LocalDate fechaDevolucion) {

        // Buscar al Estudiante por su CÓDIGO
        Estudiante estudiante = estudianteRepository.findByCodigoEstudiante(codigoEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con Código: " + codigoEstudiante));

        //  Crear la Cabecera del Préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(estudiante);
        prestamo.setFechaPrestamo(LocalDate.now()); // Fecha actual
        prestamo.setFechaDevolucionPrevista(fechaDevolucion); // Fecha elegida en el DatePicker
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        // Lista para guardar los detalles
        List<DetallePrestamo> detalles = new ArrayList<>();

        //  Procesar cada libro solicitado
        for (Long idLibro : idsLibros) {
            Libro libro = libroRepository.findById(idLibro)
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado ID: " + idLibro));

            // VALIDACIÓN DE STOCK
            if (libro.getStock() <= 0) {
                throw new RuntimeException("El libro '" + libro.getTitulo() + "' no tiene stock disponible.");
            }

            // DISMINUIR STOCK
            libro.disminuirStock();
            libroRepository.save(libro);

            // Crear el Detalle
            DetallePrestamo detalle = new DetallePrestamo();
            detalle.setLibro(libro);
            detalle.setPrestamo(prestamo);
            detalle.setEstadoLibro("Buen Estado");

            detalles.add(detalle);
        }

        //  Asignar detalles y guardar
        prestamo.setDetalles(detalles);
        return prestamoRepository.save(prestamo);
    }

    // REGISTRAR DEVOLUCIÓN
    @Transactional
    public Prestamo registrarDevolucion(Long idPrestamo) {
        //  Buscar el préstamo
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado ID: " + idPrestamo));

        // Validar que no esté ya devuelto
        if (prestamo.getFechaDevolucionReal() != null) {
            throw new RuntimeException("Este préstamo ya fue devuelto anteriormente.");
        }

        //  Registrar fecha real
        prestamo.setFechaDevolucionReal(LocalDate.now());

        // Calcular Mora
        double multa = prestamo.calcularMulta();
        prestamo.setTotalMulta(multa);

        // Actualizar Estado
        if (multa > 0) {
            prestamo.setEstado(EstadoPrestamo.CON_MORA);
        } else {
            prestamo.setEstado(EstadoPrestamo.FINALIZADO);
        }

        //  DEVOLVER EL STOCK
        for (DetallePrestamo detalle : prestamo.getDetalles()) {
            Libro libro = detalle.getLibro();
            libro.aumentarStock();
            libroRepository.save(libro);
        }

        // Guardar cambios
        return prestamoRepository.save(prestamo);
    }

    // MÉTODOS DE LISTADO - FILTROS

    // Trae TODOS los préstamos
    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }

    //  Solo trae lo que falta devolver (ACTIVO o CON_MORA)
    public List<Prestamo> listarPendientes() {
        return prestamoRepository.findByEstadoIn(List.of(EstadoPrestamo.ACTIVO, EstadoPrestamo.CON_MORA));
    }

    //  Historial de préstamos finalizados
    public List<Prestamo> listarHistorial() {
        return prestamoRepository.findByEstado(EstadoPrestamo.FINALIZADO);
    }
}