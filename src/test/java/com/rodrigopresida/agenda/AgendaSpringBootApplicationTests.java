package com.rodrigopresida.agenda;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Smoke test — contexto da aplicacao carrega sem erros")
class AgendaSpringBootApplicationTests {

    @Test
    @DisplayName("contextLoads: deve inicializar o contexto Spring com sucesso")
    void contextLoads() {
    }
}
