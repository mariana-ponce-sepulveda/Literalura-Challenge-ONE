package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/*
 Representa los datos crudos de un libro obtenidos desde la API Gutendex.
 Se utiliza un record para:
 ✔ garantizar inmutabilidad
 ✔ reducir código repetitivo (getters, constructor, etc.)
 ✔ facilitar el mapeo JSON → Java
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(

        // ID del libro en Project Gutenberg
        @JsonAlias("id") Integer id,

        // Título del libro
        @JsonAlias("title") String titulo,

        // Lista de autores del libro
        // Cada autor se mapea al record DatosAutor
        @JsonAlias("authors") List<DatosAutor> autores,

        // Lista de idiomas en formato ISO (ej: "en", "es", "fr")
        @JsonAlias("languages") List<String> idiomas,

        // Cantidad total de descargas del libro
        // Se usa Double para poder aplicar estadísticas (promedios, etc.)
        @JsonAlias("download_count") Double numeroDeDescargas

) {
}

/*
 EJEMPLO DE OBJETO "Book" EN EL JSON DE GUTENDEX:

 {
   "id": 1342,
   "title": "Pride and Prejudice",
   "authors": [
       {
           "name": "Austen, Jane",
           "birth_year": 1775,
           "death_year": 1817
       }
   ],
   "languages": ["en"],
   "download_count": 58943
 }

 ✔ Este record actúa como DTO (Data Transfer Object)
 ✔ No es una entidad JPA
 ✔ Se transforma luego en la entidad Libro para guardarse en la base de datos
*/
