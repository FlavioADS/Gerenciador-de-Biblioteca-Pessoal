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
        modelo.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String nome,
                                   @RequestParam String email,
                                   @RequestParam String senha,
                                   @RequestParam String confirmarSenha,
                                   RedirectAttributes redirecionamento,
                                   Model modelo) {

        if (!senha.equals(confirmarSenha)) {
            modelo.addAttribute("mensagemErro", "As senhas não conferem");
            modelo.addAttribute("usuario", new Usuario());
            return "registro";
        }

        if (usuarioService.existeEmail(email)) {
            modelo.addAttribute("mensagemErro", "Este email já está cadastrado");
            modelo.addAttribute("usuario", new Usuario());
            return "registro";
        }

        try {
            usuarioService.registrarUsuario(nome, email, senha);
            redirecionamento.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (Exception e) {
            modelo.addAttribute("mensagemErro", "Erro ao cadastrar: " + e.getMessage());
            modelo.addAttribute("usuario", new Usuario());
            return "registro";
        }
    }
}