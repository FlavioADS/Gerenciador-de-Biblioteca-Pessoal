package com.biblioteca.service;

import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(String nome, String email, String senha) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Já existe um usuário com este email");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPapel("USER");
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario atualizarUsuario(Long id, String nome, String email) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNome(nome);
            usuario.setEmail(email);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public boolean alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null) return false;

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            return false;
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        return true;
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}