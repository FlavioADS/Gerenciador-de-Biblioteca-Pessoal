package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.model.Livro.StatusLeitura;
import com.biblioteca.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LivroServiceTest {

    @Autowired
    private LivroService livroService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void criarUsuario() {
        usuario = usuarioService.registrarUsuario("Teste", "teste-livro@teste.com", "senha123");
    }

    @Test
    void testSalvarLivro() {
        Livro livroSalvo = livroService.salvarLivro(new Livro("Dom Casmurro", "Machado de Assis", usuario));
        assertNotNull(livroSalvo.getId());
        assertEquals("Dom Casmurro", livroSalvo.getTitulo());
    }

    @Test
    void testBuscarPorId() {
        Livro livroSalvo = livroService.salvarLivro(new Livro("O Alquimista", "Paulo Coelho", usuario));
        assertNotNull(livroService.buscarPorId(livroSalvo.getId()));
    }

    @Test
    void testBuscarLivroInexistente() {
        assertNull(livroService.buscarPorId(9999L));
    }

    @Test
    void testListarLivros() {
        livroService.salvarLivro(new Livro("Livro 1", "Autor 1", usuario));
        livroService.salvarLivro(new Livro("Livro 2", "Autor 2", usuario));
        assertEquals(2, livroService.listarLivrosDoUsuario(usuario.getId()).size());
    }

    @Test
    void testListarSemLivros() {
        assertTrue(livroService.listarLivrosDoUsuario(usuario.getId()).isEmpty());
    }

    @Test
    void testAtualizarLivro() {
        Livro livroSalvo = livroService.salvarLivro(new Livro("Titulo Antigo", "Autor", usuario));
        livroSalvo.setTitulo("Titulo Novo");
        assertEquals("Titulo Novo", livroService.atualizarLivro(livroSalvo).getTitulo());
    }

    @Test
    void testAtualizarStatus() {
        Livro livroSalvo = livroService.salvarLivro(new Livro("Livro", "Autor", usuario));
        livroService.atualizarStatus(livroSalvo.getId(), StatusLeitura.LENDO);
        assertEquals(StatusLeitura.LENDO, livroService.buscarPorId(livroSalvo.getId()).getStatusLeitura());
    }

    @Test
    void testAtualizarStatusInexistente() {
        assertNull(livroService.atualizarStatus(9999L, StatusLeitura.CONCLUIDO));
    }

    @Test
    void testExcluirLivro() {
        Livro livroSalvo = livroService.salvarLivro(new Livro("Para Excluir", "Autor", usuario));
        livroService.excluirLivro(livroSalvo.getId());
        assertNull(livroService.buscarPorId(livroSalvo.getId()));
    }

    @Test
    void testEstatisticas() {
        Livro l1 = new Livro("L1", "A", usuario);
        l1.setStatusLeitura(StatusLeitura.LENDO);
        livroService.salvarLivro(l1);

        Livro l2 = new Livro("L2", "A", usuario);
        l2.setStatusLeitura(StatusLeitura.CONCLUIDO);
        livroService.salvarLivro(l2);

        Livro l3 = new Livro("L3", "A", usuario);
        l3.setStatusLeitura(StatusLeitura.CONCLUIDO);
        livroService.salvarLivro(l3);

        Livro l4 = new Livro("L4", "A", usuario);
        l4.setStatusLeitura(StatusLeitura.PAUSADO);
        livroService.salvarLivro(l4);

        Map<String, Long> stats = livroService.obterEstatisticas(usuario.getId());
        assertEquals(4L, stats.get("total"));
        assertEquals(1L, stats.get("lendo"));
        assertEquals(2L, stats.get("concluidos"));
        assertEquals(1L, stats.get("pausados"));
    }
}

