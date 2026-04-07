package com.rodrigopresida.agenda.repository;

import com.rodrigopresida.agenda.model.Categoria;
import com.rodrigopresida.agenda.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByAtivoTrue();

    List<Contato> findByCategoria(Categoria categoria);

    List<Contato> findByNomeContainingIgnoreCase(String nome);
}
