package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.ComisionHorarioDTO;
import com.sistema.sistema.entities.areaAcademica.ComisionHorario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComisionHorarioMapper {
    ComisionHorarioDTO toDTO(ComisionHorario comisionHorario);
    ComisionHorario toEntity(ComisionHorarioDTO comisionHorarioDTO);
    List<ComisionHorarioDTO> toDTOList(List<ComisionHorario> comisionHorario);
    void actualizarEntity(ComisionHorarioDTO comisionHorarioDto, @MappingTarget ComisionHorario comisionHorario);
}
