package com.one.literalura;

import com.one.literalura.model.DatosLibro;
import com.one.literalura.model.DatosTotal;
import com.one.literalura.service.ConsumoAPI;
import com.one.literalura.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var conversor = new ConvierteDatos();
//		var json = consumoAPI.obtenerDatos("http://gutendex.com/books?search=don+quijote");
		var json = consumoAPI.obtenerDatos("http://gutendex.com/books/");
//		System.out.println(json);

		var datos = conversor.obtenerDatos(json, DatosTotal.class);
//		System.out.println(datos.getClass());
		ArrayList<DatosLibro> listaTotal = new ArrayList<>(datos.resultados());
		System.out.println(listaTotal.get(5));
	}
}
