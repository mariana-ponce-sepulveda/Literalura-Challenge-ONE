package com.aluracursos.literalura.model;

import jakarta.persistence.*;

// Indica que esta clase es una entidad JPA (se mapeará a una tabla en la BD)
@Entity
// Nombre de la tabla en la base de datos
@Table(name = "autores")
public class Autor {

    // Clave primaria de la tabla
    @Id
    // El ID se genera automáticamente (auto incremental)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA necesita un ID propio para la tabla de autores

    // Nombre completo del autor
    private String nombre;

    // Año de nacimiento del autor
    private Integer fechaDeNacimiento;

    // Año de defunción del autor (puede ser null si sigue vivo)
    private Integer fechaDeDefuncion;

    // Muchos autores pueden estar asociados a un mismo libro
    @ManyToOne
    // Define la columna de la clave foránea que apunta a la tabla libros
    @JoinColumn(name = "libro_id_libro") // Fuerza a JPA a usar exactamente esta columna
    private Libro libro;

    // Constructor vacío obligatorio para JPA
    public Autor() {}

    // Constructor que recibe un DTO (DatosAutor) y mapea sus valores
    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeDefuncion = datosAutor.fechaDeDefuncion();
    }

    // Getter del ID
    public Long getId() {
        return id;
    }

    // Setter del ID
    public void setId(Long id) {
        this.id = id;
    }

    // Getter del nombre
    public String getNombre() {
        return nombre;
    }

    // Setter del nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter del año de nacimiento
    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    // Setter del año de nacimiento
    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    // Getter del año de defunción
    public Integer getFechaDeDefuncion() {
        return fechaDeDefuncion;
    }

    // Setter del año de defunción
    public void setFechaDeDefuncion(Integer fechaDeDefuncion) {
        this.fechaDeDefuncion = fechaDeDefuncion;
    }

    // Getter del libro asociado
    public Libro getLibro() {
        return libro;
    }

    // Setter del libro asociado
    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    // Representación en texto del autor (usada al imprimir en consola)
    @Override
    public String toString() {
        return String.format("""
                         ---------- AUTOR ----------
                        | - Apellido, Nombre: %s
                        | - Nacimiento: %s
                        | - Fallecimiento: %s
                        | ---------------------------""",
                        nombre,
                        // Si no hay fecha de nacimiento, se muestra N/A
                        (fechaDeNacimiento != null ? fechaDeNacimiento : "N/A"),
                        // Si no hay fecha de defunción, se asume que está vivo o es desconocido
                        (fechaDeDefuncion != null ? fechaDeDefuncion : "Vivo/Desconocido"));
    }
}