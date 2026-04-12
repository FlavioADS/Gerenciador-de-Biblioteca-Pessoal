package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String verPerfil(@AuthenticationPrincipal UserDetails detalhesUsuario, Model modelo) {
        Usuario usuario = usuarioService.buscarPorEmail(detalhesUsuario.getUsername());
        modelo.addAttribute("usuario", usuario);
        return "perfil/visualizar";
    }

    @GetMapping("/editar")
    public String editarPerfilFormulario(@AuthenticationPrincipal UserDetails detalhesUsuario, Model modelo) {
        Usuario usuario = usuarioService.buscarPorEmail(detalhesUsuario.getUsername());
        modelo.addAttribute("usuario", usuario);
        return "perfil/editar";
    }

    @PostMapping("/editar")
    public String atualizarPerfil(@AuthenticationPrincipal UserDetails detalhesUsuario,
                                  @RequestParam String nome,
                                  @RequestParam String email,
                                  RedirectAttributes redirecionamento) {
        Usuario usuario = usuarioService.buscarPorEmail(detalhesUsuario.getUsername());
        usuarioService.atualizarUsuario(usuario.getId(), nome, email);
        redirecionamento.addFlashAttribute("mensagemSucesso", "Perfil atualizado com sucesso!");
        return "redirect:/perfil";
    }

    @GetMapping("/senha")
    public String alterarSenhaFormulario() {
        return "perfil/senha";
    }

    @PostMapping("/senha")
    public String alterarSenha(@AuthenticationPrincipal UserDetails detalhesUsuario,
                               @RequestParam String senhaAtual,
                               @RequestParam String novaSenha,
                               @RequestParam String confirmarSenha,
                               RedirectAttributes redirecionamento,
                               Model modelo) {

        if (!novaSenha.equals(confirmarSenha)) {
            modelo.addAttribute("mensagemErro", "As senhas não conferem");
            return "perfil/senha";
        }

        if (novaSenha.length() < 6) {
            modelo.addAttribute("mensagemErro", "A nova senha deve ter no mínimo 6 caracteres");
            return "perfil/senha";
        }

        Usuario usuario = usuarioService.buscarPorEmail(detalhesUsuario.getUsername());
        boolean sucesso = usuarioService.alterarSenha(usuario.getId(), senhaAtual, novaSenha);

        if (sucesso) {
            redirecionamento.addFlashAttribute("mensagemSucesso", "Senha alterada com sucesso!");
            return "redirect:/perfil";
        } else {
            modelo.addAttribute("mensagemErro", "Senha atual incorreta");
            return "perfil/senha";
        }
    }
}
