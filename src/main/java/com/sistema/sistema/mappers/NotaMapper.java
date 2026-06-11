package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.NotaDTO;
import com.sistema.sistema.entities.areaAcademica.Nota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotaMapper {

    NotaDTO toDTO(Nota nota);

    @Mapping(target = "examen", ignore = true)
    @Mapping(target = "alumno", ignore = true)
    Nota toEntity(NotaDTO notaDTO);

    List<NotaDTO> toDTOList(List<Nota> notas);

    @Mapping(target = "examen", ignore = true)
    @Mapping(target = "alumno", ignore = true)
    void actualizarEntity(NotaDTO notaDTO, @MappingTarget Nota nota);
}
