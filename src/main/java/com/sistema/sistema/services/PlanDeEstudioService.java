package com.sistema.sistema.services;

import com.sistema.sistema.dto.PlanDeEstudioDTO;
import com.sistema.sistema.entities.areaAcademica.PlanDeEstudio;
import com.sistema.sistema.mappers.PlanDeEstudioMapper;
import com.sistema.sistema.repositories.PlanDeEstudioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanDeEstudioService {
    private final PlanDeEstudioRepository planDeEstudioRepository;
    private final PlanDeEstudioMapper planDeEstudioMapper;

    public PlanDeEstudioService(PlanDeEstudioRepository planDeEstudioRepository, PlanDeEstudioMapper planDeEstudioMapper) {
        this.planDeEstudioRepository = planDeEstudioRepository;
        this.planDeEstudioMapper = planDeEstudioMapper;
    }

    public PlanDeEstudioDTO crear(PlanDeEstudio planDeEstudio) {
        return planDeEstudioMapper.toDTO(planDeEstudioRepository.save(planDeEstudio));
    }

    public void eliminar(Long id) {
        planDeEstudioRepository.deleteById(id);
    }

    public Optional<PlanDeEstudioDTO> modificar(Long id, PlanDeEstudioDTO planDeEstudioDTO) {
        return planDeEstudioRepository.findById(id)
                .map(planDeEstudio -> {
                    planDeEstudio.setIdCarrera(planDeEstudioDTO.getIdCarrera());
                    planDeEstudio.setAnioInicio(planDeEstudioDTO.getAnioInicio());
                    return planDeEstudioMapper.toDTO(planDeEstudioRepository.save(planDeEstudio));
                });
    }

    public List<PlanDeEstudioDTO> mostrar() {
        return planDeEstudioRepository.findAll().stream()
                .map(planDeEstudioMapper::toDTO)
                .toList();
    }

    public Optional<PlanDeEstudioDTO> buscarPorId(Long id) {
        return planDeEstudioRepository.findById(id)
                .map(planDeEstudioMapper::toDTO);
    }
}
