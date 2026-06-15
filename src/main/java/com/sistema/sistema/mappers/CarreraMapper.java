package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.CarreraDTO;
import com.sistema.sistema.entities.areaAcademica.Carrera;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarreraMapper {

    CarreraDTO toDTO(Carrera carrera);

    @Mapping(target = "idCarrera", ignore = true)
    Carrera toEntity(CarreraDTO carreraDTO);

    List<CarreraDTO> toDTOList(List<Carrera> carreras);

    @Mapping(target = "idCarrera", ignore = true)
    void actualizarEntity(CarreraDTO carreraDTO, @MappingTarget Carrera carrera);
}