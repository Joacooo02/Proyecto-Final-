package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.services.ComisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comisiones")
@RequiredArgsConstructor
public class ComisionController {

    private final ComisionService comisionService;

    @GetMapping("/{id}")
    public Comision buscarComisionPorId(@PathVariable Long id) {
        return comisionService.buscarComisionPorId(id);
    }

    @PostMapping
    public Comision agregarComision(@RequestBody Comision comision) {
        return comisionService.agregarComision(comision);
    }

    @DeleteMapping("/{id}")
    public void eliminarComision(@PathVariable Long id) {
        comisionService.eliminarComision(id);
    }

    @GetMapping
    public List<Comision> listarComisiones() {
        return comisionService.listarComisiones();
    }

    @PutMapping("/{id}")
    public Comision modificarComision(@PathVariable Long id, @RequestBody Comision comisionModificada) {
        return comisionService.modificarComision(id, comisionModificada);
    }
}
