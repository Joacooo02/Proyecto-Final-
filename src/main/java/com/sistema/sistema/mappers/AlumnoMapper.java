package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.entities.usuario.Alumno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlumnoMapper {

    @Mapping(target = "fechaNacimiento", source = "fechaNacimiento")
    @Mapping(target = "anioIngreso", source = "anioIngreso")
    AlumnoDTO toDTO(Alumno alumno);

    @Mapping(target = "idPersona", ignore = true)
    @Mapping(target = "rolUsuario", ignore = true)
    @Mapping(target = "inscripcionesMateria", ignore = true)
    @Mapping(target = "fechaNacimiento", source = "fechaNacimiento")
    @Mapping(target = "anioIngreso", source = "anioIngreso")
    Alumno toEntity(AlumnoDTO alumnoDTO);

    List<AlumnoDTO> toDTOList(List<Alumno> alumnos);
    @Mapping(target = "idPersona", ignore = true)
    @Mapping(target = "rolUsuario", ignore = true)
    @Mapping(target = "inscripcionesMateria", ignore = true)
    @Mapping(target = "fechaNacimiento", source = "fechaNacimiento")
    @Mapping(target = "anioIngreso", source = "anioIngreso")
    void actualizarEntity(AlumnoDTO alumnoDTO, @MappingTarget Alumno alumno);
}