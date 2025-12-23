package com.aluracursos.literalura.principal;
// Paquete principal que contiene la clase que maneja la lógica del menú y la interacción con el usuario

import com.aluracursos.literalura.model.*;
// Importa todas las entidades y records del modelo (Libro, Autor, Idioma, Datos, etc.)

import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
// Repositorios JPA para acceder a la base de datos

import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
// Servicios para consumir la API externa y convertir JSON a objetos Java

import java.util.*;
import java.util.stream.Collectors;
// Utilidades de Java: listas, opcionales, estadísticas y streams

public class App {

    // Servicio que se encarga de hacer peticiones HTTP a la API Gutendex
    private final ConsumoAPI consumoAPI = new ConsumoAPI();

    // Servicio que convierte JSON en objetos Java usando Jackson
    private final ConvierteDatos convierteDatos = new ConvierteDatos();

    // Scanner para leer la entrada del usuario desde la consola
    private final Scanner sc = new Scanner(System.in);

    // URLs base para consumir la API de Gutendex
    private final String URL_BASE = "https://gutendex.com";
    private final String URL_BOOKS = "/books/";

    // Repositorio para gestionar libros en la base de datos
    private LibroRepository repository;

    // Repositorio para gestionar autores en la base de datos
    private AutorRepository autorRepository;

    // Menú visual que se muestra en consola
    private final String menu = ("""
┌───────────────────────────────────────────────────────────────────────────┐
│                       📚  SISTEMA LITERALURA  📚                           │
├───────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│   🔍  [1] Buscar libro por nombre                                         │ 
│   📖  [2] Listar libros registrados                                       │
│   ✍️  [3] Listar autores registrados                                      │
│   ⏳  [4] Autores vivos en un año específico                               │
│   🌍  [5] Listar libros por idioma                                         │
│   🏆  [6] Top 10 libros más descargados                                    │
│   📊  [7] Estadísticas generales de descargas                              │
│                                                                           │
├───────────────────────────────────────────────────────────────────────────┤
│   🚪  [0] Salir del sistema                                                │
└───────────────────────────────────────────────────────────────────────────┘
""");

