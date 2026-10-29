package com.erp.hardware.controladores;

import com.erp.hardware.modelos.DetalleRepuestosOrden;
import com.erp.hardware.modelos.MovimientosInventario;
import com.erp.hardware.modelos.Repuestos;
import com.erp.hardware.servicios.InventarioService;
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
@RequestMapping("/api/repuestos")
@CrossOrigin(origins = "*")
public class RepuestoController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<Repuestos> listarRepuestos() {
        return inventarioService.listarRepuestos();
    }

    @GetMapping("/stock-bajo")
    public List<Repuestos> listarStockBajo() {
        return inventarioService.listarStockBajo();
    }

    @GetMapping("/{id}")
    public Repuestos obtenerPorId(@PathVariable Integer id) {
        return inventarioService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<?> guardarRepuesto(@RequestBody Repuestos repuesto,
                                             @RequestParam(required = false) Integer idUsuario) {
        try {
            Repuestos guardado = inventarioService.guardarRepuesto(repuesto, idUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }

    // asignar repuesto a una orden descontando inventario
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarRepuesto(@RequestBody Map<String, Object> body) {
        try {
            Integer idOrden = Integer.valueOf(body.get("idOrden").toString());
            Integer idRepuesto = Integer.valueOf(body.get("idRepuesto").toString());
            Integer cantidad = Integer.valueOf(body.get("cantidad").toString());
            Integer idUsuario = body.get("idUsuario") != null ? Integer.valueOf(body.get("idUsuario").toString()) : 1;

            DetalleRepuestosOrden detalle = inventarioService.asignarRepuestoAOrden(idOrden, idRepuesto, cantidad, idUsuario);
            return ResponseEntity.ok(detalle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/orden/{idOrden}")
    public List<DetalleRepuestosOrden> listarDetallesPorOrden(@PathVariable Integer idOrden) {
        return inventarioService.listarDetallesPorOrden(idOrden);
    }

    @GetMapping("/{id}/kardex")
    public List<MovimientosInventario> verKardex(@PathVariable Integer id) {
        return inventarioService.listarKardexPorRepuesto(id);
    }

    @GetMapping("/kardex/ultimos")
    public List<MovimientosInventario> ultimosMovimientos() {
        return inventarioService.listarUltimosMovimientos();
    }
}
