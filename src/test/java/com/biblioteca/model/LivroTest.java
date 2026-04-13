package com.biblioteca.model;

import com.biblioteca.model.Livro.StatusLeitura;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class LivroTest {

    @Test
    void testCriarLivroVazio() {
        Livro livro = new Livro();
        assertNull(livro.getId());
        assertNull(livro.getTitulo());
        assertEquals(StatusLeitura.NAO_INICIADO, livro.getStatusLeitura());
        assertNotNull(livro.getCriadoEm());
    }

    @Test
    void testCriarLivroComDados() {
        Usuario usuario = new Usuario("João", "joao@email.com", "senha");
        Livro livro = new Livro("Dom Casmurro", "Machado de Assis", usuario);
        assertEquals("Dom Casmurro", livro.getTitulo());
        assertEquals("Machado de Assis", livro.getAutor());
        assertEquals(usuario, livro.getUsuario());
    }

    @Test
    void testSettersEGetters() {
        Livro livro = new Livro();
        Usuario usuario = new Usuario("Maria", "maria@email.com", "senha");

        livro.setId(1L);
        livro.setTitulo("O Alquimista");
        livro.setAutor("Paulo Coelho");
        livro.setIsbn("978-0061122415");
        livro.setEditora("HarperCollins");
        livro.setAnoPublicacao(1988);
        livro.setGenero("Ficção");
        livro.setDescricao("Uma história sobre sonhos");
        livro.setNumeroPaginas(208);
        livro.setStatusLeitura(StatusLeitura.LENDO);
        livro.setUrlCapa("http://capa.jpg");
        livro.setUsuario(usuario);

        assertEquals(1L, livro.getId());
        assertEquals("O Alquimista", livro.getTitulo());
        assertEquals("Paulo Coelho", livro.getAutor());
        assertEquals("978-0061122415", livro.getIsbn());
        assertEquals("HarperCollins", livro.getEditora());
        assertEquals(1988, livro.getAnoPublicacao());
        assertEquals("Ficção", livro.getGenero());
        assertEquals("Uma história sobre sonhos", livro.getDescricao());
        assertEquals(208, livro.getNumeroPaginas());
        assertEquals(StatusLeitura.LENDO, livro.getStatusLeitura());
        assertEquals("http://capa.jpg", livro.getUrlCapa());
        assertEquals(usuario, livro.getUsuario());
    }

    @ParameterizedTest
    @CsvSource({
            "NAO_INICIADO, Não Iniciado",
            "LENDO, Lendo",
            "CONCLUIDO, Concluído",
            "PAUSADO, Pausado"
    })
    void testNomeExibicaoDoStatus(String status, String nomeEsperado) {
        assertEquals(nomeEsperado, StatusLeitura.valueOf(status).getNomeExibicao());
    }

    @ParameterizedTest
    @EnumSource(StatusLeitura.class)
    void testTodosStatusTemNome(StatusLeitura status) {
        assertNotNull(status.getNomeExibicao());
        assertFalse(status.getNomeExibicao().isEmpty());
    }

    @Test
    void testQuantidadeDeStatus() {
        assertEquals(4, StatusLeitura.values().length);
    }
}
