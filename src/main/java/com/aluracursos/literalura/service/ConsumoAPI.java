package com.aluracursos.literalura.service;
// Paquete que agrupa las clases de servicio.
// Aquí se suele ubicar la lógica relacionada con operaciones externas,
// como el consumo de APIs.

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
// Clases de Java estándar usadas para realizar solicitudes HTTP.

public class ConsumoAPI {
    // Clase encargada de consumir una API externa
    // y devolver la respuesta en formato String (JSON).

    public String obtenerDatos(String url) {
        // Método que recibe una URL como String
        // y retorna la respuesta de la API en formato JSON.

        HttpClient client = HttpClient.newHttpClient();
        // Crea un cliente HTTP que se encargará de enviar la solicitud.

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                // Convierte la URL String en un objeto URI.
                .build();
                // Construye la solicitud HTTP.

        HttpResponse<String> response = null;
        // Variable que almacenará la respuesta de la API.

        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            // Envía la solicitud HTTP de forma síncrona
            // y obtiene el cuerpo de la respuesta como String.
        } catch (IOException e) {
            // Se lanza cuando ocurre un error de entrada/salida
            // (problemas de red, URL inválida, etc.).
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // Se lanza si el proceso es interrumpido mientras espera la respuesta.
            throw new RuntimeException(e);
        }

        String json = response.body();
        // Extrae el cuerpo de la respuesta HTTP (contenido JSON).

        return json;
        // Retorna el JSON obtenido desde la API.
    }
}