    // Constructor que recibe los repositorios desde Spring
    public App(LibroRepository repository, AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    // Método principal que controla el flujo del programa
    public void aplicacionRun() {
        var opcion = -1;

        // Bucle principal del menú
        while (opcion != 0) {
            System.out.println(banner); // Banner ASCII (definido en otra parte)
            System.out.println(menu);
            System.out.print("| ➤ Seleccione una opción: ");

            try {
                // Validamos que el usuario ingrese un número
                if (sc.hasNextInt()) {
                    opcion = sc.nextInt();
                    sc.nextLine(); // Limpia el buffer

                    // Control de opciones del menú
                    switch (opcion) {
                        case 1 -> buscarLibroWeb();
                        case 2 -> listarLibros();
                        case 3 -> listarAutoresRegistrados();
                        case 4 -> buscarAutoresPorAnio();
                        case 5 -> listarLibrosPorIdioma();
                        case 6 -> top10LibrosMasDescargados();
                        case 7 -> obtenerEstadisticaDeLosLibros();
                        case 0 -> System.out.println("\n| - ¡Gracias por usar Literalura! Vuelve pronto.");
                        default -> System.out.println("\n| [!] Opción inválida.");
                    }

                    // Pausa para que el usuario pueda leer el resultado
                    if (opcion != 0) {
                        presionarEnterParaContinuar();
                    }

                } else {
                    // Manejo de entrada no numérica
                    System.out.println("\n| [!] Error: Formato incorrecto.");
                    sc.next();
                    presionarEnterParaContinuar();
                }
            } catch (Exception e) {
                // Captura de errores inesperados
                System.out.println("\n| [!] Error inesperado.");
                presionarEnterParaContinuar();
            }
        }
    }

    // ---------------- FUNCIONALIDAD: LISTAR LIBROS POR IDIOMA ----------------
    private void listarLibrosPorIdioma() {

        System.out.println("""
        |===================================================================================|
        |                🌐  FILTRANDO                      |
        |===================================================================================|""");

        System.out.print("| ➤ Ingrese el código (es, en, fr, pt): ");
        var codigo = sc.nextLine().trim().toLowerCase();

        try {
            // Convierte el código ingresado a un enum Idioma
            Idioma idiomaBusqueda = Idioma.fromString(codigo);

            System.out.println("| - Buscando libros en: " + idiomaBusqueda.getLenguajeEspanol() + "...");

            // Obtiene todos los libros desde la base de datos
            List<Libro> todosLosLibros = repository.findAll();

            // Filtra los libros que contengan el idioma solicitado
            List<Libro> librosFiltrados = todosLosLibros.stream()
                    .filter(l -> l.getIdiomas().stream()
                            .anyMatch(i -> i.name().equalsIgnoreCase(idiomaBusqueda.name())))
                    .collect(Collectors.toList());

            if (librosFiltrados.isEmpty()) {
                System.out.println("\n| [!] No se hallaron libros registrados en " + idiomaBusqueda.getLenguajeEspanol());
            } else {
                System.out.println("\n--- RESULTADOS PARA " + idiomaBusqueda.getLenguajeEspanol().toUpperCase() + " ---");
                librosFiltrados.forEach(this::mostrarDetallesLibroEntidad);
            }
        } catch (IllegalArgumentException e) {
            // Si el código no coincide con ningún idioma válido
            System.out.println("\n| [!] Código de idioma no reconocido: " + codigo);
        }
    }

    // ---------------- FUNCIONALIDAD: BUSCAR LIBRO EN LA WEB ----------------
    public void buscarLibroWeb() {

        System.out.println("""
        |===================================================================================|
        |                🔍 BUSCANDO NUEVOS                    |
        |===================================================================================|""");

        System.out.print("| - Ingrese el nombre del libro: ");
        String nombre = sc.nextLine();

        // Consume la API de Gutendex
        String json = consumoAPI.obtenerDatos(URL_BASE + URL_BOOKS + "?search=" + nombre.replace(" ", "+"));

        // Convierte el JSON a objeto Datos
        Datos datos = convierteDatos.obtenerDatos(json, Datos.class);

        // Busca el primer libro que coincida con el título
        Optional<DatosLibro> libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombre.toLowerCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();

            // Verifica si el libro ya existe en la base de datos
            Optional<Libro> libroExistente =
                    repository.findByTituloContainsIgnoreCase(datosLibro.titulo());

            if (libroExistente.isPresent()) {
                // Evita duplicados
                System.out.println("| ⚠️  EL LIBRO YA EXISTE EN LA BDD");
                mostrarDetallesLibroEntidad(libroExistente.get());
            } else {
                // Guarda el libro nuevo
                Libro libro = new Libro(datosLibro);
                repository.save(libro);
                System.out.println("\n| - ¡Libro encontrado y guardado con éxito!");
                mostrarDetallesLibroEntidad(libro);
            }
        } else {
            System.out.println("\n| [!] Libro no encontrado en la web.");
        }
    }

    // ---------------- MÉTODOS AUXILIARES ----------------

    // Muestra los detalles de un libro guardado en la BDD
    private void mostrarDetallesLibroEntidad(Libro libro) {

        String autores = (libro.getAutores() == null) ? "Desconocido" :
                libro.getAutores().stream()
                        .map(Autor::getNombre)
                        .collect(Collectors.joining(", "));

        String idiomas = (libro.getIdiomas() == null) ? "No disponible" :
                libro.getIdiomas().stream()
                        .map(Idioma::getLenguajeEspanol)
                        .collect(Collectors.joining(", "));

        System.out.println("\n| ---------- LIBRO N° " +
                (libro.getIdLibro() != null ? libro.getIdLibro() : "---") + " ----------");
        System.out.println("| Título: " + libro.getTitulo().toUpperCase());
        System.out.println("| Autor: " + autores);
        System.out.println("| Idioma: " + idiomas);
        System.out.println("| Descargas: " + libro.getNumeroDeDescargas().intValue());
        System.out.println("| ------------------------------------");
    }

    // Pausa el programa hasta que el usuario presione ENTER
    private void presionarEnterParaContinuar() {
        System.out.println("\n| ➤ Presione ENTER para continuar...");
        sc.nextLine();
    }
}