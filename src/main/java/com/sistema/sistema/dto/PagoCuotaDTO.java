package com.sistema.sistema.dto;

import com.sistema.sistema.enums.MetodoPago;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoCuotaDTO {

    private Long idCuota;
    private Integer montoPagado;
    private LocalDate fechaPago;
    private MetodoPago metodoPago;
}
