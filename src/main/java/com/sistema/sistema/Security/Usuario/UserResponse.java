package com.sistema.sistema.Security.Usuario;

import lombok.Builder;

@Builder

public record UserResponse(
        String username,
        String password
) {}
