package com.erp.hardware.controladores;

import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.OrdenesTrabajoRepository;
import com.erp.hardware.repositorios.UsuarioRepository;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
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

    // caracteres sin ambiguos (0/O, 1/I)
    private static final String CARACTERES = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private OrdenesTrabajoRepository ordenesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

            // Asegurar que siempre exista usuario de creación para el Trigger de SQL Server
            if (orden.getUsuarioCreacion() == null || orden.getUsuarioCreacion().getIdUsuario() == null) {
                orden.setUsuarioCreacion(new Usuarios(1));
            }

            orden.setEstadoRegistro(true);
            orden.setFechaCreacion(new Date());
            orden.setTokenSeguimiento(generarToken());

            OrdenesTrabajo guardada = ordenesRepository.save(orden);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        Optional<OrdenesTrabajo> optOrden = ordenesRepository.findById(id);
        if (optOrden.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Orden no encontrada"));
        }

        OrdenesTrabajo orden = optOrden.get();
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "El nuevo estado es obligatorio"));
        }

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
        return ResponseEntity.ok(guardada);
    }

    // genera token unico tipo TF-XXXXXXXX
    private String generarToken() {
        String token;
        do {
            StringBuilder sb = new StringBuilder("TF-");
            for (int i = 0; i < 8; i++) {
                sb.append(CARACTERES.charAt(RANDOM.nextInt(CARACTERES.length())));
            }
            token = sb.toString();
        } while (ordenesRepository.existsByTokenSeguimiento(token));
        return token;
    }

    // consulta publica por token
    @GetMapping("/consulta-publica/{token}")
    public ResponseEntity<?> consultarOrdenPublica(@PathVariable String token) {
        String tokenLimpio = token == null ? "" : token.trim().toUpperCase();
        return ordenesRepository.findByTokenSeguimiento(tokenLimpio)
                .filter(orden -> !Boolean.FALSE.equals(orden.getEstadoRegistro()))
                .map(orden -> {
                    Map<String, Object> respuesta = new HashMap<>();
                    respuesta.put("ticket", orden.getTokenSeguimiento());
                    respuesta.put("cliente", orden.getIdEquipo().getIdCliente().getNombreCliente());
                    respuesta.put("equipo", orden.getIdEquipo().getTipoEquipo() + " - " + orden.getIdEquipo().getMarcaModelo());
                    respuesta.put("numeroSerie", orden.getIdEquipo().getNumeroSerie());
                    respuesta.put("problemaReportado", orden.getProblemaReportado());
                    respuesta.put("estadoReparacion", orden.getEstadoReparacion());
                    respuesta.put("fechaIngreso", orden.getFechaIngreso());
                    respuesta.put("fechaModificacion", orden.getFechaModificacion());
                    respuesta.put("tecnicoAsignado", orden.getIdUsuarioTecnico().getNombreCompleto());
                    return ResponseEntity.ok(respuesta);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "No se encontró ninguna orden con ese código de seguimiento.")));
    }
}