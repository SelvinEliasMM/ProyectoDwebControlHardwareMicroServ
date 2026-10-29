package com.erp.hardware.controladores;

import com.erp.hardware.modelos.BitacoraAuditoria;
import com.erp.hardware.modelos.HistorialEstados;
import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.repositorios.BitacoraAuditoriaRepository;
import com.erp.hardware.repositorios.HistorialEstadosRepository;
import com.erp.hardware.repositorios.OrdenesTrabajoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
public class AuditoriaController {

    @Autowired
    private HistorialEstadosRepository historialEstadosRepository;

    @Autowired
    private BitacoraAuditoriaRepository bitacoraAuditoriaRepository;

    @Autowired
    private OrdenesTrabajoRepository ordenesRepository;

    // consulta del historial cronológico de cambios de estado de una orden
    @GetMapping("/orden/{idOrden}")
    public List<HistorialEstados> verHistorialOrden(@PathVariable Integer idOrden) {
        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + idOrden));
        return historialEstadosRepository.findByIdOrdenOrderByFechaCambioDesc(orden);
    }

    // consulta de la bitácora general de operaciones
    @GetMapping("/bitacora")
    public List<BitacoraAuditoria> verBitacoraGeneral() {
        return bitacoraAuditoriaRepository.findTop100ByOrderByFechaHoraDesc();
    }
}
