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
    @Column(name = "id_cuota")
    private Long idCuota;

    @ManyToOne
    @JoinColumn(name = "id_alumno",referencedColumnName = "idPersona", nullable = false)
    private Alumno alumno;

    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes")
    private Integer mes;

    @Column(name = "valor_cuota")
    private Integer valorCuota;
    @Column(name = "fecha_pago")
    private LocalDate fechaPago;
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "concepto_cuota", length = 50)
    private ConceptoCuota conceptoCuota;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuota", length = 50)
    private EstadoCuota estadoCuota;
}
