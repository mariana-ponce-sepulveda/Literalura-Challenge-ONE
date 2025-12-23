package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// Ignora cualquier propiedad del JSON que no esté definida en este record
// (por ejemplo: "count", "next", "previous")
@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(

        // Mapea la clave "results" del JSON de la API Gutendex
        // hacia esta lista de objetos DatosLibro
        @JsonAlias("results") List<DatosLibro> resultados

) {
}

/*
 EJEMPLO DE RESPUESTA JSON DE GUTENDEX:

 {
   "count": 77309,
   "next": "https://gutendex.com/books/?page=2",
   "previous": null,
   "results": [
       { ... datos del libro ... },
       { ... }
   ]
 }

 - Solo nos interesa "results", por eso:
   ✔ usamos @JsonAlias("results")
   ✔ ignoramos las demás claves con @JsonIgnoreProperties
*/