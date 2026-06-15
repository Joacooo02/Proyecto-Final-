package com.sistema.sistema.Security.Usuario;

import lombok.Builder;

@Builder

public record RegistroUsuarioDTO(
        String username,
        String password
) {}
