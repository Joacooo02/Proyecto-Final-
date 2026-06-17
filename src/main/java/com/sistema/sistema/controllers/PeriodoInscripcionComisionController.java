package com.sistema.sistema.controllers;
import com.sistema.sistema.dto.PeriodoInscripcionComisionDTO;
import com.sistema.sistema.services.PeriodoInscripcionComisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periodos")
@RequiredArgsConstructor
public class PeriodoInscripcionComisionController {

    private final PeriodoInscripcionComisionService service;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{idPeriodo}/comisiones/{idComision}")
    public PeriodoInscripcionComisionDTO habilitarComision(@PathVariable Long idPeriodo, @PathVariable Long idComision)
    {
        return service.habilitarComision(idPeriodo, idComision);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{idPeriodo}/comisiones")
    public List<PeriodoInscripcionComisionDTO> listarComisionesHabilitadas(@PathVariable Long idPeriodo)
    {
        return service.listarComisionesHabilitadas(idPeriodo);
    }
}
