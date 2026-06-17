package com.sistema.sistema.Security.User;

import lombok.Builder;

@Builder

public record UserResponse(
        String username,
        String password
) {}
