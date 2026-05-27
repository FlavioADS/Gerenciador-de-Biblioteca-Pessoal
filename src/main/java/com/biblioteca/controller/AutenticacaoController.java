package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AutenticacaoController {

    private static final String USUARIO = "usuario";
    private static final String REGISTRO = "registro";
    private static final String MENSAGEMERRO = "mensagemErro";

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String paginaRegistro(Model modelo) {
        modelo.addAttribute(USUARIO, new Usuario());
        return REGISTRO;
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String nome,
                                   @RequestParam String email,
                                   @RequestParam String senha,
                                   @RequestParam String confirmarSenha,
                                   RedirectAttributes redirecionamento,
                                   Model modelo) {

        if (!senha.equals(confirmarSenha)) {
            modelo.addAttribute(MENSAGEMERRO, "As senhas não conferem");
            modelo.addAttribute(USUARIO, new Usuario());
            return REGISTRO;
        }

        if (usuarioService.existeEmail(email)) {
            modelo.addAttribute(MENSAGEMERRO, "Este email já está cadastrado");
            modelo.addAttribute(USUARIO, new Usuario());
            return REGISTRO;
        }

        try {
            usuarioService.registrarUsuario(nome, email, senha);
            redirecionamento.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (Exception e) {
            modelo.addAttribute(MENSAGEMERRO, "Erro ao cadastrar: " + e.getMessage());
            modelo.addAttribute(USUARIO, new Usuario());
            return REGISTRO;
        }
    }
}