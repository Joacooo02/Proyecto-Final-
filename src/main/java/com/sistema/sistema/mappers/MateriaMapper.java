package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.entities.areaAcademica.Materia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MateriaMapper {


    @Mapping(target = "idCarrera", source = "carrera.idCarrera")
    @Mapping(target = "id", source = "idMateria")
    MateriaDTO toDTO(Materia materia);

    @Mapping(target = "idMateria", ignore = true)
    @Mapping(target = "carrera", ignore = true)
    Materia toEntity(MateriaDTO materiaDTO);

    List<MateriaDTO> toDTOList(List<Materia> materias);

    @Mapping(target = "idMateria", ignore = true)
    @Mapping(target = "carrera", ignore = true)
    void actualizarEntity(MateriaDTO materiaDTO, @MappingTarget Materia materia);
}
