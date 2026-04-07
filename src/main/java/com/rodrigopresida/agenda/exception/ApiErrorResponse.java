package com.rodrigopresida.agenda.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        int status,
        String erro,
        List<String> mensagens,
        LocalDateTime timestamp
) {
    public static ApiErrorResponse of(int status, String erro, List<String> mensagens) {
        return new ApiErrorResponse(status, erro, mensagens, LocalDateTime.now());
    }

    public static ApiErrorResponse of(int status, String erro, String mensagem) {
        return of(status, erro, List.of(mensagem));
    }
}
