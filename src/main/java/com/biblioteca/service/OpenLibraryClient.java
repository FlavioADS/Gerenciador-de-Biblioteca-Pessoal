package com.biblioteca.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class OpenLibraryClient {

    private final RestClient restClient;

    public OpenLibraryClient(RestClient.Builder builder,
                             @Value("${biblioteca.open-library.base-url:https://openlibrary.org}") String baseUrl) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public Optional<String> buscarUrlCapaPorIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return Optional.empty();
        }

        try {
            LivroOpenLibraryResponse resposta = restClient.get()
                    .uri("/isbn/{isbn}.json", isbn.trim())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(LivroOpenLibraryResponse.class);

            if (resposta == null || resposta.covers() == null || resposta.covers().isEmpty()) {
                return Optional.empty();
            }

            Integer coverId = resposta.covers().get(0);
            return Optional.of("https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    record LivroOpenLibraryResponse(List<Integer> covers) {
    }
}