package com.rodrigopresida.agenda.service;

import com.rodrigopresida.agenda.dto.ContatoRequestDTO;
import com.rodrigopresida.agenda.dto.ContatoResponseDTO;
import com.rodrigopresida.agenda.exception.ContatoNotFoundException;
import com.rodrigopresida.agenda.model.Categoria;
import com.rodrigopresida.agenda.model.Contato;
import com.rodrigopresida.agenda.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContatoService {

    private final ContatoRepository repository;

    public List<ContatoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(ContatoResponseDTO::fromEntity)
                .toList();
    }

    public List<ContatoResponseDTO> listarAtivos() {
        return repository.findByAtivoTrue()
                .stream()
                .map(ContatoResponseDTO::fromEntity)
                .toList();
    }

    public List<ContatoResponseDTO> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(ContatoResponseDTO::fromEntity)
                .toList();
    }

    public List<ContatoResponseDTO> buscarPorCategoria(Categoria categoria) {
        return repository.findByCategoria(categoria)
                .stream()
                .map(ContatoResponseDTO::fromEntity)
                .toList();
    }

    public ContatoResponseDTO buscarPorId(Long id) {
        Contato contato = repository.findById(id)
                .orElseThrow(() -> new ContatoNotFoundException(id));
        return ContatoResponseDTO.fromEntity(contato);
    }

    public ContatoResponseDTO criar(ContatoRequestDTO dto) {
        Contato contato = Contato.builder()
                .nome(dto.nome())
                .telefone(dto.telefone())
                .email(dto.email())
                .ativo(dto.ativo() != null ? dto.ativo() : true)
                .categoria(dto.categoria())
                .build();
        return ContatoResponseDTO.fromEntity(repository.save(contato));
    }

    public ContatoResponseDTO atualizar(Long id, ContatoRequestDTO dto) {
        Contato contato = repository.findById(id)
                .orElseThrow(() -> new ContatoNotFoundException(id));

        contato.setNome(dto.nome());
        contato.setTelefone(dto.telefone());
        contato.setEmail(dto.email());
        contato.setAtivo(dto.ativo() != null ? dto.ativo() : contato.getAtivo());
        contato.setCategoria(dto.categoria());

        return ContatoResponseDTO.fromEntity(repository.save(contato));
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ContatoNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
