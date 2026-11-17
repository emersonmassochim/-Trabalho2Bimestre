package br.unipar.backend.minhaapi.controller;

import br.unipar.backend.minhaapi.dto.FilmeRequest;
import br.unipar.backend.minhaapi.dto.FilmeResponse;
import br.unipar.backend.minhaapi.service.FilmeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    // Listar Filmes (Com Filtros Opcionais)
    @GetMapping
    public ResponseEntity<List<FilmeResponse>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String diretor,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Long generoId) {
        return ResponseEntity.ok(filmeService.buscar(titulo, diretor, ano, generoId));
    }

    // Buscar um Filme pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<FilmeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(filmeService.buscarPorId(id));
    }

    // Cadastrar um Novo Filme
    @PostMapping
    public ResponseEntity<FilmeResponse> cadastrar(@Valid @RequestBody FilmeRequest request) {
        FilmeResponse criado = filmeService.criar(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();

        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar um Filme Via ID
    @PutMapping("/{id}")
    public ResponseEntity<FilmeResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FilmeRequest request) {
        return ResponseEntity.ok(filmeService.atualizar(id, request));
    }

    // Excluir um Filme Via ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        filmeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}