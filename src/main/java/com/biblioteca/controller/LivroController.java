package com.biblioteca.controller;

import com.biblioteca.model.Livro;
import com.biblioteca.model.Livro.StatusLeitura;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.LivroService;
import com.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarLivros(@AuthenticationPrincipal UserDetails detalhesUsuario, Model modelo) {
        Usuario usuario = usuarioService.buscarPorEmail(detalhesUsuario.getUsername());
        List<Livro> livros = livroService.listarLivrosDoUsuario(usuario.getId());
        modelo.addAttribute("livros", livros);
        modelo.addAttribute("statusLeituras", StatusLeitura.values());
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novoLivroFormulario(Model modelo) {
        modelo.addAttribute("livro", new Livro());
        modelo.addAttribute("statusLeituras", StatusLeitura.values());
        return "livros/formulario";
    }

    @PostMapping
    public String salvarLivro(@AuthenticationPrincipal UserDetails detalhesUsuario,
                              @ModelAttribute Livro livro,
                              RedirectAttributes redirecionamento) {

        Usuario usuario = usuarioService.buscarPorEmail(detalhesUsuario.getUsername());
        livro.setUsuario(usuario);
        livroService.salvarLivro(livro);
        redirecionamento.addFlashAttribute("mensagemSucesso", "Livro cadastrado com sucesso!");
        return "redirect:/livros";
    }

    @GetMapping("/{id}")
    public String verLivro(@PathVariable Long id, Model modelo) {
        Livro livro = livroService.buscarPorId(id);
        modelo.addAttribute("livro", livro);
        return "livros/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String editarLivroFormulario(@PathVariable Long id, Model modelo) {
        Livro livro = livroService.buscarPorId(id);
        modelo.addAttribute("livro", livro);
        modelo.addAttribute("statusLeituras", StatusLeitura.values());
        return "livros/formulario";
    }

    @PostMapping("/{id}")
    public String atualizarLivro(@PathVariable Long id,
                                 @ModelAttribute Livro livro,
                                 RedirectAttributes redirecionamento) {

        Livro livroExistente = livroService.buscarPorId(id);
        livro.setId(id);
        livro.setUsuario(livroExistente.getUsuario());
        livroService.atualizarLivro(livro);
        redirecionamento.addFlashAttribute("mensagemSucesso", "Livro atualizado com sucesso!");
        return "redirect:/livros";
    }

    @PostMapping("/{id}/excluir")
    public String excluirLivro(@PathVariable Long id, RedirectAttributes redirecionamento) {
        livroService.excluirLivro(id);
        redirecionamento.addFlashAttribute("mensagemSucesso", "Livro excluído com sucesso!");
        return "redirect:/livros";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                  @RequestParam StatusLeitura statusLeitura,
                                  RedirectAttributes redirecionamento) {
        livroService.atualizarStatus(id, statusLeitura);
        redirecionamento.addFlashAttribute("mensagemSucesso", "Status atualizado com sucesso!");
        return "redirect:/livros" + id;
    }
}