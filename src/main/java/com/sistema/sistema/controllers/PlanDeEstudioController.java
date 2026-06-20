package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.PlanDeEstudioDTO;
import com.sistema.sistema.entities.areaAcademica.PlanDeEstudio;
import com.sistema.sistema.services.PlanDeEstudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planDeEstudio")
public class PlanDeEstudioController {
    private final PlanDeEstudioService planDeEstudioService;

    public PlanDeEstudioController(PlanDeEstudioService planDeEstudioService) {
        this.planDeEstudioService = planDeEstudioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/crear")
    public PlanDeEstudioDTO crear(@RequestBody PlanDeEstudio planDeEstudio) {
        return planDeEstudioService.crear(planDeEstudio);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planDeEstudioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PlanDeEstudioDTO> modificar(@PathVariable Long id, @RequestBody PlanDeEstudioDTO planDeEstudioDTO) {
        return planDeEstudioService.modificar(id, planDeEstudioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'ALUMNO')")
    @GetMapping("/mostrar")
    public List<PlanDeEstudioDTO> mostrar() {
        return planDeEstudioService.mostrar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanDeEstudioDTO> buscarPorId(@PathVariable Long id) {
        return planDeEstudioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
