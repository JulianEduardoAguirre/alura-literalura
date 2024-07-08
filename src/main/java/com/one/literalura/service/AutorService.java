package com.one.literalura.service;

import com.one.literalura.model.Autor;
import com.one.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<String> listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        return autores.stream()
                .sorted((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()))
                .map(Autor::toString)
                .collect(Collectors.toList());
    }
}
