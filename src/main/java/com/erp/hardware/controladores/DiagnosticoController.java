package com.erp.hardware.controladores;

import com.erp.hardware.modelos.Diagnosticos;
import com.erp.hardware.servicios.DiagnosticoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diagnosticos")
@CrossOrigin(origins = "*")
public class DiagnosticoController {

    @Autowired
    private DiagnosticoService diagnosticoService;

    @GetMapping("/orden/{idOrden}")
    public List<Diagnosticos> listarPorOrden(@PathVariable Integer idOrden) {
        return diagnosticoService.listarPorOrden(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrarDiagnostico(@RequestBody Diagnosticos diagnostico,
                                                  @RequestParam(required = false) Integer idUsuario) {
        try {
            if (diagnostico.getIdOrden() == null || diagnostico.getIdOrden().getIdOrden() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "La orden de trabajo es requerida para el diagnóstico"));
            }
            if (diagnostico.getDiagnosticoTecnico() == null || diagnostico.getDiagnosticoTecnico().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "El detalle técnico del diagnóstico no puede estar vacío"));
            }

            Integer usuarioActual = idUsuario;
            if (usuarioActual == null && diagnostico.getIdTecnico() != null) {
                usuarioActual = diagnostico.getIdTecnico().getIdUsuario();
            }

            Diagnosticos guardado = diagnosticoService.registrarDiagnostico(
                    diagnostico.getIdOrden().getIdOrden(),
                    diagnostico,
                    usuarioActual
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }
}
