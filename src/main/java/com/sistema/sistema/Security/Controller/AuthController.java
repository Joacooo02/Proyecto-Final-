package com.sistema.sistema.Security.Controller;


import com.sistema.sistema.Security.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService service;

    @PostMapping("/register") /// Usuario se quiere Registrar
    public ResponseEntity<TokenResponse> register (@RequestBody final RegisterRequest request){
        final TokenResponse token = service.register(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login") /// Usuario se quiere Loguear
    public ResponseEntity<TokenResponse> login (@RequestBody final LoginRequest request) {
        final TokenResponse token = service.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh") /// Es para RefreshToken y queremos un AccesToken nuevo
    public TokenResponse refresh (@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        return service.refreshToken(authHeader);
    }

}
