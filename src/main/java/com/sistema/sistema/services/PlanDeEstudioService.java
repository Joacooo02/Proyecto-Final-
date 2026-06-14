package com.sistema.sistema.services;

import com.sistema.sistema.mappers.PlanDeEstudioMapper;
import com.sistema.sistema.repositories.PlanDeEstudioRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanDeEstudioService {
    private final PlanDeEstudioRepository planDeEstudioRepository;
    private final PlanDeEstudioMapper planDeEstudioMapper;

    public PlanDeEstudioService(PlanDeEstudioRepository planDeEstudioRepository, PlanDeEstudioMapper planDeEstudioMapper) {
        this.planDeEstudioRepository = planDeEstudioRepository;
        this.planDeEstudioMapper = planDeEstudioMapper;
    }
}
