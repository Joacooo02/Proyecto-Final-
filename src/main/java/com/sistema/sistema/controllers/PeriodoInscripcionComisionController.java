package com.sistema.sistema.controllers;
import com.sistema.sistema.dto.PeriodoInscripcionComisionDTO;
import com.sistema.sistema.services.PeriodoInscripcionComisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periodos")
@RequiredArgsConstructor
public class PeriodoInscripcionComisionController {

    private final PeriodoInscripcionComisionService service;

    @PostMapping("/{idPeriodo}/comisiones/{idComision}")
    public PeriodoInscripcionComisionDTO habilitarComision(@PathVariable Long idPeriodo, @PathVariable Long idComision)
    {
        return service.habilitarComision(idPeriodo, idComision);
    }

    @GetMapping("/{idPeriodo}/comisiones")
    public List<PeriodoInscripcionComisionDTO> listarComisionesHabilitadas(@PathVariable Long idPeriodo)
    {
        return service.listarComisionesHabilitadas(idPeriodo);
    }
}
