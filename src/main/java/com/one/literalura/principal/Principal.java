package com.one.literalura.principal;

import com.one.literalura.model.*;
import com.one.literalura.repository.AutorRepository;
import com.one.literalura.repository.LibroRepository;
import com.one.literalura.service.AutorService;
import com.one.literalura.service.ConsumoAPI;
import com.one.literalura.service.ConvierteDatos;
import com.one.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;


    private final String BASE_URL = "http://gutendex.com/books/";

    public Principal() {
    }

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menuPrincipal() {
        var opcion = "";
        while (!opcion.equals("0")) {
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

            opcion = scanner.next();
            switchMenuPrincipal(opcion);
        }
    }



    private void switchMenuPrincipal(String opcion) {
    switch (opcion) {
        case "1":
            buscarLibroPorTitulo();
            break;
        case "2":
            mostrarListaDeLibros();
            break;
        case "3":
            mostrarListaDeAutores();
            break;
        case "4":
            mostrarAutorVivoEnFecha();
            break;
        case "5":
            mostrarLibrosPorIdioma();
            break;
        case "0":
            System.out.println("Finalizando el programa.");
            break;
        default:
            System.out.println("Opción inválida");
    }

    }

    private void mostrarAutorVivoEnFecha() {

        var anio = pedirAnio();

        List<Autor> autoresVivos =  autorRepository.findAll().stream()
                .filter(autor -> autor.getFechaDeNacimiento() != null && autor.getFechaDeNacimiento() <= anio)
                .filter(autor -> autor.getFechaDeFallecimiento() == null || autor.getFechaDeFallecimiento() >= anio)
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()){
            System.out.println("No habían autores vivos ese año");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("Mostrando libros por idiomas");
        var idiomaBuscado = scanner.next();

        List<Libro> librosPorIdioma = libroRepository.findByIdiomasContaining(idiomaBuscado);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros en ese idioma ");
        } else {
            librosPorIdioma.forEach(System.out::println);
        }

    }

    private void mostrarListaDeAutores() {

        System.out.println("Mostrando lista de autores");
        List<Autor> autores = autorRepository.findAll();

        List<String> autoresString = autores.stream()
                .sorted((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()))
                .map(Autor::toString)
                .collect(Collectors.toList());

//        System.out.println(autoresString);

        autoresString.forEach(System.out::println);

    }

    private void mostrarListaDeLibros() {

        System.out.println("Lista de libros");
        List<Libro> librosGuardados = libroRepository.findAll();


//        System.out.println(librosGuardados);
        librosGuardados.forEach(System.out::println);
    }

    //De momento, está trayendo todos los resultados, los cuales pueden ser mayor que 1
    //Modificar para indicar que se encontraron "X" resultados (cuando count != 0) o "No se encontró el libro" (count == 0)
    private void buscarLibroPorTitulo() {
        System.out.println("Inserte el título del libro a buscar");
        var tituloLibro = scanner.next();
        var json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + tituloLibro.replace(" ", "+"));
//        libroService.buscarLibroPorTitulo(tituloLibro, json);
        var datosBusqueda = conversor.obtenerDatos(json, DatosTotal.class);
//        System.out.println(datosBusqueda);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){

            Optional<Libro> libroPorApiId = Optional.ofNullable(libroRepository.findByApiId(libroBuscado.get().apiId()));
            System.out.println("ALGO");
            if(libroPorApiId.isPresent()){
                System.out.println("El libro ya se encuentra en la base de datos");
            } else {
                System.out.println("Libro encontrado ");
//            System.out.println(libroBuscado.get());
                DatosLibro datosLibro = libroBuscado.get();

                DatosAutor datosAutor = datosLibro.autores().get(0);
                Optional<Autor> autorEnBaseDeDatos = Optional.ofNullable(autorRepository.findByNombreIgnoreCase(datosAutor.nombre()));

                Libro libro = new Libro(datosLibro);
                autorEnBaseDeDatos.ifPresent(libro::setAutor);


                libroRepository.save(libro);

                System.out.println("Libro guardado en la base de datos");
            }




        } else {
            System.out.println("No se encontró el libro");
        }
    }

    private void buscarLibroPorTituloGeneral() {
        System.out.println("Inserte el título del libro a buscar");
        String tituloLibro = scanner.next();
//        String json = consumoAPI.obtenerDatos(BASE_URL + );
    }

    private boolean IsInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public Double pedirAnio() {
        String anioString;
        do {
            System.out.println("Ingrese el año: ");
            anioString = scanner.next();
        } while (!IsInteger(anioString));
        return Double.parseDouble(anioString);
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
