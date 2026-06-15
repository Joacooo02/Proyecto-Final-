package com.sistema.sistema.services;

import com.sistema.sistema.dto.PeriodoInscripcionDTO;
import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcion;
import com.sistema.sistema.mappers.PeriodoInscripcionMapper;
import com.sistema.sistema.repositories.PeriodoInscripcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeriodoInscripcionService {
    private final PeriodoInscripcionRepository periodoInscripcionRepository;
    private final PeriodoInscripcionMapper periodoInscripcionMapper;

    public PeriodoInscripcionService(PeriodoInscripcionRepository periodoInscripcionRepository, PeriodoInscripcionMapper periodoInscripcionMapper) {
        this.periodoInscripcionRepository = periodoInscripcionRepository;
        this.periodoInscripcionMapper = periodoInscripcionMapper;
    }

    public PeriodoInscripcionDTO crear(PeriodoInscripcion periodoInscripcion) {
        return periodoInscripcionMapper.toDTO(periodoInscripcionRepository.save(periodoInscripcion));
    }

    public void eliminar(Long id) {
        periodoInscripcionRepository.deleteById(id);
    }

    public Optional<PeriodoInscripcionDTO> modificar(Long id, PeriodoInscripcionDTO periodoInscripcionDTO) {
        return periodoInscripcionRepository.findById(id)
                .map(periodoInscripcion -> {
                    periodoInscripcion.setIdCarrera(periodoInscripcionDTO.getIdCarrera());
                    periodoInscripcion.setTipoInscripcion(periodoInscripcionDTO.getTipoInscripcion());
                    periodoInscripcion.setFechaInicio(periodoInscripcionDTO.getFechaInicio().atStartOfDay());
                    periodoInscripcion.setFechaCierre(periodoInscripcionDTO.getFechaCierre().atStartOfDay());
                    periodoInscripcion.setAnioLectivo(periodoInscripcionDTO.getAnioLectivo());
                    periodoInscripcion.setCuatrimestre(periodoInscripcionDTO.getCuatrimestre());
                    periodoInscripcion.setActiva(periodoInscripcionDTO.getActiva());
                    return periodoInscripcionMapper.toDTO(periodoInscripcionRepository.save(periodoInscripcion));
                });
    }

    public List<PeriodoInscripcionDTO> mostrar() {
        return periodoInscripcionRepository.findAll().stream()
                .map(periodoInscripcionMapper::toDTO)
                .toList();
    }

    public Optional<PeriodoInscripcionDTO> buscarPorId(Long id) {
        return periodoInscripcionRepository.findById(id)
                .map(periodoInscripcionMapper::toDTO);
    }
}
