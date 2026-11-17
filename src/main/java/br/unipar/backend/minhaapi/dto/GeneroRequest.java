package br.unipar.backend.minhaapi.dto;

import jakarta.validation.constraints.NotBlank;

public record GeneroRequest(
        @NotBlank(message = "O nome do gênero é obrigatório")
        String nome
) {}