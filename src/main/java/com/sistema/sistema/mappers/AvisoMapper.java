package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.AvisoDTO;
import com.sistema.sistema.entities.areaAdministrativa.Aviso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface AvisoMapper {

    @Mapping(source = "persona.nombre", target = "nombre")
    @Mapping(source = "persona.apellido", target = "apellido")
    AvisoDTO toDTO(Aviso aviso);

    List<AvisoDTO> toDTO(List<Aviso>avisos);


}
