package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.AlumnoMateriaDTO;
import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlumnoMateriaMapper {

    @Mapping(target = "idAlumno", source = "alumno.idPersona")
    @Mapping(target = "alumnoNombre", source = "alumno.nombre")
    @Mapping(target = "idMateria", source = "materia.idMateria")
    @Mapping(target = "materiaNombre", source = "materia.nombre")
    AlumnoMateriaDTO toDTO(AlumnoMateria alumnoMateria);

    List<AlumnoMateriaDTO> toDTOList(List<AlumnoMateria> alumnoMaterias);

    @Mapping(target = "alumno", ignore = true)
    @Mapping(target = "materia", ignore = true)
    void actualizarEntity(AlumnoMateriaDTO alumnoMateriaDTO, @MappingTarget AlumnoMateria alumnoMateria);
}