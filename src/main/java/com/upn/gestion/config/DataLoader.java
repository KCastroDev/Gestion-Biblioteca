package com.upn.gestion.config; // O tu paquete correspondiente

import com.upn.gestion.model.*;
import com.upn.gestion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component // Le dice a Spring: "Carga esto al iniciar"
public class DataLoader implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private AutorRepository autorRepository;
    @Autowired private LibroRepository libroRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen datos para no duplicar
        if (categoriaRepository.count() == 0) {
            cargarDatosDePrueba();
        }
    }

    private void cargarDatosDePrueba() {
        // 1. Crear Categorías
        Categoria catIngenieria = new Categoria();
        catIngenieria.setNombre("Ingeniería de Sistemas");
        categoriaRepository.save(catIngenieria);

        Categoria catLiteratura = new Categoria();
        catLiteratura.setNombre("Literatura Clásica");
        categoriaRepository.save(catLiteratura);

        // 2. Crear Autores
        Autor autorRobert = new Autor();
        autorRobert.setNombre("Robert C. Martin");
        autorRobert.setNacionalidad("USA");
        autorRepository.save(autorRobert);

        Autor autorGabo = new Autor();
        autorGabo.setNombre("Gabriel García Márquez");
        autorGabo.setNacionalidad("Colombia");
        autorRepository.save(autorGabo);

        // 3. Crear Libros
        Libro libro1 = new Libro();
        libro1.setTitulo("Clean Code");
        libro1.setIsbn("978-0132350884");
        libro1.setStock(5);
        libro1.setDisponible(true);
        libro1.setCategoria(catIngenieria);
        libro1.setAutor(autorRobert);
        libroRepository.save(libro1);

        Libro libro2 = new Libro();
        libro2.setTitulo("Cien Años de Soledad");
        libro2.setIsbn("978-0307474728");
        libro2.setStock(3);
        libro2.setDisponible(true);
        libro2.setCategoria(catLiteratura);
        libro2.setAutor(autorGabo);
        libroRepository.save(libro2);

        // 4. Crear Usuarios
        Administrador admin = new Administrador();
        admin.setNombre("Carlos");
        admin.setApellidos("Admin");
        admin.setDni("12345678");
        admin.setEmail("admin@biblioteca.com");
        admin.setPassword("admin123"); // En realidad esto se encripta, pero para pruebas o
        admin.setCodigoTrabajador("ADM001");
        admin.setTurno("Mañana");
        usuarioRepository.save(admin);

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan");
        estudiante.setApellidos("Pérez");
        estudiante.setDni("87654321");
        estudiante.setEmail("juan@upn.pe");
        estudiante.setPassword("1234");
        estudiante.setCodigoEstudiante("N00123456");
        estudiante.setCarrera("Ingeniería de Sistemas");
        usuarioRepository.save(estudiante);

        System.out.println("DATOS DE PRUEBA CARGADOS EXITOSAMENTE");
    }
}