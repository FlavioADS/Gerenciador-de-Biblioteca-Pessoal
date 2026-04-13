package com.biblioteca.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AutenticacaoServiceTest {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testCarregarUsuarioExistente() {
        usuarioService.registrarUsuario("Joao", "joao@teste.com", "senha123");
        UserDetails detalhes = autenticacaoService.loadUserByUsername("joao@teste.com");

        assertNotNull(detalhes);
        assertEquals("joao@teste.com", detalhes.getUsername());
    }

    @Test
    void testCarregarUsuarioInexistente() {
        assertThrows(UsernameNotFoundException.class, () -> {
            autenticacaoService.loadUserByUsername("naoexiste@teste.com");
        });
    }
}
