package com.biblioteca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testCriarUsuarioVazio() {
        Usuario usuario = new Usuario();
        assertNull(usuario.getId());
        assertNull(usuario.getNome());
        assertNull(usuario.getEmail());
        assertEquals("USER", usuario.getPapel());
        assertTrue(usuario.isAtivo());
        assertNotNull(usuario.getCriadoEm());
        assertNotNull(usuario.getLivros());
    }

    @Test
    void testCriarUsuarioComDados() {
        Usuario usuario = new Usuario("João", "joao@email.com", "senha123");
        assertEquals("João", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
    }

    @Test
    void testSettersEGetters() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setSenha("senha456");
        usuario.setPapel("ADMIN");
        usuario.setAtivo(false);

        assertEquals(1L, usuario.getId());
        assertEquals("Maria", usuario.getNome());
        assertEquals("maria@email.com", usuario.getEmail());
        assertEquals("senha456", usuario.getSenha());
        assertEquals("ADMIN", usuario.getPapel());
        assertFalse(usuario.isAtivo());
    }

    @Test
    void testListaDeLivros() {
        Usuario usuario = new Usuario();
        assertNotNull(usuario.getLivros());
        assertEquals(0, usuario.getLivros().size());
    }
}
