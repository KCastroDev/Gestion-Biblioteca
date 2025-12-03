# Sistema de Gestión de Biblioteca (JavaFX + Spring Boot)

Proyecto académico de una **aplicación de escritorio** para la gestión básica de una biblioteca, desarrollado en **Java**, combinando:

- **JavaFX** para la interfaz gráfica (login, ventana principal).
- **Spring Boot** para la lógica de negocio, servicios y acceso a datos.

## 1. Funcionalidades principales

- **Autenticación** de usuarios del sistema (administrador, docente, estudiante).
- **Gestión de libros**:
  - Listado de libros en una tabla.
  - Visualización de título, ISBN y stock.
- **Gestión de préstamos**:
  - Registro de préstamo de uno o varios libros a un usuario.
  - Disminución automática de stock.
  - Cálculo de fechas de préstamo y devolución prevista.
  - Cálculo de retraso y multa (en el modelo).
- **Inicialización de datos de prueba** (usuarios, libros, autores, categorías).

> El alcance está enfocado a un **gestor de biblioteca básico** para fines de práctica de Programación Orientada a Objetos.

---

## 2. Tecnologías utilizadas

- **Java 17** (o compatible con JavaFX utilizado)
- **JavaFX**
- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **Base de datos en memoria / configurada en `application.properties`** (por defecto H2 o la que se defina)
- **Maven** (usa *wrapper* `mvnw` incluido en el proyecto)

---

## 3. Estructura del proyecto

```
src/
 ├─ main/
 │   ├─ java/
 │   │   └─ com/upn/gestion/
 │   │       ├─ GestionBibliotecaApplication.java   (clase principal JavaFX + Spring)
 │   │       ├─ BibliotecaLauncher.java             (puede usarse como launcher alterno)
 │   │       ├─ config/                             (configuración Spring + JavaFX)
 │   │       │    ├─ DataLoader.java                (carga datos iniciales)
 │   │       │    └─ StageInitializer.java          (integra JavaFX con Spring)
 │   │       ├─ controller/                         (controladores de la UI JavaFX)
 │   │       │    ├─ LoginController.java
 │   │       │    ├─ MainController.java
 │   │       │    └─ PrestamoController.java
 │   │       ├─ model/                              (entidades del dominio)
 │   │       │    ├─ Libro.java, Prestamo.java, DetallePrestamo.java, ...
 │   │       │    ├─ UsuarioSistema.java, Estudiante.java, Docente.java, Administrador.java
 │   │       │    └─ EstadoPrestamo.java, Autor.java, Categoria.java
 │   │       ├─ repository/                         (repositorios JPA)
 │   │       │    ├─ LibroRepository.java
 │   │       │    ├─ PrestamoRepository.java
 │   │       │    ├─ UsuarioRepository.java
 │   │       │    ├─ AutorRepository.java
 │   │       │    └─ CategoriaRepository.java
 │   │       └─ service/                            (servicios de negocio)
 │   │            ├─ LibroService.java
 │   │            ├─ PrestamoService.java
 │   │            └─ UsuarioService.java
 │   └─ resources/
 │       ├─ application.properties
 │       ├─ css/styles.css
 │       └─ fxml/
 │           ├─ login.fxml
 │           └─ main.fxml
 └─ test/
     └─ java/com/upn/gestion/GestionBibliotecaApplicationTests.java
```

---

## 4. Requisitos previos

1. **Java JDK 17** (recomendado) instalado y configurado en `PATH`.

   Comprobar:
   ```bash
   java -version
   ```

2. **Maven** (si no quieres usar el wrapper `mvnw`):
   ```bash
   mvn -version
   ```

3. **JavaFX**: las dependencias están gestionadas por Maven, no necesitas instalarlo aparte, pero sí usar un JDK compatible.

4. **IDE recomendado**: Visual Studio Code, IntelliJ IDEA o Eclipse.

---

## 5. Cómo compilar y ejecutar

### 5.1. Opción A: Desde la línea de comandos (Windows)

1. Abrir una terminal en la carpeta raíz del proyecto:

   ```bash
   cd "c:\Users\Une\OneDrive\Estudios\UPN\CURSOS\CICLO 5\Programación Orientada a Objetos\Gestion-Biblioteca"
   ```

2. Limpiar y compilar:

   Con Maven instalado:
   ```bash
   mvn clean package
   ```
   O usando el wrapper del proyecto:
   ```bash
   .\mvnw clean package
   ```

3. Ejecutar la aplicación:


   ```bash
   mvn javafx:run
   ```

---


## 6. Uso básico de la aplicación

1. **Login**:
   - Usa uno de los usuarios que se cargan en `DataLoader.java` (correo y contraseña).
   - Si las credenciales son válidas, se cierra la ventana de login y se abre la ventana principal.

2. **Catálogo de libros**:
   - Se muestran en una tabla (`TableView`) con sus columnas (ID, Título, ISBN, Stock).
   - Se refresca desde la base de datos a través de `LibroService`.

3. **Registro de préstamos**:
   - Ingresar el DNI del usuario y el ID del libro.
   - El sistema valida existencia de usuario, existencia de libro y stock disponible.
   - Si todo es correcto, se registra el préstamo y se disminuye el stock del libro.

---

## 7. Manejo de errores y colecciones

- Validaciones de negocio realizadas en los *services* (por ejemplo, stock no negativo, usuario existente, etc.).
- Manejo de excepciones en controladores para mostrar mensajes en la interfaz.
- Uso de:
  - `List<>` para colecciones de entidades (e.g. detalles de préstamo).
  - `ObservableList<>` para poblar tablas JavaFX.
  - `Optional<>` en repositorios Spring Data JPA para evitar `NullPointerException`.

---

## 8. Ejecución de tests

Para ejecutar las pruebas unitarias/integración:

```bash
mvn test
```

o

```bash
.\mvnw test
```

---

## 9. Notas y problemas comunes

- Si `mvn spring-boot:run` falla con un error sobre el método `main`, es porque la clase principal `GestionBibliotecaApplication` está pensada para lanzarse como **JavaFX Application** (extendiendo `javafx.application.Application`). En ese caso, ejecuta directamente la clase desde el IDE o asegúrate de que el método `main` tiene la firma correcta (`public static void main(String[] args)` y llama a `launch(args)`).
- Asegúrate de usar un **JDK**, no solo un JRE, para que JavaFX y Spring Boot funcionen correctamente.