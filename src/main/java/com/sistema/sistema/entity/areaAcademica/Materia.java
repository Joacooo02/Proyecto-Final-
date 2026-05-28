package com.sistema.sistema.entity.areaAcademica;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Materia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMateria;
    private String nombre;
    private Integer cuatrimestre;
    private Integer anioCursando;
}
