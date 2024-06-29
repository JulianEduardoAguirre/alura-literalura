package com.one.literalura.principal;

import java.util.Scanner;

public class Menu {

    private Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    public void menuPrincipal() {
        Integer opcion = -1;
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
//            switchMenuPrincipal(opcion);
        }
    }

//    private void switchMenuPrincipal(Integer opcion) {
//    switch (opcion) {
//        case 1:
//            buscarLibroPorTitulo();
//            break;
//        case 2:
//            mostrarListaDeLibros();
//            break;
//        case 3:
//            mostrarListaDeAutores();
//            break;
//        case 4:
//            mostrarLibrosPorIdioma();
//            break;
//        case 5:
//            mostrarTop5Descargas();
//            break;
//        case 0:
//            System.out.println("Hasta luego.");
//            break;
//        default:
//            System.out.println("Opción inválida");
//    }
//
//    }
}
