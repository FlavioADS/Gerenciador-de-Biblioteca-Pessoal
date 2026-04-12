package com.biblioteca.controller;

import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.LivroService;
import com.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@Controller
public class PainelController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LivroService livroService;

    @GetMapping("/painel")
    public String painel(@AuthenticationPrincipal UserDetails detalhesUsuario, Model modelo) {
        Usuario usuario = usuarioService.buscarPorEmail(detalhesUsuario.getUsername());
        Map<String, Long> estatisticas = livroService.obterEstatisticas(usuario.getId());
        List<Livro> livros = livroService.listarLivrosDoUsuario(usuario.getId());

        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("estatisticas", estatisticas);
        modelo.addAttribute("livrosRecentes", livros);

        return "painel";
    }
}
