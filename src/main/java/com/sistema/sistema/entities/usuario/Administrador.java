package com.sistema.sistema.entities.usuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Administrador")
@PrimaryKeyJoinColumn(name = "idPersona")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Administrador extends Persona{

}
