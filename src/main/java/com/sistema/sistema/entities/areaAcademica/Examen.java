package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.entities.enums.TipoExamen;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Examen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExamen;

    @ManyToOne
    @JoinColumn(name = "idMateria", nullable = false)
    private Materia materia;

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoExamen tipoExamen;
}
