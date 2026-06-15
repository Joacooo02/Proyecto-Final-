package com.sistema.sistema.entities.areaAdministrativa;

import com.sistema.sistema.enums.ConceptoCuota;
import com.sistema.sistema.enums.EstadoCuota;
import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cuota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuota")
    private Long idCuota;

    @ManyToOne
    @JoinColumn(name = "idAlumno",referencedColumnName = "idPersona", nullable = false)
    private Alumno alumno;

    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes")
    private Integer mes;

    @Column(name = "valorCuota")
    private Integer valorCuota;
    @Column(name = "fechaPago")
    private LocalDate fechaPago;
    @Column(name = "fechaVencimiento")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "conceptoCuota", length = 50)
    private ConceptoCuota conceptoCuota;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoCuota", length = 50)
    private EstadoCuota estadoCuota;
}
