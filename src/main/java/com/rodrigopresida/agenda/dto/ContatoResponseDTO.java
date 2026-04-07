package com.rodrigopresida.agenda.dto;

import com.rodrigopresida.agenda.model.Categoria;
import com.rodrigopresida.agenda.model.Contato;

import java.time.LocalDateTime;

public record ContatoResponseDTO(
        Long id,
        String nome,
        String telefone,
        String email,
        Boolean ativo,
        Categoria categoria,
        LocalDateTime dataCriacao
) {
    public static ContatoResponseDTO fromEntity(Contato contato) {
        return new ContatoResponseDTO(
                contato.getId(),
                contato.getNome(),
                contato.getTelefone(),
                contato.getEmail(),
                contato.getAtivo(),
                contato.getCategoria(),
                contato.getDataCriacao()
        );
    }
}
