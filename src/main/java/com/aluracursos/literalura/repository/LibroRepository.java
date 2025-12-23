package com.aluracursos.literalura.repository;
// Paquete de repositorios JPA.
// Contiene las interfaces que permiten acceder a la base de datos
// mediante Spring Data JPA.

import com.aluracursos.literalura.model.Idioma;
import com.aluracursos.literalura.model.Libro;
// Importa las entidades usadas por este repositorio.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// Importaciones necesarias para trabajar con JPA y consultas personalizadas.

import java.util.List;
import java.util.Optional;
// List se usa para múltiples resultados
// Optional se usa para manejar valores que pueden o no existir.

/*
  Repositorio JPA para la entidad Libro.
  Extiende JpaRepository para obtener automáticamente
  operaciones CRUD básicas.
 */
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // <Libro, Long> indica:
    // Libro → entidad administrada
    // Long  → tipo del ID (clave primaria)

    /*
      Consulta personalizada en JPQL.

      Busca libros por idioma.

      - Se hace un JOIN entre Libro y su colección de idiomas.
      - CAST(i AS string) se utiliza para comparar el enum Idioma
        con un String, ya que Postgres maneja el enum como texto.
      - :idioma es el valor recibido por parámetro.
     */
    @Query("SELECT l FROM Libro l JOIN l.idiomas i WHERE CAST(i AS string) = :idioma")
    List<Libro> encontrarLibrosPorIdioma(String idioma);
    // Devuelve una lista de libros escritos en el idioma solicitado.

    /*
      Consulta derivada de Spring Data JPA.

      Busca un libro cuyo título contenga el texto indicado,
      sin distinguir entre mayúsculas y minúsculas.

      - Si el libro existe, lo devuelve dentro de un Optional
      - Si no existe, devuelve Optional.empty()
     */
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
}