package com.aluracursos.literalura.model;

/*
 Enum que representa los idiomas soportados por la aplicación.
 Se usa para:
 ✔ traducir los códigos ISO de la API Gutendex (es, en, fr, etc.)
 ✔ mostrar los idiomas de forma amigable al usuario
 ✔ evitar Strings sueltos y errores tipográficos
*/
public enum Idioma {

    // Idiomas disponibles con:
    // (código usado por Gutendex, nombre legible en español)
    ESPANOL("es", "Español"),
    INGLES("en", "Inglés"),
    FRANCES("fr", "Francés"),
    PORTUGUES("pt", "Portugués"),
    ALEMAN("de", "Alemán"),
    ITALIANO("it", "Italiano"),
    CHINO("zh", "Chino"),
    JAPONES("ja", "Japonés"),
    RUSO("ru", "Ruso"),
    LATIN("la", "Latín"),
    HOLANDES("nl", "Holandés"),
    DESCONOCIDO("unknown", "Desconocido");

    // Código de idioma que viene desde la API Gutendex (ej: "es", "en")
    private String lenguajeGutendex;

    // Nombre del idioma en español para mostrar al usuario
    private String lenguajeEspanol;

    /*
     Constructor del enum.
     Se ejecuta automáticamente una vez por cada constante declarada arriba.
     Permite asociar datos extra a cada idioma.
    */
    Idioma(String lenguajeGutendex, String lenguajeEspanol) {
        this.lenguajeGutendex = lenguajeGutendex;
        this.lenguajeEspanol = lenguajeEspanol;
    }

    /*
     Convierte un String ingresado por el usuario o recibido desde la API
     (por ejemplo "es" o "en") al enum correspondiente.
     Si no existe coincidencia, lanza una excepción.
    */
    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            // Compara el código del enum con el texto recibido
            if (idioma.lenguajeGutendex.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        // Se lanza excepción si el código no corresponde a ningún idioma válido
        throw new IllegalArgumentException("Ningún idioma encontrado para: " + text);
    }

    /*
     Devuelve el nombre del idioma en español.
     Se usa para mostrar información clara en el menú y resultados.
    */
    public String getLenguajeEspanol() {
        return lenguajeEspanol;
    }
}