package com.rodrigopresida.agenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigopresida.agenda.dto.ContatoRequestDTO;
import com.rodrigopresida.agenda.dto.ContatoResponseDTO;
import com.rodrigopresida.agenda.exception.ContatoNotFoundException;
import com.rodrigopresida.agenda.model.Categoria;
import com.rodrigopresida.agenda.service.ContatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContatoController.class)
@DisplayName("ContatoController — testes de integracao da camada web")
class ContatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContatoService service;

    private ContatoResponseDTO responseDTO;
    private ContatoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new ContatoResponseDTO(
                1L,
                "Maria Silva",
                "(11) 99999-1234",
                "maria@email.com",
                true,
                Categoria.TRABALHO,
                LocalDateTime.of(2026, 1, 15, 10, 0)
        );

        requestDTO = new ContatoRequestDTO(
                "Maria Silva",
                "(11) 99999-1234",
                "maria@email.com",
                Categoria.TRABALHO,
                true
        );
    }

    // ------------------------------------------------------------------ //
    //  GET /api/contatos
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("GET /api/contatos: deve retornar 200 com lista de contatos")
    void listarTodos_deveRetornar200ComLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/contatos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Maria Silva")))
                .andExpect(jsonPath("$[0].categoria", is("TRABALHO")));
    }

    @Test
    @DisplayName("GET /api/contatos: deve retornar 200 com lista vazia")
    void listarTodos_semContatos_deveRetornar200Vazio() throws Exception {
        when(service.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/contatos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ------------------------------------------------------------------ //
    //  GET /api/contatos/ativos
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("GET /api/contatos/ativos: deve retornar 200 com contatos ativos")
    void listarAtivos_deveRetornar200() throws Exception {
        when(service.listarAtivos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/contatos/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ativo", is(true)));
    }

    // ------------------------------------------------------------------ //
    //  GET /api/contatos/buscar
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("GET /api/contatos/buscar?nome=: deve retornar 200 com resultado da busca")
    void buscarPorNome_deveRetornar200() throws Exception {
        when(service.buscarPorNome("maria")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/contatos/buscar").param("nome", "maria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", containsStringIgnoringCase("Maria")));
    }

    // ------------------------------------------------------------------ //
    //  GET /api/contatos/categoria/{categoria}
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("GET /api/contatos/categoria/TRABALHO: deve retornar 200 filtrado por categoria")
    void buscarPorCategoria_deveRetornar200() throws Exception {
        when(service.buscarPorCategoria(Categoria.TRABALHO)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/contatos/categoria/TRABALHO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoria", is("TRABALHO")));
    }

    // ------------------------------------------------------------------ //
    //  GET /api/contatos/{id}
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("GET /api/contatos/1: deve retornar 200 com o contato encontrado")
    void buscarPorId_idExistente_deveRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/contatos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Maria Silva")))
                .andExpect(jsonPath("$.email", is("maria@email.com")));
    }

    @Test
    @DisplayName("GET /api/contatos/99: deve retornar 404 quando id nao existe")
    void buscarPorId_idInexistente_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ContatoNotFoundException(99L));

        mockMvc.perform(get("/api/contatos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    // ------------------------------------------------------------------ //
    //  POST /api/contatos
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("POST /api/contatos: deve retornar 201 com o contato criado")
    void criar_dadosValidos_deveRetornar201() throws Exception {
        when(service.criar(any(ContatoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Maria Silva")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("POST /api/contatos: deve retornar 400 quando nome esta vazio")
    void criar_nomeVazio_deveRetornar400() throws Exception {
        ContatoRequestDTO invalido = new ContatoRequestDTO(
                "",
                "(11) 99999-1234",
                "maria@email.com",
                Categoria.TRABALHO,
                true
        );

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @DisplayName("POST /api/contatos: deve retornar 400 quando telefone esta no formato errado")
    void criar_telefoneInvalido_deveRetornar400() throws Exception {
        ContatoRequestDTO invalido = new ContatoRequestDTO(
                "Maria Silva",
                "11999991234",
                "maria@email.com",
                Categoria.TRABALHO,
                true
        );

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/contatos: deve retornar 400 quando email esta invalido")
    void criar_emailInvalido_deveRetornar400() throws Exception {
        ContatoRequestDTO invalido = new ContatoRequestDTO(
                "Maria Silva",
                "(11) 99999-1234",
                "nao-e-um-email",
                Categoria.TRABALHO,
                true
        );

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ------------------------------------------------------------------ //
    //  PUT /api/contatos/{id}
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("PUT /api/contatos/1: deve retornar 200 com contato atualizado")
    void atualizar_idExistente_deveRetornar200() throws Exception {
        when(service.atualizar(eq(1L), any(ContatoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/contatos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Maria Silva")));
    }

    @Test
    @DisplayName("PUT /api/contatos/99: deve retornar 404 quando id nao existe")
    void atualizar_idInexistente_deveRetornar404() throws Exception {
        when(service.atualizar(eq(99L), any(ContatoRequestDTO.class)))
                .thenThrow(new ContatoNotFoundException(99L));

        mockMvc.perform(put("/api/contatos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------------ //
    //  DELETE /api/contatos/{id}
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("DELETE /api/contatos/1: deve retornar 204 quando contato excluido")
    void excluir_idExistente_deveRetornar204() throws Exception {
        doNothing().when(service).excluir(1L);

        mockMvc.perform(delete("/api/contatos/1"))
                .andExpect(status().isNoContent());

        verify(service).excluir(1L);
    }

    @Test
    @DisplayName("DELETE /api/contatos/99: deve retornar 404 quando id nao existe")
    void excluir_idInexistente_deveRetornar404() throws Exception {
        doThrow(new ContatoNotFoundException(99L)).when(service).excluir(99L);

        mockMvc.perform(delete("/api/contatos/99"))
                .andExpect(status().isNotFound());
    }
}
