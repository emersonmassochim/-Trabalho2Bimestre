package br.unipar.backend.minhaapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilmeRequest(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        String diretor,

        @NotNull(message = "O ano é obrigatório")
        @Min(value = 1888, message = "O ano deve ser maior ou igual a 1888")
        Integer ano,

        @NotNull(message = "O gênero é obrigatório")
        Long generoId
) {}