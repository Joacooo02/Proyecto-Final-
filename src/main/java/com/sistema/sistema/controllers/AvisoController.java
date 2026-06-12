package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.AvisoDTO;
import com.sistema.sistema.services.AvisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avisos")
@RequiredArgsConstructor
public class AvisoController {

    private final AvisoService service;

    @PostMapping("/persona/{idPersona}")
    public ResponseEntity<AvisoDTO> crearAviso(@PathVariable Long idPersona, @RequestBody AvisoDTO avisoDTO) {
        return ResponseEntity.ok(service.crearAviso(idPersona, avisoDTO));
    }

    @GetMapping
    public ResponseEntity<List<AvisoDTO>> listarAvisos() {

        return ResponseEntity.ok(
                service.listarAvisoDto()
        );
    }

    @GetMapping("/{idAviso}")
    public ResponseEntity<AvisoDTO> verAvisoPorId(@PathVariable Long idAviso) {
        return ResponseEntity.ok(service.verAvisoPorId(idAviso));
    }

    @DeleteMapping("{idAviso}/{idPersona}")
    public ResponseEntity<String> eliminarAvisoPorId(@PathVariable Long idPersona, @PathVariable Long idAviso)
    {
        service.eliminarAvisoPorId(idPersona, idAviso);
        return ResponseEntity.ok("Aviso eliminado correctamente");
    }

}
