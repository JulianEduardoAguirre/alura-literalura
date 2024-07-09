package com.one.literalura;

import com.one.literalura.model.DatosLibro;
import com.one.literalura.model.DatosTotal;
import com.one.literalura.principal.Principal;
import com.one.literalura.repository.AutorRepository;
import com.one.literalura.repository.LibroRepository;
import com.one.literalura.service.ConsumoAPI;
import com.one.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private AutorRepository autorRepository;

	@Autowired
	private LibroRepository libroRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(libroRepository, autorRepository);
		principal.menuPrincipal();


	}
}
