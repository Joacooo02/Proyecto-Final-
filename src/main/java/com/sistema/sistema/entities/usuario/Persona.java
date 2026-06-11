package com.sistema.sistema.entities.usuario;

import com.sistema.sistema.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "Persona")
@Inheritance(strategy = InheritanceType.JOINED)
//@PrimaryKeyJoinColumn(name = "idPersona")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;

    @Column(nullable = false,length = 50)
    private String nombre;

    @Column(nullable = false,length = 50)
    private String apellido;

    @Column(nullable = false,length = 50,unique = true)
    private String dni;

    @Column(length = 50)
    private String telefono;

    private LocalDate fechaNacimiento;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rolUsuario;
}
