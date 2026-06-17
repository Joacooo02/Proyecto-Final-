package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.PeriodoInscripcionDTO;
import com.sistema.sistema.dto.PlanDeEstudioDTO;
import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcion;
import com.sistema.sistema.entities.areaAcademica.PlanDeEstudio;
import com.sistema.sistema.mappers.PeriodoInscripcionMapper;
import com.sistema.sistema.repositories.PeriodoInscripcionRepository;
import com.sistema.sistema.services.PeriodoInscripcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periodo/inscripcion")
public class PeriodoInscripcionController {
    private final PeriodoInscripcionService periodoInscripcionService;

    public PeriodoInscripcionController(PeriodoInscripcionService periodoInscripcionService) {
        this.periodoInscripcionService = periodoInscripcionService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/crear")
    public PeriodoInscripcionDTO crear(@RequestBody PeriodoInscripcion periodoInscripcion) {
        return periodoInscripcionService.crear(periodoInscripcion);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        periodoInscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PeriodoInscripcionDTO> modificar(@PathVariable Long id, @RequestBody PeriodoInscripcionDTO periodoInscripcionDTO) {
        return periodoInscripcionService.modificar(id, periodoInscripcionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/mostrar")
    public List<PeriodoInscripcionDTO> mostrar() {
        return periodoInscripcionService.mostrar();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PeriodoInscripcionDTO> buscarPorId(@PathVariable Long id) {
        return periodoInscripcionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
