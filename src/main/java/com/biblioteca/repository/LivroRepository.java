package com.biblioteca.repository;

import com.biblioteca.model.Livro;
import com.biblioteca.model.Livro.StatusLeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByUsuarioId(Long usuarioId);

    List<Livro> findByUsuarioIdAndStatusLeitura(Long usuarioId, StatusLeitura status);

    long countByUsuarioId(Long usuarioId);

    long countByUsuarioIdAndStatusLeitura(Long usuarioId, StatusLeitura status);

}