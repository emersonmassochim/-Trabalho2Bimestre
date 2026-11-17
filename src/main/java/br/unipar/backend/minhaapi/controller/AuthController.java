package br.unipar.backend.minhaapi.controller;

import br.unipar.backend.minhaapi.dto.LoginResponse;
import br.unipar.backend.minhaapi.dto.RegistroRequest;
import br.unipar.backend.minhaapi.dto.UsuarioResponse;
import br.unipar.backend.minhaapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Cadastro de Usuário (Rota Aberta)
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(201).body(usuarioService.registrar(request));
    }

    // Login: Só Chega Aqui Quem Mandou Usuário e Senha Corretos (Basic Auth)
    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(Authentication authentication) {
        return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso", authentication.getName()));
    }
}