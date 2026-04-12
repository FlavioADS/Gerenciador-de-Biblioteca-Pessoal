package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.model.Livro.StatusLeitura;
import com.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public Livro salvarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id).orElse(null);
    }

    public List<Livro> listarLivrosDoUsuario(Long usuarioId) {
        return livroRepository.findByUsuarioId(usuarioId);
    }

    public Livro atualizarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public Livro atualizarStatus(Long livroId, StatusLeitura novoStatus) {
        Livro livro = livroRepository.findById(livroId).orElse(null);
        if (livro != null) {
            livro.setStatusLeitura(novoStatus);
            return livroRepository.save(livro);
        }
        return null;
    }

    public void excluirLivro(Long id) {
        livroRepository.deleteById(id);
    }

    public Map<String, Long> obterEstatisticas(Long usuarioId) {
        Map<String, Long> estatisticas = new HashMap<>();

        estatisticas.put("total", livroRepository.countByUsuarioId(usuarioId));
        estatisticas.put("lendo", livroRepository.countByUsuarioIdAndStatusLeitura(usuarioId, StatusLeitura.LENDO));
        estatisticas.put("concluidos", livroRepository.countByUsuarioIdAndStatusLeitura(usuarioId, StatusLeitura.CONCLUIDO));
        estatisticas.put("naoIniciados", livroRepository.countByUsuarioIdAndStatusLeitura(usuarioId, StatusLeitura.NAO_INICIADO));
        estatisticas.put("pausados", livroRepository.countByUsuarioIdAndStatusLeitura(usuarioId, StatusLeitura.PAUSADO));

        return estatisticas;
    }
}
