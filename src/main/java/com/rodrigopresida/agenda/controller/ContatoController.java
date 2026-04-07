package com.rodrigopresida.agenda.controller;

import com.rodrigopresida.agenda.dto.ContatoRequestDTO;
import com.rodrigopresida.agenda.dto.ContatoResponseDTO;
import com.rodrigopresida.agenda.model.Categoria;
import com.rodrigopresida.agenda.service.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contatos")
@RequiredArgsConstructor
@Tag(name = "Contatos", description = "Operacoes CRUD de contatos da agenda")
public class ContatoController {

    private final ContatoService service;

    @GetMapping
    @Operation(summary = "Lista todos os contatos")
    public ResponseEntity<List<ContatoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Lista apenas contatos ativos")
    public ResponseEntity<List<ContatoResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca contatos por nome (parcial, case-insensitive)")
    public ResponseEntity<List<ContatoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Filtra contatos por categoria")
    public ResponseEntity<List<ContatoResponseDTO>> buscarPorCategoria(@PathVariable Categoria categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca contato por ID")
    public ResponseEntity<ContatoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo contato")
    public ResponseEntity<ContatoResponseDTO> criar(@RequestBody @Valid ContatoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um contato existente")
    public ResponseEntity<ContatoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ContatoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um contato")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
