package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.entities.usuario.Alumno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlumnoMapper {

    AlumnoDTO toDTO(Alumno alumno);

    @Mapping(target = "idPersona", ignore = true)
    @Mapping(target = "rolUsuario", ignore = true)
    @Mapping(target = "inscripcionesMateria", ignore = true)
    Alumno toEntity(AlumnoDTO alumnoDTO);

    List<AlumnoDTO> toDTOList(List<Alumno> alumnos);

    @Mapping(target = "idPersona", ignore = true)
    @Mapping(target = "rolUsuario", ignore = true)
    @Mapping(target = "inscripcionesMateria", ignore = true)
    void actualizarEntity(AlumnoDTO alumnoDTO, @MappingTarget Alumno alumno);
}