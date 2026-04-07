package com.rodrigopresida.agenda.exception;

public class ContatoNotFoundException extends RuntimeException {

    public ContatoNotFoundException(Long id) {
        super("Contato nao encontrado com id: " + id);
    }
}
