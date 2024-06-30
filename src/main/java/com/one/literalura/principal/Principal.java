package com.one.literalura.principal;

import com.one.literalura.model.DatosLibro;
import com.one.literalura.model.DatosTotal;
import com.one.literalura.service.ConsumoAPI;
import com.one.literalura.service.ConvierteDatos;

import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();

    private final String BASE_URL = "http://gutendex.com/books/";
    public void menuPrincipal() {
        var opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    **** Bienvenido a LiterAlura ****
                    Ingrese el número correspondiente a su consulta
                    
                    1) Buscar libro por título
                    2) Listar libros registrados
                    3) Listar autores registrados
                    4) Listar autores vivos en un determinado año
                    5) Listar libros por idioma
                    
                    0) Salir
                    """);

            opcion = scanner.nextInt();
            switchMenuPrincipal(opcion);
        }
    }



    private void switchMenuPrincipal(Integer opcion) {
    switch (opcion) {
        case 1:
            buscarLibroPorTitulo();
            break;
        case 2:
            mostrarListaDeLibros();
            break;
        case 3:
            mostrarListaDeAutores();
            break;
        case 4:
            mostrarAutorVivoEnFecha();
            break;
        case 5:
            mostrarLibrosPorIdioma();
            break;
        case 0:
            System.out.println("Finalizando el programa.");
            break;
        default:
            System.out.println("Opción inválida");
    }

    }

    private void mostrarAutorVivoEnFecha() {
        System.out.println("El autor X estaba vivo en...");
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("Mostrando libros por idiomas");
    }

    private void mostrarListaDeAutores() {
        System.out.println("Mostrando lista de autores");
    }

    private void mostrarListaDeLibros() {
        System.out.println("Lista de libros");
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Inserte el título del libro a buscar");
        var tituloLibro = scanner.nextLine();
        var json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, DatosTotal.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println("Libro encontrado ");
            System.out.println(libroBuscado.get());
        } else {
            System.out.println("No se encontró el libro");
        }
    }

//    public void muestraElMenu() {
//        var json = consumoAPI.obtenerDatos(BASE_URL);
////        System.out.println(json);
//        var datosTodos = conversor.obtenerDatos(json, DatosTotal.class);
//
//        //Búsqueda de libros por nombre
//        System.out.println("Inserte el título del libro a buscar");
//        var tituloLibro = scanner.nextLine();
//        json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + tituloLibro.replace(" ", "+"));
//        var datosBusqueda = conversor.obtenerDatos(json, DatosTotal.class);
//        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
//                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
//                .findFirst();
//        if(libroBuscado.isPresent()){
//            System.out.println("Libro encontrado ");
//            System.out.println(libroBuscado.get());
//        } else {
//            System.out.println("No se encontró el libro");
//        }
//
//        //Trabajando con estadísticas
//        IntSummaryStatistics est = datosTodos.resultados().stream()
//                .filter(d -> d.descargas() > 0)
//                .collect(Collectors.summarizingInt(DatosLibro::descargas));
//        System.out.println("Cantidad media de descargas: " + est.getAverage());
//        System.out.println("Cantidad máxima de descargas: " + est.getMax());
//    }
}
