package com.biblioteca.service;

import com.biblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testRegistrarUsuario() {
        Usuario usuario = usuarioService.registrarUsuario("Joao", "joao@teste.com", "senha123");
        assertNotNull(usuario.getId());
        assertEquals("Joao", usuario.getNome());
        assertNotEquals("senha123", usuario.getSenha()); // Senha deve estar criptografada
    }

    @Test
    void testRegistrarEmailDuplicado() {
        usuarioService.registrarUsuario("Joao", "mesmo@teste.com", "senha123");
        assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario("Maria", "mesmo@teste.com", "senha456");
        });
    }

    @Test
    void testBuscarPorEmail() {
        usuarioService.registrarUsuario("Maria", "maria@teste.com", "senha123");
        Usuario usuario = usuarioService.buscarPorEmail("maria@teste.com");
        assertNotNull(usuario);
        assertEquals("Maria", usuario.getNome());
    }

    @Test
    void testBuscarEmailInexistente() {
        assertNull(usuarioService.buscarPorEmail("naoexiste@teste.com"));
    }

    @Test
    void testBuscarPorId() {
        Usuario registrado = usuarioService.registrarUsuario("Carlos", "carlos@teste.com", "senha123");
        assertNotNull(usuarioService.buscarPorId(registrado.getId()));
    }

    @Test
    void testBuscarIdInexistente() {
        assertNull(usuarioService.buscarPorId(9999L));
    }

    @Test
    void testAtualizarUsuario() {
        Usuario registrado = usuarioService.registrarUsuario("Ana", "ana@teste.com", "senha123");
        Usuario atualizado = usuarioService.atualizarUsuario(registrado.getId(), "Ana Silva", "ana.silva@teste.com");
        assertEquals("Ana Silva", atualizado.getNome());
    }

    @Test
    void testAtualizarUsuarioInexistente() {
        assertNull(usuarioService.atualizarUsuario(9999L, "Nome", "email@teste.com"));
    }

    @Test
    void testAlterarSenha() {
        Usuario usuario = usuarioService.registrarUsuario("Pedro", "pedro@teste.com", "senhaAntiga");
        assertTrue(usuarioService.alterarSenha(usuario.getId(), "senhaAntiga", "senhaNova"));
    }

    @Test
    void testAlterarSenhaErrada() {
        Usuario usuario = usuarioService.registrarUsuario("Lucas", "lucas@teste.com", "senhaCorreta");
        assertFalse(usuarioService.alterarSenha(usuario.getId(), "senhaErrada", "senhaNova"));
    }

    @Test
    void testExisteEmail() {
        usuarioService.registrarUsuario("Teste", "existe@teste.com", "senha123");
        assertTrue(usuarioService.existeEmail("existe@teste.com"));
        assertFalse(usuarioService.existeEmail("naoexiste@teste.com"));
    }

    @ParameterizedTest
    @CsvSource({
            "Ana, ana@teste.com, senha123",
            "Bruno, bruno@teste.com, senha456",
            "Carla, carla@teste.com, senha789"
    })
    void testRegistrarVariosUsuarios(String nome, String email, String senha) {
        Usuario usuario = usuarioService.registrarUsuario(nome, email, senha);
        assertNotNull(usuario.getId());
        assertEquals(nome, usuario.getNome());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalido@teste.com", "naoexiste@mail.com", "fake@abc.com"})
    void testBuscarEmailsInexistentes(String email) {
        assertNull(usuarioService.buscarPorEmail(email));
    }
}

