package com.aluracursos.literalura.service;
// Paquete de servicios.
// Contiene la lógica encargada de transformar y procesar datos externos.

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// Clases de la librería Jackson utilizadas para convertir JSON en objetos Java.

/*
  Clase responsable de la conversión de datos JSON a objetos Java.
  Utiliza la biblioteca Jackson Databind para realizar el mapeo
  entre el texto JSON y las clases o records del proyecto.
 */
public class ConvierteDatos implements IConvierteDatos {

    // ObjectMapper es el componente principal de Jackson.
    // Se define como atributo para reutilizar la instancia
    // y evitar crear objetos innecesarios en cada conversión.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
      Implementación del método genérico definido en la interfaz IConvierteDatos.

      @param json   Texto JSON recibido desde la API.
      @param clase Clase o record al cual se desea mapear el JSON.
      @return       Un objeto del tipo T con los datos ya cargados.
      @throws RuntimeException Si ocurre un error durante la conversión del JSON.
     */
    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            // readValue analiza el JSON y, usando reflexión,
            // crea una instancia de la clase indicada
            // asignando automáticamente los valores correspondientes.
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            // Captura errores relacionados con el formato del JSON
            // o con problemas al mapear los datos a la clase destino.
            // Se relanza como RuntimeException para simplificar el manejo de errores.
            throw new RuntimeException(
                    "Error al convertir el JSON a la clase: " + clase.getSimpleName(), e
            );
        }
    }
}