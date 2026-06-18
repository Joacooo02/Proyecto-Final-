package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.PagoCuotaDTO;
import com.sistema.sistema.services.PagoCuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos-cuota")
@RequiredArgsConstructor
public class PagoCuotaController {

    private final PagoCuotaService service;

    @PreAuthorize("hasAnyRole('ALUMNO')")
    @PostMapping("/pagar")
    public PagoCuotaDTO pagarCuota(@RequestBody PagoCuotaDTO dto) {
        return service.pagarCuota(dto);
    }

    @PreAuthorize("hasAnyRole('ALUMNO')")
    @GetMapping("/alumno/{idAlumno}")
    public List<PagoCuotaDTO> obtenerPagos(@PathVariable Long idAlumno) {
        return service.obtenerPagosPorAlumno(idAlumno);
    }



}
