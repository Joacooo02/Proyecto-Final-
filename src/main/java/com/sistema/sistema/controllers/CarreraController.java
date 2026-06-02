package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Carrera;
import com.sistema.sistema.services.CarreraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService carreraService;

    @GetMapping("/{id}")
    public Carrera buscarCarreraPorId(@PathVariable Long id) {
        return carreraService.buscarCarreraPorId(id);
    }

    @PostMapping
    public Carrera agregarCarrera(@RequestBody Carrera carrera) {
        return carreraService.agregarCarrera(carrera);
    }

    @DeleteMapping("/{id}")
    public void eliminarCarrera(@PathVariable Long id) {
        carreraService.eliminarCarrera(id);
    }

    @GetMapping
    public List<Carrera> listarCarreras(@RequestParam(required = false) String nombre) {
        return carreraService.listarCarreras(nombre);
    }

    @PutMapping("/{id}")
    public Carrera modificarCarrera(@PathVariable Long id, @RequestBody Carrera carreraModificada) {
        return carreraService.modificarCarrera(id, carreraModificada);
    }
}
