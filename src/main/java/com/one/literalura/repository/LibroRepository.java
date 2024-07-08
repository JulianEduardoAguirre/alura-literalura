package com.one.literalura.repository;

import com.one.literalura.model.Autor;
import com.one.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Libro findByApiId(Long apiId);

}
