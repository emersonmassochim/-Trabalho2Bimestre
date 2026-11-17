package br.unipar.backend.minhaapi.dto;

public record FilmeResponse(
        Long id,
        String titulo,
        String diretor,
        int ano,
        Long generoId,
        String generoNome
) {}