package com.sistema.sistema.entities.areaAdministrativa;

import com.sistema.sistema.entities.enums.ConceptoCuota;
import com.sistema.sistema.entities.enums.EstadoCuota;
import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Cuota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuota;

    @ManyToOne
    @JoinColumn(name = "idAlumno", nullable = false)
    private Alumno alumno;

    private Integer valorCuota;
    private LocalDate fechaPago;
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConceptoCuota conceptoCuota;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuota estadoCuota;
}
