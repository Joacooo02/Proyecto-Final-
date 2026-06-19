package com.sistema.sistema.dto;

import com.sistema.sistema.enums.ModalidadCarrera;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarreraDTO {

    private Long idCarrera;
    private String nombre;
    private Integer duracion;
    private String tituloOtorgado;
    private ModalidadCarrera modalidadCarrera;
}