package com.sistema.sistema.dto;

import com.sistema.sistema.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ComisionHorarioDto {
    private Long idComision;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
