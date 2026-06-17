package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.dto.NotaDTO;
import com.sistema.sistema.services.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        serv.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<NotaDTO>> verNotas()
    {
        return ResponseEntity.ok(serv.verNotas());
    }

    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<List<NotaDTO>> verNotasPorAlumno(@PathVariable Long idAlumno)
    {
        return ResponseEntity.ok(serv.verNotasPorAlumno(idAlumno));
    }

}
