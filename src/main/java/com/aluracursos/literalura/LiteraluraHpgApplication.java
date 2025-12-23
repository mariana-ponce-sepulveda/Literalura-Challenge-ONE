package com.aluracursos.literalura;

import com.aluracursos.literalura.principal.App;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// ↑ Se usa cuando aún no se ha configurado una base de datos.
// Evita que Spring falle al iniciar si no existe un DataSource.

@SpringBootApplication
// Anotación principal de Spring Boot.
// Activa la auto-configuración, el escaneo de componentes y la configuración del contexto.
public class LiteraluraHpgApplication implements CommandLineRunner {
	// Implementar CommandLineRunner permite ejecutar código automáticamente
	// una vez que la aplicación Spring Boot ha iniciado.

	@Autowired
	// Inyección automática del repositorio de libros.
	// Permite acceder a operaciones CRUD sobre la entidad Libro.
	private LibroRepository repository;

	@Autowired
	// Inyección automática del repositorio de autores.
	// Permite acceder a operaciones CRUD sobre la entidad Autor.
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		// Punto de entrada de la aplicación.
		// Inicia el contexto de Spring Boot.
		SpringApplication.run(LiteraluraHpgApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Este método se ejecuta automáticamente después de iniciar Spring Boot.

		// Se crea una instancia de la clase App,
		// que contiene la lógica principal y el menú de la aplicación.
		App app = new App(repository, autorRepository);

		// Ejecuta el flujo principal de la aplicación en consola.
		app.aplicacionRun();

		// Iconos usados para mostrar resultados en consola.
		// 📘 Libro encontrado
		// 📙 Libro ya registrado
		// 📕 Error o resultado no encontrado
	}
}