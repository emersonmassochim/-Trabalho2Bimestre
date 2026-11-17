package br.unipar.backend.minhaapi.service;

import br.unipar.backend.minhaapi.dto.GeneroRequest;
import br.unipar.backend.minhaapi.dto.GeneroResponse;
import br.unipar.backend.minhaapi.exception.RecursoNaoEncontradoException;
import br.unipar.backend.minhaapi.exception.RegraNegocioException;
import br.unipar.backend.minhaapi.model.Genero;
import br.unipar.backend.minhaapi.repository.FilmeRepository;
import br.unipar.backend.minhaapi.repository.GeneroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;
    private final FilmeRepository filmeRepository;

    public GeneroService(GeneroRepository generoRepository, FilmeRepository filmeRepository) {
        this.generoRepository = generoRepository;
        this.filmeRepository = filmeRepository;
    }

    @Transactional(readOnly = true)
    public List<GeneroResponse> listar() {
        return generoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GeneroResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public GeneroResponse criar(GeneroRequest request) {
        if (generoRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new RegraNegocioException("Já existe um gênero com o nome informado");
        }

        Genero genero = new Genero();
        genero.setNome(request.nome());

        return toResponse(generoRepository.save(genero));
    }

    @Transactional
    public GeneroResponse atualizar(Long id, GeneroRequest request) {
        Genero genero = buscarEntidade(id);

        if (generoRepository.existsByNomeIgnoreCaseAndIdNot(request.nome(), id)) {
            throw new RegraNegocioException("Já existe um gênero com o nome informado");
        }

        genero.setNome(request.nome());

        return toResponse(generoRepository.save(genero));
    }

    @Transactional
    public void excluir(Long id) {
        Genero genero = buscarEntidade(id);

        if (filmeRepository.existsByGeneroId(id)) {
            throw new RegraNegocioException("Não é possível excluir um gênero que possui filmes");
        }

        generoRepository.delete(genero);
    }

    private Genero buscarEntidade(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gênero não encontrado: " + id));
    }

    private GeneroResponse toResponse(Genero genero) {
        return new GeneroResponse(genero.getId(), genero.getNome());
    }
}