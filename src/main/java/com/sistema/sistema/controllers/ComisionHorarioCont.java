package com.sistema.sistema.controllers;


import com.sistema.sistema.dto.ComisionHorarioDto;
import com.sistema.sistema.entities.ComisionHorario;
import com.sistema.sistema.services.ComisionHorarioServ;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comisionHorario")
public class ComisionHorarioCont {
    private final ComisionHorarioServ serv;

    public ComisionHorarioCont(ComisionHorarioServ serv) {
        this.serv = serv;
    }

    @PostMapping("/agregar")
    public ComisionHorarioDto agregar (@PathVariable ComisionHorario comHor){
        return serv.agregar(comHor);
    }

    @GetMapping("/mostrar")
    public List<ComisionHorarioDto> mostrar(){
        return serv.mostrar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComisionHorarioDto> modificar (@PathVariable Long id, @RequestBody ComisionHorarioDto comisionHorarioDto){
        return serv.modificar(id, comisionHorarioDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComisionHorarioDto> buscarPorId(@PathVariable Long id){
        return serv.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        serv.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
