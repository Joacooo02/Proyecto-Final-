package com.sistema.sistema.entities.usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Administrador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAdministrador;

    @OneToOne
    @JoinColumn(name = "idPersona", nullable = false)
    private Persona persona;
}
