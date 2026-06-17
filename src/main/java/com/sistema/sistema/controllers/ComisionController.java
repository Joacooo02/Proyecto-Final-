package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.dto.ComisionDTO;
import com.sistema.sistema.services.ComisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comisiones")
@RequiredArgsConstructor
public class ComisionController {

    private final ComisionService comisionService;

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public Comision buscarComisionPorId(@PathVariable Long id) {
        return comisionService.buscarComisionPorId(id);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public Comision agregarComision(@RequestBody Comision comision) {

        if(comision.getCantAlumnos() == null)
        {
            comision.setCantAlumnos(0);
        }

        return comisionService.agregarComision(comision);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminarComision(@PathVariable Long id) {
        comisionService.eliminarComision(id);
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping
    public List<Comision> listarComisiones(@RequestParam(required = false) Integer nroComision) {
        return comisionService.listarComisiones(nroComision);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public Comision modificarComision(@PathVariable Long id, @RequestBody Comision comisionModificada) {
        return comisionService.modificarComision(id, comisionModificada);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<ComisionDTO>> obtenerComisionesPorProfesor(@PathVariable Long profesorId) {
        List<ComisionDTO> comisiones = comisionService.obtenerComisionesPorProfesor(profesorId);
        return ResponseEntity.ok(comisiones);
    }
}

