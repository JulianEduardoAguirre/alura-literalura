package com.one.literalura.repository;

import com.one.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombreIgnoreCase(String nombre);
    List<Autor> findByNombreContainingIgnoreCase(String name);
    List<Autor> findTop1ByFechaDeNacimientoNotNullOrderByFechaDeNacimientoDesc();
}
