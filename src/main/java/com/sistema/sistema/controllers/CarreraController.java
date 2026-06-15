package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.CarreraDTO;
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
    public CarreraDTO buscarCarreraPorId(@PathVariable Long id) {
        return carreraService.buscarCarreraPorId(id);
    }

    @PostMapping
    public CarreraDTO agregarCarrera(@RequestBody CarreraDTO carrera) {
        return carreraService.agregarCarrera(carrera);
    }

    @DeleteMapping("/{id}")
    public void eliminarCarrera(@PathVariable Long id) {
        carreraService.eliminarCarrera(id);
    }

    @GetMapping
    public List<CarreraDTO> listarCarreras() {
        return carreraService.listarCarreras();
    }

    @PutMapping("/{id}")
    public CarreraDTO modificarCarrera(@PathVariable Long id, @RequestBody CarreraDTO carreraModificada) {
        return carreraService.modificarCarrera(id, carreraModificada);
    }
}