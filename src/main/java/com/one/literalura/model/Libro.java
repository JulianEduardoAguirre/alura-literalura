package com.one.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalInt;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "autor_id")
    private Autor autor;

//    @ElementCollection        //Usado para generar una tabla intermedia para un listado de idiomas
//    private List<String> idiomas;
    private String idioma;

    private Integer numeroDeDescargas;
    private Long apiId;

    public Libro () {
    }

    public Libro (DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.autor = new Autor(datosLibro.autores().get(0));
//        this.idiomas = datosLibro.idiomas();  // Anteriormente, se creaba una lista de los idiomas
        this.idioma = datosLibro.idiomas().get(0);
        this.numeroDeDescargas = OptionalInt.of(datosLibro.descargas()).orElse(0);
        this.apiId = datosLibro.apiId();
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
        autor.setLibro(this);
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {

        return String.format("_________________Libro__________________" +
                "\nTítulo   :  %s\nAutor    :  %s\nDescargas:  %s\n" +
                "_________________________________________\n", titulo, autor.getNombre(), numeroDeDescargas);
    }
}
