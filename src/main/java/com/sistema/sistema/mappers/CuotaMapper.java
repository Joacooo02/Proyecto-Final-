package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.CuotaDTO;
import com.sistema.sistema.entities.areaAdministrativa.Cuota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CuotaMapper {

    @Mapping(source = "idCuota", target = "id")
    @Mapping(source = "alumno.idPersona", target = "idAlumno")
    CuotaDTO toDto(Cuota cuota);

    @Mapping(target = "alumno", ignore = true)
    Cuota toEntity(CuotaDTO dto);

    List<CuotaDTO> toDTOList(List<Cuota> cuotas);
}
