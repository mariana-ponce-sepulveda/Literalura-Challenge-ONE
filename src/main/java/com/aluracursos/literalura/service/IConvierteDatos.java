package com.aluracursos.literalura.service;
// Paquete de servicios.
// Contiene interfaces y clases relacionadas con la lógica de negocio
// y transformación de datos.

 /*
   Interfaz que define el contrato para la conversión de datos.
   Su objetivo principal es desacoplar la lógica de conversión
   (por ejemplo, usando Jackson) del resto de la aplicación.
 */
public interface IConvierteDatos {

    /*
      Método genérico para convertir una cadena JSON en un objeto Java.

      @param <T>   Tipo genérico que permite reutilizar el método
                  para cualquier clase o record del proyecto
                  (por ejemplo: DatosLibro, DatosAutor, etc.).
      @param json  Cadena de texto en formato JSON obtenida desde la API.
      @param clase Clase destino a la cual se desea mapear el JSON
                  (por ejemplo: DatosLibro.class).
      @return      Un objeto del tipo T con los datos del JSON convertidos.
     */
    <T> T obtenerDatos(String json, Class<T> clase);
}