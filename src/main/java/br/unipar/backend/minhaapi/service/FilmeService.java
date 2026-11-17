package br.unipar.backend.minhaapi.service;

import br.unipar.backend.minhaapi.dto.FilmeRequest;
import br.unipar.backend.minhaapi.dto.FilmeResponse;
import br.unipar.backend.minhaapi.exception.RecursoNaoEncontradoException;
import br.unipar.backend.minhaapi.exception.RegraNegocioException;
import br.unipar.backend.minhaapi.model.Filme;
import br.unipar.backend.minhaapi.model.Genero;
import br.unipar.backend.minhaapi.repository.FilmeRepository;
import br.unipar.backend.minhaapi.repository.GeneroRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilmeService {

    private final FilmeRepository filmeRepository;
    private final GeneroRepository generoRepository;

    public FilmeService(FilmeRepository filmeRepository, GeneroRepository generoRepository) {
        this.filmeRepository = filmeRepository;
        this.generoRepository = generoRepository;
    }

    @Transactional(readOnly = true)
    public FilmeResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    // Consulta com filtro no path: GET /generos/{id}/filmes
    @Transactional(readOnly = true)
    public List<FilmeResponse> listarPorGenero(Long generoId) {
        if (!generoRepository.existsById(generoId)) {
            throw new RecursoNaoEncontradoException("Gênero não encontrado: " + generoId);
        }

        return filmeRepository.findByGeneroId(generoId).stream()
                .map(this::toResponse)
                .toList();
    }

    // Consulta com múltiplos parâmetros: GET /filmes?titulo=...&diretor=...&ano=...&generoId=...
    // Sem nenhum parâmetro, devolve todos os filmes.
    @Transactional(readOnly = true)
    public List<FilmeResponse> buscar(String titulo, String diretor, Integer ano, Long generoId) {
        Specification<Filme> spec = (root, query, cb) -> cb.conjunction();

        if (titulo != null && !titulo.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
        }
        if (diretor != null && !diretor.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("diretor")), "%" + diretor.toLowerCase() + "%"));
        }
        if (ano != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("ano"), ano));
        }
        if (generoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("genero").get("id"), generoId));
        }

        return filmeRepository.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FilmeResponse criar(FilmeRequest request) {
        if (filmeRepository.existsByTituloIgnoreCaseAndAno(request.titulo(), request.ano())) {
            throw new RegraNegocioException("Já existe um filme com esse título e ano");
        }

        Filme filme = new Filme();
        preencher(filme, request);

        return toResponse(filmeRepository.save(filme));
    }

    @Transactional
    public FilmeResponse atualizar(Long id, FilmeRequest request) {
        Filme filme = buscarEntidade(id);

        if (filmeRepository.existsByTituloIgnoreCaseAndAnoAndIdNot(request.titulo(), request.ano(), id)) {
            throw new RegraNegocioException("Já existe um filme com esse título e ano");
        }

        preencher(filme, request);

        return toResponse(filmeRepository.save(filme));
    }

    @Transactional
    public void excluir(Long id) {
        filmeRepository.delete(buscarEntidade(id));
    }

    private void preencher(Filme filme, FilmeRequest request) {
        Genero genero = generoRepository.findById(request.generoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gênero não encontrado: " + request.generoId()));

        filme.setTitulo(request.titulo());
        filme.setDiretor(request.diretor());
        filme.setAno(request.ano());
        filme.setGenero(genero);
    }

    private Filme buscarEntidade(Long id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Filme não encontrado: " + id));
    }

    private FilmeResponse toResponse(Filme filme) {
        return new FilmeResponse(
                filme.getId(),
                filme.getTitulo(),
                filme.getDiretor(),
                filme.getAno(),
                filme.getGenero().getId(),
                filme.getGenero().getNome());
    }
}