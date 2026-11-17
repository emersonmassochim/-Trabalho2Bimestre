package br.unipar.backend.minhaapi.controller;

import br.unipar.backend.minhaapi.dto.FilmeResponse;
import br.unipar.backend.minhaapi.dto.GeneroRequest;
import br.unipar.backend.minhaapi.dto.GeneroResponse;
import br.unipar.backend.minhaapi.service.FilmeService;
import br.unipar.backend.minhaapi.service.GeneroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/generos")
public class GeneroController {

    private final GeneroService generoService;
    private final FilmeService filmeService;

    public GeneroController(GeneroService generoService, FilmeService filmeService) {
        this.generoService = generoService;
        this.filmeService = filmeService;
    }

    // Listar Todos os Gêneros
    @GetMapping
    public ResponseEntity<List<GeneroResponse>> listar() {
        return ResponseEntity.ok(generoService.listar());
    }

    // Buscar um Gênero pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.buscarPorId(id));
    }

    // Consulta Com Filtro no Path: Filmes de um Gênero
    @GetMapping("/{id}/filmes")
    public ResponseEntity<List<FilmeResponse>> listarFilmesDoGenero(@PathVariable Long id) {
        return ResponseEntity.ok(filmeService.listarPorGenero(id));
    }

    // Cadastrar um Novo Gênero
    @PostMapping
    public ResponseEntity<GeneroResponse> cadastrar(@Valid @RequestBody GeneroRequest request) {
        GeneroResponse criado = generoService.criar(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();

        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar um Gênero Via ID
    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody GeneroRequest request) {
        return ResponseEntity.ok(generoService.atualizar(id, request));
    }

    // Excluir um Gênero Via ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        generoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}