package com.sistema.sistema.services;

import com.sistema.sistema.Exceptions.CuotaInvalidaException;
import com.sistema.sistema.dto.PagoCuotaDTO;
import com.sistema.sistema.entities.areaAdministrativa.Cuota;
import com.sistema.sistema.entities.areaAdministrativa.PagoCuota;
import com.sistema.sistema.enums.EstadoCuota;
import com.sistema.sistema.mappers.PagoCuotaMapper;
import com.sistema.sistema.repositories.CuotaRepository;
import com.sistema.sistema.repositories.PagoCuotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoCuotaService {

    private final PagoCuotaRepository pagoRepo;
    private final CuotaRepository cuotaRepo;
    private final PagoCuotaMapper mapper;


    public PagoCuotaDTO pagarCuota(PagoCuotaDTO dto)
    {

        Cuota cuota = cuotaRepo.findById(dto.getIdCuota()) .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));

        if (cuota.getEstadoCuota() == EstadoCuota.PAGADA)
        {
            throw new CuotaInvalidaException("La cuota ya esta pagada");
        }
        PagoCuota pago = mapper.toEntity(dto);
        pago.setCuota(cuota);
        pago.setFechaPago(LocalDate.now());
        PagoCuota guardado = pagoRepo.save(pago);

        cuota.setEstadoCuota(EstadoCuota.PAGADA);
        cuotaRepo.save(cuota);

        return mapper.toDTO(guardado);

    }

    public List<PagoCuotaDTO> obtenerPagosPorAlumno(Long idAlumno) {
        return pagoRepo.findByCuota_Alumno_IdPersona(idAlumno)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }



}
