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

import java.util.*;
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
        case "6":
            System.out.println(libroRepository.findTop1ByOrderByNumeroDeDescargasDesc());
            break;
        case "7":
            System.out.println(autorRepository.findTop1ByFechaDeNacimientoNotNullOrderByFechaDeNacimientoDesc());
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

//        List<Autor> toSort = new ArrayList<>(autores);
//        toSort.sort((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()));
//        List<String> autoresString = new ArrayList<>();
//        for (Autor autor : toSort) {
//            String string = autor.toString();
//            autoresString.add(string);
//        }

        List<String> autoresStr = autores.stream().
                sorted((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()))
                        .map(Autor::toString)
                                .collect(Collectors.toList());


        autoresStr.forEach(System.out::println);

    }

    private void mostrarListaDeLibros() {

        System.out.println("Lista de libros");
        List<Libro> librosGuardados = libroRepository.findAll();


//        System.out.println(librosGuardados);
        librosGuardados.forEach(System.out::println);
    }

    private void buscarLibroPorTitulo() {
    // Realiza la consulta, trae todos los libros con esa palabra en el título y retiene SOLAMENTE el primer resultado
        System.out.println("Inserte el título del libro a buscar");
        var tituloLibro = scanner.next();
        var json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, DatosTotal.class);

        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){

            //Aseguro que el libro no se encuentra salvado en la base de datos
            Optional<Libro> libroPorApiId = Optional.ofNullable(libroRepository.findByApiId(libroBuscado.get().apiId()));

            if(libroPorApiId.isPresent()){
                System.out.println("El libro ya se encuentra en la base de datos");
            } else {
                System.out.println("Libro encontrado ");

                DatosLibro datosLibro = libroBuscado.get();

                // Solo busco el primer autor (se supone que tiene, SIEMPRE, al menos uno)
                DatosAutor datosAutor = datosLibro.autores().get(0);

                // Busco el autor en la base de datos (solo puedo hacerlo por nombre, no posee Id propio (en la Gutendex) )
                Optional<Autor> autorEnBaseDeDatos = Optional.ofNullable(autorRepository.findByNombreIgnoreCase(datosAutor.nombre()));

                // Genero una nueva entidad Libro
                Libro libro = new Libro(datosLibro);

                // Adjudico el autor encontrado en la base de datos (en el caso de que hubiera existido en la misma, previamente)
                autorEnBaseDeDatos.ifPresent(libro::setAutor);

                libroRepository.save(libro);

                System.out.println("Libro guardado en la base de datos");
            }

        } else {
            System.out.println("No se encontró ningún libro con esa palabra en el título.");
        }
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
