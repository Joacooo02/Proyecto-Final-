package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.EstadoMateria;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Alumno_Materia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_alumno",nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "id_materia",nullable = false)
    private Materia materia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoMateria estado = EstadoMateria.PENDIENTE;

    private Double notaParcial1;
    private Double notaParcial2;
    private Double notaFinal;

    private LocalDate fechaInscripcion;
    private LocalDate fechaRegularizacion;
    private LocalDate fechaAprobacion;
}
