package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.PagoCuotaDTO;
import com.sistema.sistema.services.PagoCuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos-cuota")
@RequiredArgsConstructor
public class PagoCuotaController {

    private final PagoCuotaService service;

    @PostMapping("/pagar")
    public PagoCuotaDTO pagarCuota(@RequestBody PagoCuotaDTO dto) {
        return service.pagarCuota(dto);
    }

    @GetMapping("/alumno/{idAlumno}")
    public List<PagoCuotaDTO> obtenerPagos(@PathVariable Long idAlumno) {
        return service.obtenerPagosPorAlumno(idAlumno);
    }



}
