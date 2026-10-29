package com.erp.hardware.servicios;

import com.erp.hardware.modelos.Diagnosticos;
import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.DiagnosticosRepository;
import com.erp.hardware.repositorios.OrdenesTrabajoRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiagnosticoService {

    @Autowired
    private DiagnosticosRepository diagnosticosRepository;

    @Autowired
    private OrdenesTrabajoRepository ordenesRepository;

    public List<Diagnosticos> listarPorOrden(Integer idOrden) {
        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + idOrden));
        return diagnosticosRepository.findByIdOrdenOrderByFechaDiagnosticoDesc(orden);
    }

    @Transactional
    public Diagnosticos registrarDiagnostico(Integer idOrden, Diagnosticos diag, Integer idUsuario) {
        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + idOrden));

        diag.setIdOrden(orden);
        diag.setFechaDiagnostico(new Date());
        diag.setFechaCreacion(new Date());
        diag.setEstadoRegistro(true);

        if (diag.getIdTecnico() == null && idUsuario != null) {
            diag.setIdTecnico(new Usuarios(idUsuario));
        }
        if (idUsuario != null) {
            diag.setUsuarioCreacion(new Usuarios(idUsuario));
        }

        // si la orden estaba en estado 'Recibido', pasarla a 'En Diagnóstico'
        if ("Recibido".equalsIgnoreCase(orden.getEstadoReparacion())) {
            orden.setEstadoReparacion("En Diagnóstico");
            orden.setFechaModificacion(new Date());
            if (idUsuario != null) {
                orden.setUsuarioModificacion(new Usuarios(idUsuario));
            }
            ordenesRepository.save(orden);
        }

        return diagnosticosRepository.save(diag);
    }
}
