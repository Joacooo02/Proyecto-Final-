package com.sistema.sistema.Security;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {
    @GetMapping("/publico/saludo")
    public String publico() {
        return "Endpoint público";
    }
    @GetMapping("/perfil")
    public String perfil(Authentication authentication) {
        return "Usuario autenticado: " + authentication.getName();
    }
    @GetMapping("/admin/panel")
    public String admin() {
        return "Panel de administrador";
    }
}
