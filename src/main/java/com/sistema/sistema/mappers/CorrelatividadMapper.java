package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.CorrelatividadDTO;
import com.sistema.sistema.entities.areaAcademica.Correlatividad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface CorrelatividadMapper {

    @Mapping(target = "idMateria", source = "materia.idMateria")
    @Mapping(target = "idMateriaCorrelativa", source = "materiaCorrelativa.idMateria")
    CorrelatividadDTO toDTO(Correlatividad correlatividad);

    List<CorrelatividadDTO> toDTOList(List<Correlatividad> correlatividads);
}
