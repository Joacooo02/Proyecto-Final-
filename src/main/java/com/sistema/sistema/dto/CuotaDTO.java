package com.sistema.sistema.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Long id;
    private Long idAlumno;
    private Integer mes;
    private Integer anio;
    private Integer valorCuota;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;
    private ConceptoCuota conceptoCuota;
    private EstadoCuota estadoCuota;

}
