package com.sistema.sistema.entities.dto;

import com.sistema.sistema.entities.encuestas.RespuestaPregunta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncuestaRequest {
    private List<RespuestaPregunta> respuestas;
    private String comentarioFinal;
}