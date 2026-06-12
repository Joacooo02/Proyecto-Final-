package com.sistema.sistema.dto;


import com.sistema.sistema.enums.ConceptoCuota;
import com.sistema.sistema.enums.EstadoCuota;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuotaDTO {

    private Long idAlumno;
    private Integer mes;
    private Integer anio;
    private Integer valorCuota;
    private LocalDate fechaPago;
    private LocalDate fechaVencimiento;
    private ConceptoCuota conceptoCuota;
    private EstadoCuota estadoCuota;

}
