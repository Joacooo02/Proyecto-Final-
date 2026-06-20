package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.CuotaDTO;
import com.sistema.sistema.entities.areaAdministrativa.Cuota;
import com.sistema.sistema.enums.EstadoCuota;
import com.sistema.sistema.services.CuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuotas")
@RequiredArgsConstructor
public class CuotaController {

    private final CuotaService cuotaService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public String generarCuota(@RequestBody CuotaDTO dto)
    {
        return cuotaService.generarCuotaAutomatica(dto);
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/alumno/{id}")
    public List<CuotaDTO> listarPorAlumno(@PathVariable Long id)
    {
        return cuotaService.listarPorAlumno(id);
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/deuda/{id}")
    public Integer deuda(@PathVariable Long id)
    {
        return cuotaService.calcularDeuda(id);
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/estado")
    public List<CuotaDTO> listarPorEstado(@RequestParam EstadoCuota estado) {
        return cuotaService.listarPorEstado(estado);
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/deuda/total/{idAlumno}")
    public Integer obtenerDeudaTotal(@PathVariable Long idAlumno) {
        return cuotaService.obtenerDeudaTotal(idAlumno);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{idCuota}")
    public ResponseEntity<CuotaDTO> modificarCuota(@PathVariable Long idCuota, @RequestBody CuotaDTO dto)
    {
        return ResponseEntity.ok(cuotaService.modificarCuota(idCuota, dto));
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{idCuota}")
    public ResponseEntity<String> eliminarCuota(@PathVariable Long idCuota)
    {
        cuotaService.eliminarCuota(idCuota);
        return ResponseEntity.ok("Cuota eliminada correctamente");
    }


}
