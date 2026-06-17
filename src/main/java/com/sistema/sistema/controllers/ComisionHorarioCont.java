package com.sistema.sistema.controllers;


import com.sistema.sistema.dto.ComisionHorarioDTO;
import com.sistema.sistema.entities.areaAcademica.ComisionHorario;
import com.sistema.sistema.services.ComisionHorarioServ;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comisionHorario")
public class ComisionHorarioCont {
    private final ComisionHorarioServ serv;

    public ComisionHorarioCont(ComisionHorarioServ serv) {
        this.serv = serv;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/agregar")
    public ComisionHorarioDTO agregar (@PathVariable ComisionHorario comHor){
        return serv.agregar(comHor);
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/mostrar")
    public List<ComisionHorarioDTO> mostrar(){
        return serv.mostrar();
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ComisionHorarioDTO> modificar (@PathVariable Long id, @RequestBody ComisionHorarioDTO comisionHorarioDto){
        return serv.modificar(id, comisionHorarioDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ComisionHorarioDTO> buscarPorId(@PathVariable Long id){
        return serv.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //@PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        serv.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
