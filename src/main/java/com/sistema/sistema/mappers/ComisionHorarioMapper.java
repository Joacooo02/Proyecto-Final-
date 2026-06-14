package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.ComisionHorarioDto;
import com.sistema.sistema.entities.ComisionHorario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComisionHorarioMapper {
    ComisionHorarioDto toDTO(ComisionHorario comisionHorario);
    ComisionHorario toEntity(ComisionHorarioDto comisionHorarioDTO);
    List<ComisionHorarioDto> toDTOList(List<ComisionHorario> comisionHorario);
    void actualizarEntity(ComisionHorarioDto comisionHorarioDto, @MappingTarget ComisionHorario comisionHorario);
}
