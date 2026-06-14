package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.CorrelativaDTO;
import com.sistema.sistema.entities.areaAcademica.Correlatividad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface CorrelatividadMapper {

    @Mapping(target = "idMateria", source = "materia.idMateria")
    @Mapping(target = "idMateriaCorrelativa", source = "materiaCorrelativa.idMateria")
    CorrelativaDTO toDTO(Correlatividad correlatividad);

    List<CorrelativaDTO> toDTOList(List<Correlatividad> correlatividads);
}
