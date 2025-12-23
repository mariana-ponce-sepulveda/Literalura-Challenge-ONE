package com.aluracursos.literalura.repository;
// Paquete que contiene los repositorios JPA de la aplicación.
// Los repositorios permiten interactuar con la base de datos sin escribir SQL manual.

import com.aluracursos.literalura.model.Autor;
// Importa la entidad Autor, que representa la tabla autores en la base de datos.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// Importaciones de Spring Data JPA para repositorios y consultas personalizadas.

import java.util.List;
// Importación para trabajar con colecciones de resultados.

/*
  Repositorio de la entidad Autor.
  Extiende JpaRepository para heredar automáticamente:
  - Guardar
  - Buscar
  - Actualizar
  - Eliminar registros
 */
public interface AutorRepository extends JpaRepository<Autor, Long> {
    // <Autor, Long> indica:
    // Autor → entidad que se maneja
    // Long  → tipo de la clave primaria (ID)

    /*
      Consulta personalizada usando JPQL (no SQL nativo).

      Busca los autores que estaban vivos en un año específico.

      Condiciones:
      - El autor nació en un año menor o igual al año consultado
      - El autor falleció en un año mayor o igual al año consultado

      :anio es un parámetro dinámico recibido por el método.
     */
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio AND a.fechaDeDefuncion >= :anio")
    List<Autor> buscarAutoresVivosEnDeterminadoAnio(Integer anio);
    // Devuelve una lista de autores que cumplen la condición
}