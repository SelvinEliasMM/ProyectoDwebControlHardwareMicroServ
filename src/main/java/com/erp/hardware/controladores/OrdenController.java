package com.erp.hardware.controladores;

import com.erp.hardware.modelos.BitacoraAuditoria;
import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.BitacoraAuditoriaRepository;
import com.erp.hardware.repositorios.OrdenesTrabajoRepository;
import com.erp.hardware.repositorios.UsuarioRepository;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class OrdenController {

    @Autowired
    private OrdenesTrabajoRepository ordenesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BitacoraAuditoriaRepository bitacoraRepository;

    @GetMapping
    public List<OrdenesTrabajo> listarOrdenes() {
        return ordenesRepository.findByEstadoRegistroTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return ordenesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // guardar orden y generar ticket
    @PostMapping
    public ResponseEntity<?> guardarOrden(@RequestBody OrdenesTrabajo orden) {
        try {
            if (orden.getIdEquipo() == null || orden.getIdEquipo().getIdEquipo() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "Debe seleccionar un equipo válido"));
            }
            if (orden.getIdUsuarioTecnico() == null || orden.getIdUsuarioTecnico().getIdUsuario() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "Debe asignar un técnico a la orden"));
            }
            if (orden.getProblemaReportado() == null || orden.getProblemaReportado().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "El problema reportado es obligatorio"));
            }

            if (orden.getEstadoReparacion() == null) {
                orden.setEstadoReparacion("Recibido");
            }
            if (orden.getFechaIngreso() == null) {
                orden.setFechaIngreso(new Date());
            }
            if (orden.getRequiereRespaldo() == null) {
                orden.setRequiereRespaldo(false);
            }
            
            orden.setEstadoRegistro(true);
            orden.setFechaCreacion(new Date());

            OrdenesTrabajo guardada = ordenesRepository.save(orden);

            // registro en bitacora
            try {
                BitacoraAuditoria bit = new BitacoraAuditoria();
                bit.setTablaAfectada("ORDENES_TRABAJO");
                bit.setAccionRealizada("INSERT");
                bit.setFechaHora(new Date());
                bit.setIdUsuario(guardada.getUsuarioCreacion() != null ? guardada.getUsuarioCreacion() : guardada.getIdUsuarioTecnico());
                bit.setDatosOriginales("Ingreso inicial");
                bit.setDatosNuevos("{\"id_orden\":" + guardada.getIdOrden() + ",\"estado\":\"" + guardada.getEstadoReparacion() + "\",\"problema\":\"" + guardada.getProblemaReportado() + "\"}");
              //  bitacoraRepository.save(bit);
            } catch (Exception ex) {}

            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }
    
    // actualizar estado con validación de rol/técnico asignado
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        Optional<OrdenesTrabajo> optOrden = ordenesRepository.findById(id);
        if (optOrden.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Orden no encontrada"));
        }

        OrdenesTrabajo orden = optOrden.get();
        String estadoAnt = orden.getEstadoReparacion();
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "El nuevo estado es obligatorio"));
        }

        // solo el técnico asignado o un Administrador puede cambiar el estado
        String modStr = body.get("idUsuarioModificacion");
        if (modStr == null || modStr.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "Acceso denegado: Debe identificarse el usuario que modifica el estado."));
        }

        try {
            Integer idModificador = Integer.parseInt(modStr.trim());
            Optional<Usuarios> optUser = usuarioRepository.findById(idModificador);
            if (optUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("mensaje", "Acceso denegado: Usuario no registrado o no autorizado."));
            }

            Usuarios usuarioModificador = optUser.get();
            boolean esAdmin = "Administrador".equalsIgnoreCase(usuarioModificador.getRol());
            boolean esTecnicoAsignado = orden.getIdUsuarioTecnico() != null 
                    && orden.getIdUsuarioTecnico().getIdUsuario().equals(idModificador);

            if (!esAdmin && !esTecnicoAsignado) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("mensaje", "Acceso denegado: Solo el técnico asignado a esta orden o un Administrador pueden cambiar su estado."));
            }
            orden.setUsuarioModificacion(usuarioModificador);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Identificador de usuario inválido"));
        }

        orden.setEstadoReparacion(nuevoEstado);
        orden.setFechaModificacion(new Date());
        
        OrdenesTrabajo guardada = ordenesRepository.save(orden);

        // registro en bitacora
        try {
            BitacoraAuditoria bit = new BitacoraAuditoria();
            bit.setTablaAfectada("ORDENES_TRABAJO");
            bit.setAccionRealizada("UPDATE");
            bit.setFechaHora(new Date());
            bit.setIdUsuario(guardada.getUsuarioModificacion());
            bit.setDatosOriginales("{\"id_orden\":" + guardada.getIdOrden() + ",\"estado\":\"" + estadoAnt + "\"}");
            bit.setDatosNuevos("{\"id_orden\":" + guardada.getIdOrden() + ",\"estado_anterior\":\"" + estadoAnt + "\",\"estado_nuevo\":\"" + nuevoEstado + "\"}");
          //  bitacoraRepository.save(bit);
        } catch (Exception ex) {}

        return ResponseEntity.ok(guardada);
    }
    
    
    @GetMapping("/consulta-publica/{id}")
    public org.springframework.http.ResponseEntity<?> consultarOrdenPublica(@PathVariable Integer id) {
        return ordenesRepository.findById(id)
            .filter(orden -> Boolean.TRUE.equals(orden.getEstadoRegistro()))
            .map(orden -> {
                java.util.Map<String, Object> respuesta = new java.util.HashMap<>();
                respuesta.put("idOrden", orden.getIdOrden());
                respuesta.put("ticket", "#ORD-" + orden.getIdOrden());
                respuesta.put("cliente", orden.getIdEquipo().getIdCliente().getNombreCliente());
                respuesta.put("equipo", orden.getIdEquipo().getTipoEquipo() + " - " + orden.getIdEquipo().getMarcaModelo());
                respuesta.put("numeroSerie", orden.getIdEquipo().getNumeroSerie());
                respuesta.put("problemaReportado", orden.getProblemaReportado());
                respuesta.put("estadoReparacion", orden.getEstadoReparacion());
                respuesta.put("fechaIngreso", orden.getFechaIngreso());
                respuesta.put("fechaModificacion", orden.getFechaModificacion());
                respuesta.put("tecnicoAsignado", orden.getIdUsuarioTecnico().getNombreCompleto());
                return org.springframework.http.ResponseEntity.ok(respuesta);
            })
            .orElse(org.springframework.http.ResponseEntity.status(404)
                .body(java.util.Map.of("mensaje", "No se encontró ninguna orden activa con ese número de ticket.")));
    }
}