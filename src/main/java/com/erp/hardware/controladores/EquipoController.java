package com.erp.hardware.controladores;

import com.erp.hardware.modelos.BitacoraAuditoria;
import com.erp.hardware.modelos.Equipos;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.BitacoraAuditoriaRepository;
import com.erp.hardware.repositorios.EquiposRepository;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    @Autowired
    private EquiposRepository equiposRepository;

    @Autowired
    private BitacoraAuditoriaRepository bitacoraRepository;

    @GetMapping
    public List<Equipos> listarEquipos() {
        return equiposRepository.findByEstadoRegistroTrue(); 
    }

    // registro de hardware con validación de serie única
    @PostMapping
    public ResponseEntity<?> guardarEquipo(@RequestBody Equipos equipo) {
        try {
            if (equipo.getIdCliente() == null || equipo.getIdCliente().getIdCliente() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "Debe asignar el equipo a un cliente registrado"));
            }
            if (equipo.getMarcaModelo() == null || equipo.getMarcaModelo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "La marca y modelo del equipo son obligatorios"));
            }
            if (equipo.getNumeroSerie() == null || equipo.getNumeroSerie().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "El número de serie del equipo es obligatorio"));
            }

            String serie = equipo.getNumeroSerie().trim();
            if (equiposRepository.findByNumeroSerie(serie).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("mensaje", "Ya existe un equipo registrado con el número de serie: " + serie));
            }
            equipo.setNumeroSerie(serie);

            equipo.setEstadoRegistro(true);
            equipo.setFechaCreacion(new Date());

            Equipos guardado = equiposRepository.save(equipo);

            // registro en bitacora
            try {
                BitacoraAuditoria bit = new BitacoraAuditoria();
                bit.setTablaAfectada("EQUIPOS");
                bit.setAccionRealizada("INSERT");
                bit.setFechaHora(new Date());
                bit.setIdUsuario(guardado.getUsuarioCreacion() != null ? guardado.getUsuarioCreacion() : new Usuarios(1));
                bit.setDatosOriginales("Nuevo equipo");
                bit.setDatosNuevos("{\"id_equipo\":" + guardado.getIdEquipo() + ",\"marca_modelo\":\"" + guardado.getMarcaModelo() + "\",\"numero_serie\":\"" + guardado.getNumeroSerie() + "\"}");
                bitacoraRepository.save(bit);
            } catch (Exception ex) {}

            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }
}
