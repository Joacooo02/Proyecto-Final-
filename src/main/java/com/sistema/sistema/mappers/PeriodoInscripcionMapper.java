package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.PeriodoInscripcionDTO;
import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PeriodoInscripcionMapper {
    PeriodoInscripcionDTO toDTO(PeriodoInscripcion periodoInscripcion);

    @Mapping(target = "idCarrera", ignore = true)
    PeriodoInscripcion toEntity(PeriodoInscripcionDTO periodoInscripcionDTO);

    List<PeriodoInscripcionDTO> toDTOList(List<PeriodoInscripcion> periodosDeInscripcion);

    @Mapping(target = "idCarrera", ignore = true)
    void actualizarEntity(PeriodoInscripcionDTO periodoInscripcionDTO, @MappingTarget PeriodoInscripcion periodoInscripcion);
}
