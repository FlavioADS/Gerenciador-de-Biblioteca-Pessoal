package com.biblioteca.controller;

import com.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testPaginaInicial() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testPaginaLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testPaginaRegistro() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"));
    }

    @Test
    void testRegistroComSucesso() throws Exception {
        mockMvc.perform(post("/registro").with(csrf())
                        .param("nome", "João")
                        .param("email", "joao@teste.com")
                        .param("senha", "senha123")
                        .param("confirmarSenha", "senha123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testRegistroSenhasDiferentes() throws Exception {
        mockMvc.perform(post("/registro").with(csrf())
                        .param("nome", "Maria")
                        .param("email", "maria@teste.com")
                        .param("senha", "senha123")
                        .param("confirmarSenha", "outra"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attribute("mensagemErro", "As senhas não conferem"));
    }

    @Test
    void testRegistroEmailDuplicado() throws Exception {
        usuarioService.registrarUsuario("Existente", "existe@teste.com", "senha123");

        mockMvc.perform(post("/registro").with(csrf())
                        .param("nome", "Outro")
                        .param("email", "existe@teste.com")
                        .param("senha", "senha123")
                        .param("confirmarSenha", "senha123"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attribute("mensagemErro", "Este email já está cadastrado"));
    }
}
