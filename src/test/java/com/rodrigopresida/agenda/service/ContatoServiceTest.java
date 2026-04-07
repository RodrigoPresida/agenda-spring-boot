package com.rodrigopresida.agenda.service;

import com.rodrigopresida.agenda.dto.ContatoRequestDTO;
import com.rodrigopresida.agenda.dto.ContatoResponseDTO;
import com.rodrigopresida.agenda.exception.ContatoNotFoundException;
import com.rodrigopresida.agenda.model.Categoria;
import com.rodrigopresida.agenda.model.Contato;
import com.rodrigopresida.agenda.repository.ContatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContatoService — testes unitarios")
class ContatoServiceTest {

    @Mock
    private ContatoRepository repository;

    @InjectMocks
    private ContatoService service;

    private Contato contatoBase;
    private ContatoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        contatoBase = Contato.builder()
                .id(1L)
                .nome("Maria Silva")
                .telefone("(11) 99999-1234")
                .email("maria@email.com")
                .ativo(true)
                .categoria(Categoria.TRABALHO)
                .dataCriacao(LocalDateTime.of(2026, 1, 15, 10, 0))
                .build();

        requestDTO = new ContatoRequestDTO(
                "Maria Silva",
                "(11) 99999-1234",
                "maria@email.com",
                Categoria.TRABALHO,
                true
        );
    }

    // ------------------------------------------------------------------ //
    //  listarTodos
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("listarTodos: deve retornar lista com todos os contatos mapeados")
    void listarTodos_deveRetornarListaCompleta() {
        Contato segundo = Contato.builder()
                .id(2L)
                .nome("Carlos Souza")
                .telefone("(21) 98888-5678")
                .email("carlos@email.com")
                .ativo(false)
                .categoria(Categoria.PESSOAL)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(repository.findAll()).thenReturn(List.of(contatoBase, segundo));

        List<ContatoResponseDTO> resultado = service.listarTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nome()).isEqualTo("Maria Silva");
        assertThat(resultado.get(1).nome()).isEqualTo("Carlos Souza");
        verify(repository).findAll();
    }

    @Test
    @DisplayName("listarTodos: deve retornar lista vazia quando nao ha contatos")
    void listarTodos_semContatos_deveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(List.of());

        List<ContatoResponseDTO> resultado = service.listarTodos();

        assertThat(resultado).isEmpty();
    }

    // ------------------------------------------------------------------ //
    //  listarAtivos
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("listarAtivos: deve retornar apenas contatos com ativo=true")
    void listarAtivos_deveRetornarSomenteAtivos() {
        when(repository.findByAtivoTrue()).thenReturn(List.of(contatoBase));

        List<ContatoResponseDTO> resultado = service.listarAtivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).ativo()).isTrue();
        verify(repository).findByAtivoTrue();
    }

    // ------------------------------------------------------------------ //
    //  buscarPorNome
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("buscarPorNome: deve retornar contatos que contenham o nome (case-insensitive)")
    void buscarPorNome_deveRetornarContatosFiltrados() {
        when(repository.findByNomeContainingIgnoreCase("maria")).thenReturn(List.of(contatoBase));

        List<ContatoResponseDTO> resultado = service.buscarPorNome("maria");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nome()).containsIgnoringCase("Maria");
        verify(repository).findByNomeContainingIgnoreCase("maria");
    }

    @Test
    @DisplayName("buscarPorNome: deve retornar lista vazia quando nome nao encontrado")
    void buscarPorNome_semResultado_deveRetornarVazio() {
        when(repository.findByNomeContainingIgnoreCase("xyz")).thenReturn(List.of());

        List<ContatoResponseDTO> resultado = service.buscarPorNome("xyz");

        assertThat(resultado).isEmpty();
    }

    // ------------------------------------------------------------------ //
    //  buscarPorCategoria
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("buscarPorCategoria: deve retornar contatos da categoria informada")
    void buscarPorCategoria_deveRetornarContatosDaCategoria() {
        when(repository.findByCategoria(Categoria.TRABALHO)).thenReturn(List.of(contatoBase));

        List<ContatoResponseDTO> resultado = service.buscarPorCategoria(Categoria.TRABALHO);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).categoria()).isEqualTo(Categoria.TRABALHO);
        verify(repository).findByCategoria(Categoria.TRABALHO);
    }

    // ------------------------------------------------------------------ //
    //  buscarPorId
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("buscarPorId: deve retornar DTO quando id existe")
    void buscarPorId_idExistente_deveRetornarDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(contatoBase));

        ContatoResponseDTO resultado = service.buscarPorId(1L);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Maria Silva");
        assertThat(resultado.email()).isEqualTo("maria@email.com");
    }

    @Test
    @DisplayName("buscarPorId: deve lancar ContatoNotFoundException quando id nao existe")
    void buscarPorId_idInexistente_deveLancarException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(ContatoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ------------------------------------------------------------------ //
    //  criar
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("criar: deve salvar e retornar DTO do contato criado")
    void criar_deveRetornarDTODoContatoSalvo() {
        when(repository.save(any(Contato.class))).thenReturn(contatoBase);

        ContatoResponseDTO resultado = service.criar(requestDTO);

        assertThat(resultado.nome()).isEqualTo("Maria Silva");
        assertThat(resultado.categoria()).isEqualTo(Categoria.TRABALHO);
        assertThat(resultado.ativo()).isTrue();
        verify(repository).save(any(Contato.class));
    }

    @Test
    @DisplayName("criar: quando ativo nao informado no DTO, deve salvar como true")
    void criar_semAtivoNoDTO_devePadronizarComoTrue() {
        ContatoRequestDTO dtoSemAtivo = new ContatoRequestDTO(
                "Ana Lima",
                "(31) 97777-0001",
                null,
                Categoria.FAMILIA,
                null
        );

        Contato contatoSalvo = Contato.builder()
                .id(2L)
                .nome("Ana Lima")
                .telefone("(31) 97777-0001")
                .email(null)
                .ativo(true)
                .categoria(Categoria.FAMILIA)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(repository.save(any(Contato.class))).thenReturn(contatoSalvo);

        ContatoResponseDTO resultado = service.criar(dtoSemAtivo);

        assertThat(resultado.ativo()).isTrue();
    }

    // ------------------------------------------------------------------ //
    //  atualizar
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("atualizar: deve atualizar campos e retornar DTO atualizado")
    void atualizar_idExistente_deveRetornarDTOAtualizado() {
        ContatoRequestDTO dtoAtualizado = new ContatoRequestDTO(
                "Maria Oliveira",
                "(11) 91111-2222",
                "maria.oliveira@email.com",
                Categoria.PESSOAL,
                true
        );

        Contato contatoAtualizado = Contato.builder()
                .id(1L)
                .nome("Maria Oliveira")
                .telefone("(11) 91111-2222")
                .email("maria.oliveira@email.com")
                .ativo(true)
                .categoria(Categoria.PESSOAL)
                .dataCriacao(contatoBase.getDataCriacao())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(contatoBase));
        when(repository.save(any(Contato.class))).thenReturn(contatoAtualizado);

        ContatoResponseDTO resultado = service.atualizar(1L, dtoAtualizado);

        assertThat(resultado.nome()).isEqualTo("Maria Oliveira");
        assertThat(resultado.telefone()).isEqualTo("(11) 91111-2222");
        assertThat(resultado.categoria()).isEqualTo(Categoria.PESSOAL);
        verify(repository).findById(1L);
        verify(repository).save(any(Contato.class));
    }

    @Test
    @DisplayName("atualizar: deve lancar ContatoNotFoundException quando id nao existe")
    void atualizar_idInexistente_deveLancarException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(99L, requestDTO))
                .isInstanceOf(ContatoNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).save(any());
    }

    // ------------------------------------------------------------------ //
    //  excluir
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("excluir: deve chamar deleteById quando id existe")
    void excluir_idExistente_deveDeletar() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertThatCode(() -> service.excluir(1L)).doesNotThrowAnyException();

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("excluir: deve lancar ContatoNotFoundException quando id nao existe")
    void excluir_idInexistente_deveLancarException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.excluir(99L))
                .isInstanceOf(ContatoNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).deleteById(any());
    }
}
