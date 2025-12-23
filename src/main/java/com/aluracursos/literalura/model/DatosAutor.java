package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Indica a Jackson que ignore cualquier campo del JSON
// que no esté declarado en este record
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(

        // Mapea la clave "name" del JSON de la API Gutendex
        // al atributo "nombre" del record
        @JsonAlias("name") String nombre,

        // Mapea la clave "birth_year" del JSON
        // al atributo "fechaDeNacimiento"
        @JsonAlias("birth_year") Integer fechaDeNacimiento,

        // Mapea la clave "death_year" del JSON
        // al atributo "fechaDeDefuncion"
        @JsonAlias("death_year") Integer fechaDeDefuncion

) {
}

/*
 EJEMPLO DE OBJETO "Person" EN EL JSON DE GUTENDEX:

 {
   "name": "Cervantes Saavedra, Miguel de",
   "birth_year": 1547,
   "death_year": 1616
 }

 ✔ Jackson usa @JsonAlias para traducir nombres en inglés a español
 ✔ Los valores pueden ser null (por eso usamos Integer y no int)
 ✔ Este record se usa como DTO (Data Transfer Object), no se guarda en BDD
*/