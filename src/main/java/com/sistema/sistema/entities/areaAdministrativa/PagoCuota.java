package com.sistema.sistema.entities.areaAdministrativa;

import com.sistema.sistema.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PagoCuota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoCuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagoCuota;

    @ManyToOne
    @JoinColumn(name = "idCuota", nullable = false)
    private Cuota cuota;

    @Column(nullable = false)
    private Integer montoPagado;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
}
