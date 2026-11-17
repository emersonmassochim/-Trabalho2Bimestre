package br.unipar.backend.minhaapi.service;

import br.unipar.backend.minhaapi.dto.RegistroRequest;
import br.unipar.backend.minhaapi.dto.UsuarioResponse;
import br.unipar.backend.minhaapi.exception.RegraNegocioException;
import br.unipar.backend.minhaapi.model.Usuario;
import br.unipar.backend.minhaapi.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByLogin(request.login())) {
            throw new RegraNegocioException("Já existe um usuário com o login informado");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(request.login());
        usuario.setSenha(passwordEncoder.encode(request.senha()));

        Usuario salvo = usuarioRepository.save(usuario);

        return new UsuarioResponse(salvo.getId(), salvo.getLogin());
    }
}