package com.one.literalura.repository;

import com.one.literalura.model.Autor;
import com.one.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Libro findByApiId(Long apiId);

//    List<Libro> findByIdiomasContaining(String idioma);   //Usado cuando buscaba TODOS los idiomas
    List<Libro> findByIdioma(String idioma);

    List<Libro> findTop1ByOrderByNumeroDeDescargasDesc();

    List<Libro> findTop5ByOrderByNumeroDeDescargasDesc();
}
