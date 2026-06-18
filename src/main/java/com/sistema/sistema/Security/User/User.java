package com.sistema.sistema.Security.User;

import com.sistema.sistema.Security.Repository.Token;
import com.sistema.sistema.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;


    private String password;
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Token> tokens;

    @Enumerated(EnumType.STRING)
    private RolUsuario role;
}
