package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.dto.NotaDTO;
import com.sistema.sistema.services.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notas")
@RequiredArgsConstructor

public class NotaController {
    private final NotaService serv;

    @PostMapping
    public Nota registrarNota(@RequestBody NotaDTO dto){
        return serv.registrarNota(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaDTO> modificar(@PathVariable Long id, @RequestBody NotaDTO dto) {
        return serv.modificar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
