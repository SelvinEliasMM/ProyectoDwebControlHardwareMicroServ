package com.erp.hardware.servicios;

import com.erp.hardware.modelos.BitacoraAuditoria;
import com.erp.hardware.modelos.DetalleRepuestosOrden;
import com.erp.hardware.modelos.MovimientosInventario;
import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.modelos.Repuestos;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.BitacoraAuditoriaRepository;
import com.erp.hardware.repositorios.DetalleRepuestosOrdenRepository;
import com.erp.hardware.repositorios.MovimientosInventarioRepository;
import com.erp.hardware.repositorios.OrdenesTrabajoRepository;
import com.erp.hardware.repositorios.RepuestosRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioService {

    @Autowired
    private RepuestosRepository repuestosRepository;

    @Autowired
    private DetalleRepuestosOrdenRepository detalleRepository;

    @Autowired
    private MovimientosInventarioRepository movimientosRepository;

    @Autowired
    private OrdenesTrabajoRepository ordenesRepository;

    @Autowired
    private BitacoraAuditoriaRepository bitacoraRepository;

    public List<Repuestos> listarRepuestos() {
        return repuestosRepository.findByEstadoRegistroTrue();
    }

    public List<Repuestos> listarStockBajo() {
        return repuestosRepository.findStockBajo();
    }

    public Repuestos obtenerPorId(Integer id) {
        return repuestosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado con ID: " + id));
    }

    @Transactional
    public Repuestos guardarRepuesto(Repuestos repuesto, Integer idUsuario) {
        boolean esNuevo = (repuesto.getIdRepuesto() == null);
        if (repuesto.getPrecioCosto() == null && repuesto.getPrecioVenta() != null) {
            repuesto.setPrecioCosto(repuesto.getPrecioVenta());
        }
        if (repuesto.getPrecioVenta() != null && repuesto.getPrecioCosto() != null
                && repuesto.getPrecioVenta().compareTo(repuesto.getPrecioCosto()) < 0) {
            throw new IllegalArgumentException("El precio de venta no puede ser menor al costo.");
        }

        if (esNuevo) {
            repuesto.setFechaCreacion(new Date());
            repuesto.setEstadoRegistro(true);
            if (idUsuario != null) {
                repuesto.setUsuarioCreacion(new Usuarios(idUsuario));
            }
        } else {
            repuesto.setFechaModificacion(new Date());
            if (idUsuario != null) {
                repuesto.setUsuarioModificacion(new Usuarios(idUsuario));
            }
        }

        Repuestos guardado = repuestosRepository.save(repuesto);

        // si es nuevo y se le asignó stock inicial, registrar entrada en Kardex
        if (esNuevo && guardado.getStock() > 0) {
            MovimientosInventario mov = new MovimientosInventario();
            mov.setIdRepuesto(guardado);
            mov.setTipoMovimiento("ENTRADA");
            mov.setCantidad(guardado.getStock());
            mov.setStockAnterior(0);
            mov.setStockNuevo(guardado.getStock());
            mov.setMotivo("Carga inicial de producto");
            mov.setFechaMovimiento(new Date());
            if (idUsuario != null) {
                mov.setIdUsuario(new Usuarios(idUsuario));
            } else {
                mov.setIdUsuario(new Usuarios(1));
            }
            movimientosRepository.save(mov);
        }

        // bitacora
        BitacoraAuditoria bit = new BitacoraAuditoria();
        bit.setTablaAfectada("REPUESTOS");
        bit.setAccionRealizada(esNuevo ? "INSERT" : "UPDATE");
        bit.setFechaHora(new Date());
        bit.setIdUsuario(new Usuarios(idUsuario != null ? idUsuario : 1));
        bit.setDatosOriginales(esNuevo ? "Nuevo repuesto" : "Actualización de repuesto");
        bit.setDatosNuevos("{\"id_repuesto\":" + guardado.getIdRepuesto() + ",\"codigo\":\"" + guardado.getCodigoRepuesto() + "\",\"nombre\":\"" + guardado.getNombreRepuesto() + "\",\"stock\":" + guardado.getStock() + "}");
       // bitacoraRepository.save(bit);

        return guardado;
    }

    // asignar repuesto a orden descontando automaticamente del stock y registrando Kardex
    @Transactional
    public DetalleRepuestosOrden asignarRepuestoAOrden(Integer idOrden, Integer idRepuesto, Integer cantidad, Integer idUsuario) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + idOrden));

        Repuestos repuesto = repuestosRepository.findById(idRepuesto)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado: " + idRepuesto));

        int stockActual = repuesto.getStock() != null ? repuesto.getStock() : 0;
        if (stockActual < cantidad) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + stockActual + ", Solicitado: " + cantidad);
        }

        int stockNuevo = stockActual - cantidad;
        repuesto.setStock(stockNuevo);
        repuesto.setFechaModificacion(new Date());
        if (idUsuario != null) {
            repuesto.setUsuarioModificacion(new Usuarios(idUsuario));
        }
        repuestosRepository.save(repuesto);

        // guardar detalle
        DetalleRepuestosOrden detalle = new DetalleRepuestosOrden();
        detalle.setIdOrden(orden);
        detalle.setIdRepuesto(repuesto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(repuesto.getPrecioVenta());
        detalle.setSubtotal(repuesto.getPrecioVenta().multiply(BigDecimal.valueOf(cantidad)));
        detalle.setFechaRegistro(new Date());
        if (idUsuario != null) {
            detalle.setUsuarioCreacion(new Usuarios(idUsuario));
        }
        DetalleRepuestosOrden detalleGuardado = detalleRepository.save(detalle);

        // registrar Kardex
        MovimientosInventario mov = new MovimientosInventario();
        mov.setIdRepuesto(repuesto);
        mov.setTipoMovimiento("SALIDA");
        mov.setCantidad(cantidad);
        mov.setStockAnterior(stockActual);
        mov.setStockNuevo(stockNuevo);
        mov.setMotivo("Asignado a Orden #ORD-" + orden.getIdOrden());
        mov.setIdOrden(orden);
        mov.setFechaMovimiento(new Date());
        if (idUsuario != null) {
            mov.setIdUsuario(new Usuarios(idUsuario));
        } else {
            mov.setIdUsuario(new Usuarios(1));
        }
        movimientosRepository.save(mov);

        return detalleGuardado;
    }

    public List<DetalleRepuestosOrden> listarDetallesPorOrden(Integer idOrden) {
        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return detalleRepository.findByIdOrdenOrderByFechaRegistroAsc(orden);
    }

    public List<MovimientosInventario> listarKardexPorRepuesto(Integer idRepuesto) {
        Repuestos repuesto = obtenerPorId(idRepuesto);
        return movimientosRepository.findByIdRepuestoOrderByFechaMovimientoDesc(repuesto);
    }

    public List<MovimientosInventario> listarUltimosMovimientos() {
        return movimientosRepository.findTop50ByOrderByFechaMovimientoDesc();
    }
}
