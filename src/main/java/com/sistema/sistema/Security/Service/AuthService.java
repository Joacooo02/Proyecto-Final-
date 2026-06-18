package com.sistema.sistema.Security.Service;

import com.sistema.sistema.Security.AccesoDenegadoException;
import com.sistema.sistema.Security.Controller.LoginRequest;
import com.sistema.sistema.Security.Controller.RegisterRequest;
import com.sistema.sistema.Security.Controller.TokenResponse;
import com.sistema.sistema.Security.Repository.Token;
import com.sistema.sistema.Security.Repository.TokenRepository;
import com.sistema.sistema.Security.User.User;
import com.sistema.sistema.Security.User.UserRepository;
import com.sistema.sistema.Security.UsuarioNotFoundException;
import com.sistema.sistema.enums.RolUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register (RegisterRequest request){

        if (request.role() == RolUsuario.ADMIN) {
            throw new AccesoDenegadoException("No podés registrarte como ADMIN.");
        }

        var usuario = User.builder()
                .username(request.email())
                .password(passwordEncoder.encode(request.contrasena()))
                .email(request.email())
                .role(request.role())
                .build();
        var savedUser = userRepository.save(usuario);
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse login (LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.contrasena()
                )
        );
        var usuario = userRepository.findByEmail(request.email()).orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);
        saveUserToken(usuario, jwtToken);
        return new TokenResponse (jwtToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    public TokenResponse refreshToken(final String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Token Invalido.");
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail == null){
            throw new IllegalArgumentException ("Token de Refresh Invalido.");
        }

        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNotFoundException(userEmail));

        if(!jwtService.isTokenValid(refreshToken, user)){
            throw new IllegalArgumentException("Token de Refresco Invalido");
        }

        final String accessToken = jwtService.generateToken(user);
        saveUserToken(user, accessToken);
        return new TokenResponse(accessToken, refreshToken);






    }
}
