package com.sistema.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeEstudioDTO {
    private Long idPlan;
    private Long idCarrera;
    private String nombre;
}
