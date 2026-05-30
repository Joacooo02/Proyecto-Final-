package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.usuario.Administrador;
import com.sistema.sistema.services.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    @GetMapping("/{id}")
    public Administrador buscarAdministradorPorId(@PathVariable Long id) {
        return administradorService.buscarAdministradorPorId(id);
    }

    @PostMapping
    public Administrador agregarAdministrador(@RequestBody Administrador administrador) {
        return administradorService.agregarAdministrador(administrador);
    }

    @DeleteMapping("/{id}")
    public void eliminarAdministrador(@PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
    }

    @GetMapping
    public List<Administrador> listarAdministradores() {
        return administradorService.listarAdministradores();
    }

    @PutMapping("/{id}")
    public Administrador modificarAdministrador(@PathVariable Long id, @RequestBody Administrador administradorModificado) {
        return administradorService.modificarAdministrador(id, administradorModificado);
    }
}
