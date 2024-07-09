package com.one.literalura.principal;

import com.one.literalura.model.*;
import com.one.literalura.repository.AutorRepository;
import com.one.literalura.repository.LibroRepository;
import com.one.literalura.service.ConsumoAPI;
import com.one.literalura.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
                    
                    1)  Buscar libro por título
                    2)  Listar libros registrados
                    3)  Listar autores registrados
                    4)  Listar autores vivos en un determinado año
                    5)  Mostrar número de libros por idioma
                    6)  Listar libros por idioma
                    7)  Mostrar libro más descargado
                    8)  Mostrar autor más joven
                    9)  Mostrar top 5 libros descargados
                    10) Mostrar estadísticas
                    11) Buscar autor por nombre
                    
                    0) Salir
                    """);

            opcion = scanner.next();
            switchMenuPrincipal(opcion);
        }
    }



    private void switchMenuPrincipal(String opcion) {
        switch (opcion) {
            case "1" -> buscarLibroPorTitulo();
            case "2" -> mostrarListaDeLibros();
            case "3" -> mostrarListaDeAutores();
            case "4" -> mostrarAutorVivoEnFecha();
            case "5" -> menuIdiomas();
            case "6" -> mostrarLibrosPorIdioma();
            case "7" -> mostrarLibroMasDescargado();
            case "8" -> mostrarAutorMasJoven();
            case "9" -> mostrarTop10Descargas();
            case "10" -> mostrarEstadisticas();
            case "11" -> buscarAutorPorNombre();
            case "0" -> System.out.println("Finalizando el programa.");
            default -> System.out.println("Opción inválida");
        }

    }

    private void buscarAutorPorNombre() {
        var nombreAutor = "";
        do{
            System.out.println("Escriba el nombre del autor que desea buscar en la base de datos: ");
            nombreAutor = scanner.next();
            if (Objects.equals(nombreAutor, "")) {
                System.out.println("Debe ingresar un nombre\n\n");
            }
        } while (Objects.equals(nombreAutor, ""));

        List<Autor> autoresEncontrados = autorRepository.findByNombreContainingIgnoreCase(nombreAutor);
        int cantidad = autoresEncontrados.size();

        if (cantidad == 0) {
            System.out.println("No se encontraron autores con un nombre parecido al solicitado");
        } else {
            System.out.println("Se encontraron los siguientes autores:\n");
            autoresEncontrados.forEach(System.out::println);
        }
    }

    private void mostrarTop10Descargas() {
        List<Libro> topDescargados = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();
        topDescargados.forEach(System.out::println);
    }

    private void mostrarAutorMasJoven() {
        System.out.println(autorRepository.findTop1ByFechaDeNacimientoNotNullOrderByFechaDeNacimientoDesc());
    }

    private void mostrarLibroMasDescargado() {
        System.out.println(libroRepository.findTop1ByOrderByNumeroDeDescargasDesc());
    }

    public void menuIdiomas() {
        var opcionIdioma = "";
        while (!opcionIdioma.equals("0")) {
            System.out.println("""
                    ***********************************************
                    Ingrese el número correspondiente al idioma deseado
                    
                    1) Español
                    2) Inglés
                    3) Francés
                    4) Alemán
                    
                    0) Volver
                    """);

            opcionIdioma = scanner.next();
            switchIdiomas(opcionIdioma);
        }
    }

    public void switchIdiomas(String opcionIdioma) {
        switch (opcionIdioma) {
            case "1" -> mostrarCantidadDeLibrosPorIdioma("es", "español");
            case "2" -> mostrarCantidadDeLibrosPorIdioma("en", "inglés");
            case "3" -> mostrarCantidadDeLibrosPorIdioma("fr", "francés");
            case "4" -> mostrarCantidadDeLibrosPorIdioma("de", "alemán");
            case "0" -> System.out.println("..............");
            default -> System.out.println("Opción inválida");
        }
    }

    private void mostrarCantidadDeLibrosPorIdioma(String abreviacion, String idioma) {
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(abreviacion);
        int cantidad = librosPorIdioma.size();

        if (librosPorIdioma.isEmpty()) {
            System.out.println("Sin existencias de libros en " + idioma);
        } else {
            System.out.println((cantidad == 1? "Se encontró " + cantidad + " libro" : "Se encontraron " + cantidad + " libros") + " en " + idioma);
        }

    }
    private void mostrarAutorVivoEnFecha() {
        double anioActual = (double) LocalDate.now().getYear();
        Double anio = -1.0;
        do {
            anio = pedirAnio();
            if (anio <= 0 || anio > anioActual) {
                System.out.println("Periodo válido: 0 - " + (int)anioActual);
            }

        } while (anio <= 0 || anio > anioActual);

        Double finalAnio = anio;

        List<Autor> autoresVivos =  autorRepository.findAll().stream()
                .filter(autor -> autor.getFechaDeNacimiento() != null && autor.getFechaDeNacimiento() <= finalAnio)
                .filter(autor -> autor.getFechaDeFallecimiento() == null || autor.getFechaDeFallecimiento() >= finalAnio)
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()){
            System.out.println("No habían autores vivos ese año");
        } else {
            System.out.println("Se encontraron los siguientes autores vivos en el año " + finalAnio.intValue());
            autoresVivos.forEach(System.out::println);
        }
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("Mostrando libros por idiomas");
        var idiomaBuscado = scanner.next();

//        List<Libro> librosPorIdioma = libroRepository.findByIdiomasContaining(idiomaBuscado);
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idiomaBuscado);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros en ese idioma ");
        } else {
            librosPorIdioma.forEach(System.out::println);
        }

    }

    private void mostrarListaDeAutores() {

        System.out.println("Mostrando lista de autores");
        List<Autor> autores = autorRepository.findAll();

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


    public void mostrarEstadisticas() {

        //Trabajando con estadísticas
        DoubleSummaryStatistics estadisticas = libroRepository.findAll().stream()
                .mapToDouble(Libro::getNumeroDeDescargas)
                .filter(d -> d > 0)
                .summaryStatistics();

        System.out.println("Mayor número de descargas: " + estadisticas.getMax());
        System.out.println("Menor número de descargas: " + estadisticas.getMin());
        System.out.println("Promedio de descargas: " + estadisticas.getAverage());
    }
}
