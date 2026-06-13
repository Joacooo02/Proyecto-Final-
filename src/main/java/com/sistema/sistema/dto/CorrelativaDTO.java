package com.sistema.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CorrelativaDTO {
    private Long id;
    private String nombre;
}
