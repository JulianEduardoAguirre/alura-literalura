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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @ElementCollection
    private List<String> idiomas;
    private Integer numeroDeDescargas;
    private Long apiId;

    public Libro () {
    }

    public Libro (DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.autor = new Autor(datosLibro.autores().get(0));
        this.idiomas = datosLibro.idiomas();
        this.numeroDeDescargas = OptionalInt.of(datosLibro.descargas()).orElse(0);
        this.apiId = datosLibro.apiId();
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        apiId = apiId;
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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", numeroDeDescargas=" + numeroDeDescargas +
                ", apiId=" + apiId +
                '}';
    }
}
