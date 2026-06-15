package com.sistema.sistema.Security.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void crearUsuario(RegistroUsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role("USER")
                .enabled(true)
                .build();
        usuarioRepository.save(usuario);
    }
}
