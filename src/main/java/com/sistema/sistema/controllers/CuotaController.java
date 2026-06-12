package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.CuotaDTO;
import com.sistema.sistema.entities.areaAdministrativa.Cuota;
import com.sistema.sistema.enums.EstadoCuota;
import com.sistema.sistema.services.CuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuotas")
@RequiredArgsConstructor
public class CuotaController {

    private final CuotaService cuotaService;

    @PostMapping
    public String generarCuota(@RequestBody CuotaDTO dto)
    {
        return cuotaService.generarCuotaAutomatica(dto);
    }


    @GetMapping("/alumno/{id}")
    public List<CuotaDTO> listarPorAlumno(@PathVariable Long id)
    {
        return cuotaService.listarPorAlumno(id);
    }

    @GetMapping("/deuda/{id}")
    public Integer deuda(@PathVariable Long id)
    {
        return cuotaService.calcularDeuda(id);
    }

    @GetMapping("/estado")
    public List<CuotaDTO> listarPorEstado(@RequestParam EstadoCuota estado) {
        return cuotaService.listarPorEstado(estado);
    }


}
