package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void criarUsuario() {
        usuario = usuarioService.registrarUsuario("Teste", "perfil@teste.com", "senha123");
    }

    @Test
    void testPerfilSemLogin() throws Exception {
        mockMvc.perform(get("/perfil"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testVerPerfil() throws Exception {
        mockMvc.perform(get("/perfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/visualizar"));
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testFormularioEditarPerfil() throws Exception {
        mockMvc.perform(get("/perfil/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/editar"));
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testAtualizarPerfil() throws Exception {
        mockMvc.perform(post("/perfil/editar").with(csrf())
                        .param("nome", "Novo")
                        .param("email", "perfil@teste.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil"));

        Usuario atualizado = usuarioService.buscarPorEmail("perfil@teste.com");
        org.junit.jupiter.api.Assertions.assertEquals("Novo", atualizado.getNome());
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testFormularioAlterarSenha() throws Exception {
        mockMvc.perform(get("/perfil/senha"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/senha"));
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testAlterarSenha() throws Exception {
        mockMvc.perform(post("/perfil/senha").with(csrf())
                        .param("senhaAtual", "senha123")
                        .param("novaSenha", "novaSenha123")
                        .param("confirmarSenha", "novaSenha123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil"));
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testAlterarSenhaDiferente() throws Exception {
        mockMvc.perform(post("/perfil/senha").with(csrf())
                        .param("senhaAtual", "senha123")
                        .param("novaSenha", "novaSenha")
                        .param("confirmarSenha", "outra"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/senha"))
                .andExpect(model().attribute("mensagemErro", "As senhas não conferem"));
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testAlterarSenhaAtualErrada() throws Exception {
        mockMvc.perform(post("/perfil/senha").with(csrf())
                        .param("senhaAtual", "errada")
                        .param("novaSenha", "novaSenha123")
                        .param("confirmarSenha", "novaSenha123"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/senha"))
                .andExpect(model().attribute("mensagemErro", "Senha atual incorreta"));
    }

    @Test
    @WithMockUser(username = "perfil@teste.com")
    void testAlterarSenhaCurta() throws Exception {
        mockMvc.perform(post("/perfil/senha").with(csrf())
                        .param("senhaAtual", "senha123")
                        .param("novaSenha", "abc")
                        .param("confirmarSenha", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/senha"))
                .andExpect(model().attribute("mensagemErro", "A nova senha deve ter no mínimo 6 caracteres"));
    }
}
