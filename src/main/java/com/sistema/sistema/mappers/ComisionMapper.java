package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.ComisionDTO;
import com.sistema.sistema.entities.areaAcademica.Comision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComisionMapper {

    ComisionDTO toDTO(Comision comision);

    @Mapping(target = "materia", ignore = true)
    @Mapping(target = "profesor", ignore = true)
    Comision toEntity(ComisionDTO comisionDTO);

    List<ComisionDTO> toDTOList(List<Comision> comisiones);

    @Mapping(target = "materia", ignore = true)
    @Mapping(target = "profesor", ignore = true)
    void actualizarEntity(ComisionDTO comisionDTO, @MappingTarget Comision comision);
}
