package com.sistema.sistema.services;

import com.sistema.sistema.dto.CuotaDTO;
import com.sistema.sistema.entities.areaAdministrativa.Cuota;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.EstadoCuota;
import com.sistema.sistema.exceptions.CuotaInvalidaException;
import com.sistema.sistema.mappers.CuotaMapper;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.CuotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuotaService {

    private final CuotaRepository cuotaRepository;
    private final CuotaMapper cuotaMapper;
    private final AlumnoRepository alumnoRepository;

    public String generarCuotaAutomatica(CuotaDTO dto)
    {
        Alumno alumno = alumnoRepository.findById(dto.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("No se encontró el alumno"));

        boolean existe = cuotaRepository
                .existsByAlumnoIdPersonaAndMesAndAnio(
                        dto.getIdAlumno(),
                        dto.getMes(),
                        dto.getAnio()
                );

        if (existe) {
            return "Ya existe una cuota para ese mes y año";
        }

        Cuota cuota = Cuota.builder()
                .alumno(alumno)
                .mes(dto.getMes())
                .anio(dto.getAnio())
                .valorCuota(dto.getValorCuota())
                .fechaVencimiento(dto.getFechaVencimiento() != null
                        ? dto.getFechaVencimiento()
                        : LocalDate.of(dto.getAnio(), dto.getMes(), 10))
                .conceptoCuota(dto.getConceptoCuota())
                .estadoCuota(dto.getEstadoCuota() != null
                        ? dto.getEstadoCuota()
                        : EstadoCuota.PENDIENTE)
                .build();

        cuotaRepository.save(cuota);
        return "Cuota generada correctamente";
    }

    public List<CuotaDTO> listarPorAlumno(Long idAlumno)
    {
        return cuotaMapper.toDTOList(cuotaRepository.findByAlumnoIdPersona(idAlumno));
    }

    public Integer calcularDeuda(Long idAlumno)
    {
        return cuotaRepository.findByAlumnoIdPersona(idAlumno)
                .stream()
                .filter(c -> c.getEstadoCuota() != EstadoCuota.PAGADA)
                .mapToInt(Cuota::getValorCuota)
                .sum();
    }


    public List<CuotaDTO> listarPorEstado(EstadoCuota estadoCuota) {
        return cuotaMapper.toDTOList(
                cuotaRepository.findByEstadoCuota(estadoCuota)
        );
    }

    public Integer obtenerDeudaTotal(Long idAlumno)
    {
        List<Cuota> cuotasDeuda = cuotaRepository.findByAlumno_IdPersonaAndEstadoCuotaIn(idAlumno,List.of(EstadoCuota.PENDIENTE,EstadoCuota.VENCIDA));
        return cuotasDeuda.stream()
                .mapToInt(Cuota::getValorCuota)
                .sum();
    }


    public CuotaDTO modificarCuota(Long idCuota, CuotaDTO dto)
    {
        Cuota cuota = cuotaRepository.findById(idCuota).orElseThrow(() -> new RuntimeException("No se encontró la cuota"));

        cuota.setValorCuota(dto.getValorCuota());
        cuota.setEstadoCuota(dto.getEstadoCuota());
        cuota.setConceptoCuota(dto.getConceptoCuota());
        cuota.setFechaVencimiento(dto.getFechaVencimiento());

        cuota = cuotaRepository.save(cuota);
        return cuotaMapper.toDto(cuota);
    }


    public void eliminarCuota(Long idCuota)
    {
        Cuota cuota = cuotaRepository.findById(idCuota).orElseThrow(() -> new CuotaInvalidaException("No se encontro la cuota"));
        cuotaRepository.deleteById(idCuota);
    }

}
