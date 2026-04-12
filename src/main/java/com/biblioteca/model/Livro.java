package com.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    private String isbn;

    private String editora;

    private Integer anoPublicacao;

    private String genero;

    @Column(length = 2000)
    private String descricao;

    private Integer numeroPaginas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLeitura statusLeitura = StatusLeitura.NAO_INICIADO;

    private String urlCapa;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    // Construtor Padrão (Vazio)
    public Livro() {
    }

    // Construtor com Parâmetros
    public Livro(String titulo, String autor, Usuario usuario) {
        this.titulo = titulo;
        this.autor = autor;
        this.usuario = usuario;
    }

    // --- GETTERS AND SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public StatusLeitura getStatusLeitura() {
        return statusLeitura;
    }

    public void setStatusLeitura(StatusLeitura statusLeitura) {
        this.statusLeitura = statusLeitura;
    }

    public String getUrlCapa() {
        return urlCapa;
    }

    public void setUrlCapa(String urlCapa) {
        this.urlCapa = urlCapa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public enum StatusLeitura {
        NAO_INICIADO("Não Iniciado"),
        LENDO("Lendo"),
        CONCLUIDO("Concluído"),
        PAUSADO("Pausado");

        private final String nomeExibicao;

        StatusLeitura(String nomeExibicao) {
            this.nomeExibicao = nomeExibicao;
        }

        public String getNomeExibicao() {
            return nomeExibicao;
        }
    }
}