package com.sistema.sistema.entities.areaAcademica;
import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Nota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNota;

    @ManyToOne
    @JoinColumn(name = "idExamen", nullable = false)
    private Examen examen;

    @ManyToOne
    @JoinColumn(name = "idAlumno", nullable = false)
    private Alumno alumno;

    private Integer nota;
    private LocalDate fechaRegistro;
}