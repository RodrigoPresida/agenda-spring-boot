package com.rodrigopresida.agenda.dto;

import com.rodrigopresida.agenda.model.Categoria;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ContatoRequestDTO(

        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotBlank(message = "Telefone e obrigatorio")
        @Pattern(regexp = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$",
                 message = "Telefone deve estar no formato (99) 99999-9999")
        String telefone,

        @Email(message = "E-mail invalido")
        String email,

        @NotNull(message = "Categoria e obrigatoria")
        Categoria categoria,

        Boolean ativo
) {}
