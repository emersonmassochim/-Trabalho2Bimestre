package br.unipar.backend.minhaapi.repository;

import br.unipar.backend.minhaapi.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FilmeRepository extends JpaRepository<Filme, Long>, JpaSpecificationExecutor<Filme> {

    List<Filme> findByGeneroId(Long generoId);

    boolean existsByGeneroId(Long generoId);

    boolean existsByTituloIgnoreCaseAndAno(String titulo, int ano);

    boolean existsByTituloIgnoreCaseAndAnoAndIdNot(String titulo, int ano, Long id);
}