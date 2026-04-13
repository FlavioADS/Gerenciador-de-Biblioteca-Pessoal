package com.biblioteca.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class OpenLibraryClientTest {

    private static final HttpServer HTTP_SERVER = criarServidorHttp();

    static {
        HTTP_SERVER.start();
    }

    @Autowired
    private OpenLibraryClient openLibraryClient;

    @DynamicPropertySource
    static void registrarHttpServer(DynamicPropertyRegistry registry) {
        registry.add("biblioteca.open-library.base-url", () -> "http://localhost:" + HTTP_SERVER.getAddress().getPort());
    }

    @AfterAll
    static void pararServidor() {
        HTTP_SERVER.stop(0);
    }

    @Test
    void deveBuscarUrlDaCapaDaOpenLibrary() {
        Optional<String> url = openLibraryClient.buscarUrlCapaPorIsbn("9780140449136");

        assertTrue(url.isPresent());
        assertEquals("https://covers.openlibrary.org/b/id/12345-M.jpg", url.get());
    }

    @Test
    void deveRetornarVazioQuandoApiNaoInformarCapa() {
        Optional<String> url = openLibraryClient.buscarUrlCapaPorIsbn("0000000000");

        assertTrue(url.isEmpty());
    }

    private static HttpServer criarServidorHttp() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/isbn/9780140449136.json", new FixtureHandler("/vcr/openlibrary/9780140449136.json"));
            server.createContext("/isbn/0000000000.json", new FixtureHandler("/vcr/openlibrary/0000000000.json"));
            return server;
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao iniciar servidor de fixtures", e);
        }
    }

    private static class FixtureHandler implements HttpHandler {
        private final String resourcePath;

        private FixtureHandler(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] responseBody = carregarFixture(resourcePath);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseBody.length);
            try (exchange; var outputStream = exchange.getResponseBody()) {
                outputStream.write(responseBody);
            }
        }
    }

    private static byte[] carregarFixture(String resourcePath) throws IOException {
        try (InputStream inputStream = OpenLibraryClientTest.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Fixture não encontrada: " + resourcePath);
            }
            return inputStream.readAllBytes();
        }
    }
}