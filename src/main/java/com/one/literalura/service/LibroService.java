package com.one.literalura.service;

import com.one.literalura.model.DatosLibro;
import com.one.literalura.model.DatosTotal;
import com.one.literalura.model.Libro;
import com.one.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private ConvierteDatos conversor;

    public void buscarLibroPorTitulo(String titulo, String json) {
        try{
            DatosTotal datosTotal = conversor.obtenerDatos(json, DatosTotal.class);

            Optional<DatosLibro> libroBuscado = datosTotal.resultados().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(titulo.toUpperCase()))
                    .findFirst();

            if (libroBuscado.isPresent()) {
                DatosLibro datosLibro = libroBuscado.get();

                Libro libro = new Libro(datosLibro);

                libroRepository.save(libro);

                System.out.println("Libro guardado en la base de datos");

            } else {
                System.out.println("Libro no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error");
            System.out.println(e.getMessage());
        }
    }


}
