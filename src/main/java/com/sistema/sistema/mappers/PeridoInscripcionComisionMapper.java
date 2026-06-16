package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.PeriodoInscripcionComisionDTO;
import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcionComision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface PeridoInscripcionComisionMapper {
    @Mapping(target = "idPeriodo", source = "periodo.idPeriodo")
    @Mapping(target = "idComision", source = "comision.idComision")
    @Mapping(target = "materia", source = "comision.materia.nombre")
    @Mapping(target = "nroComision", source = "comision.nroComision")
    PeriodoInscripcionComisionDTO toDTO(PeriodoInscripcionComision entity);

    List<PeriodoInscripcionComisionDTO> toDTOList(List<PeriodoInscripcionComision> entities);

}
