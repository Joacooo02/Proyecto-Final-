package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.AvisoDTO;
import com.sistema.sistema.services.AvisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avisos")
@RequiredArgsConstructor
public class AvisoController {

    private final AvisoService service;

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @PostMapping("/persona/{idPersona}")
    public ResponseEntity<AvisoDTO> crearAviso(@PathVariable Long idPersona, @RequestBody AvisoDTO avisoDTO) {
        return ResponseEntity.ok(service.crearAviso(idPersona, avisoDTO));
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<AvisoDTO>> listarAvisos() {

        return ResponseEntity.ok(
                service.listarAvisoDto()
        );
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'ADMIN')")
    @GetMapping("/{idAviso}")
    public ResponseEntity<AvisoDTO> verAvisoPorId(@PathVariable Long idAviso) {
        return ResponseEntity.ok(service.verAvisoPorId(idAviso));
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @DeleteMapping("{idAviso}/{idPersona}")
    public ResponseEntity<String> eliminarAvisoPorId(@PathVariable Long idPersona, @PathVariable Long idAviso)
    {
        service.eliminarAvisoPorId(idPersona, idAviso);
        return ResponseEntity.ok("Aviso eliminado correctamente");
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @PutMapping("/{idAviso}/persona/{idPersona}")
    public ResponseEntity<AvisoDTO> modificarAviso(@PathVariable Long idAviso,@PathVariable Long idPersona,@RequestBody AvisoDTO dto)
    {
        return ResponseEntity.ok(service.modificarAviso(idAviso, idPersona, dto));
    }
}
