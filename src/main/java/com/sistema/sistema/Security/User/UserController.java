package com.sistema.sistema.Security.User;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor

public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<UserResponse> getUsers(){
        final var users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(user -> new UserResponse(user.getName(), user.getEmail()))
                .toList();
    }
}
