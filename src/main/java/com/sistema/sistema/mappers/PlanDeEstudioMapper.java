package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.PlanDeEstudioDTO;
import com.sistema.sistema.entities.areaAcademica.PlanDeEstudio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanDeEstudioMapper {
    PlanDeEstudioDTO toDTO(PlanDeEstudio planDeEstudio);

    @Mapping(target = "idCarrera", ignore = true)
    PlanDeEstudio toEntity(PlanDeEstudioDTO planDeEstudioDTO);

    List<PlanDeEstudioDTO> toDTOList(List<PlanDeEstudio> planesDeEstudio);

    @Mapping(target = "idCarrera", ignore = true)
    void actualizarEntity(PlanDeEstudioDTO planDeEstudioDTO, @MappingTarget PlanDeEstudio planDeEstudio);
}
