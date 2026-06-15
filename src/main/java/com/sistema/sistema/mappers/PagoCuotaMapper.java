package com.sistema.sistema.mappers;

import com.sistema.sistema.dto.PagoCuotaDTO;
import com.sistema.sistema.entities.areaAdministrativa.PagoCuota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoCuotaMapper {

    @Mapping(target = "idCuota", source = "cuota.idCuota")
    PagoCuotaDTO toDTO(PagoCuota pagoCuota);

    @Mapping(target = "cuota", ignore = true)
    @Mapping(target = "idPagoCuota", ignore = true)
    PagoCuota toEntity(PagoCuotaDTO dto);

    List<PagoCuotaDTO> toDTOList(List<PagoCuota> list);

    @Mapping(target = "cuota", ignore = true)
    @Mapping(target = "idPagoCuota", ignore = true)
    void actualizarEntity(PagoCuotaDTO dto, @MappingTarget PagoCuota entity);
}
