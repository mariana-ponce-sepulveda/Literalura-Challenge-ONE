package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.util.ArrayList; // Se usa explícitamente para asegurar listas mutables
import java.util.List;
import java.util.stream.Collectors;

/*
 Entidad JPA que representa un Libro en la base de datos.
 Se encarga de:
 ✔ almacenar la información persistente del libro
 ✔ relacionarse con autores e idiomas
 ✔ mapear datos provenientes de la API (DatosLibro → Libro)
*/
@Entity
@Table(name = "libros")
public class Libro {

    /*
     ID primario de la tabla "libros".
     Es generado automáticamente por la base de datos.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro;

    /*
     ID del libro proveniente de la API Gutendex.
     No es clave primaria en la base de datos.
    */
    private Integer id;

    /*
     Título del libro.
     Se marca como único para evitar duplicados en la base de datos.
    */
    @Column(unique = true)
    private String titulo;

    /*
     Relación uno a muchos con Autor.
     - mappedBy: la relación es controlada desde la entidad Autor
     - cascade: permite guardar autores automáticamente al guardar el libro
     - fetch EAGER: carga los autores junto con el libro
    */
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    /*
     Colección de enums Idioma.
     - ElementCollection crea una tabla intermedia
     - EnumType.STRING guarda el nombre del enum (ESPANOL, INGLES, etc.)
     - fetch EAGER carga los idiomas junto con el libro
    */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Idioma> idiomas;

    /*
     Número total de descargas del libro.
     Se usa Double para facilitar cálculos estadísticos.
    */
    private Double numeroDeDescargas;

    // Constructor vacío obligatorio para JPA
    public Libro() {}

    /*
     Constructor que transforma un DTO (DatosLibro)
     en una entidad persistente (Libro).
    */
    public Libro(DatosLibro datosLibro) {
        // ID externo (API)
        this.id = datosLibro.id();

        // Título del libro
        this.titulo = datosLibro.titulo();

        /*
         Mapeo de autores:
         - Se convierte cada DatosAutor en Autor
         - Se establece la relación bidireccional con el libro
         - Se usa ArrayList para asegurar que la colección sea mutable
        */
        this.autores = datosLibro.autores().stream()
                .map(a -> {
                    Autor autor = new Autor(a);
                    autor.setLibro(this); // Relación inversa
                    return autor;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        /*
         Mapeo de idiomas:
         - Convierte Strings ("es", "en") al enum Idioma
         - Si el idioma no existe, se asigna DESCONOCIDO
         - Evita listas nulas
        */
        if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
            this.idiomas = datosLibro.idiomas().stream()
                    .map(i -> {
                        try {
                            return Idioma.fromString(i);
                        } catch (IllegalArgumentException e) {
                            return Idioma.DESCONOCIDO;
                        }
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            this.idiomas = new ArrayList<>();
        }

        // Descargas (valor por defecto 0.0 si viene nulo)
        this.numeroDeDescargas =
                datosLibro.numeroDeDescargas() != null ? datosLibro.numeroDeDescargas() : 0.0;
    }

    // ---------------- GETTERS Y SETTERS ----------------

    public Long getIdLibro() { return idLibro; }
    public void setIdLibro(Long idLibro) { this.idLibro = idLibro; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public List<Autor> getAutores() { return autores; }

    /*
     Al asignar autores manualmente,
     se asegura la relación bidireccional correcta.
    */
    public void setAutores(List<Autor> autores) {
        if (autores != null) {
            autores.forEach(a -> a.setLibro(this));
        }
        this.autores = autores;
    }

    public List<Idioma> getIdiomas() { return idiomas; }
    public void setIdiomas(List<Idioma> idiomas) { this.idiomas = idiomas; }

    public Double getNumeroDeDescargas() { return numeroDeDescargas; }
    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    /*
     Representación legible del libro.
     Se usa principalmente para mostrar resultados en consola.
    */
    @Override
    public String toString() {
        return String.format("""
                | --- LIBRO ENCONTRADO ---
                | Título: %s
                | Idiomas en BDD: %s
                | Descargas: %.1f
                | ---------------------------""",
                titulo, idiomas, numeroDeDescargas);
    }
}