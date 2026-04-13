package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.model.Livro.StatusLeitura;
import com.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final OpenLibraryClient openLibraryClient;

    public LivroService(LivroRepository livroRepository, OpenLibraryClient openLibraryClient) {
        this.livroRepository = livroRepository;
        this.openLibraryClient = openLibraryClient;
    }

    public Livro salvarLivro(Livro livro) {
        return persistirLivro(livro);
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id).orElse(null);
    }

    public List<Livro> listarLivrosDoUsuario(Long usuarioId) {
        return livroRepository.findByUsuarioId(usuarioId);
    }

    public Livro atualizarLivro(Livro livro) {
        return persistirLivro(livro);
    }

    private Livro persistirLivro(Livro livro) {
        normalizarCampos(livro);
        enriquecerCapaSeNecessario(livro);
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

    private void enriquecerCapaSeNecessario(Livro livro) {
        if (livro.getUrlCapa() != null && !livro.getUrlCapa().isBlank()) {
            return;
        }

        if (livro.getIsbn() == null || livro.getIsbn().isBlank()) {
            return;
        }

        openLibraryClient.buscarUrlCapaPorIsbn(livro.getIsbn())
                .ifPresent(livro::setUrlCapa);
    }

    private void normalizarCampos(Livro livro) {
        livro.setUrlCapa(normalizarTexto(livro.getUrlCapa()));
        livro.setIsbn(normalizarTexto(livro.getIsbn()));
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String normalizado = valor.trim();
        if (normalizado.length() >= 2) {
            boolean comAspasDuplas = normalizado.startsWith("\"") && normalizado.endsWith("\"");
            boolean comAspasSimples = normalizado.startsWith("'") && normalizado.endsWith("'");
            if (comAspasDuplas || comAspasSimples) {
                normalizado = normalizado.substring(1, normalizado.length() - 1).trim();
            }
        }

        return normalizado.isBlank() ? null : normalizado;
    }
}
