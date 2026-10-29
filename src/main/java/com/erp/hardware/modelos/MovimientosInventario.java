package com.erp.hardware.modelos;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Movimientos_Inventario")
public class MovimientosInventario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @JoinColumn(name = "id_repuesto", referencedColumnName = "id_repuesto")
    @ManyToOne(optional = false)
    private Repuestos idRepuesto;

    @Basic(optional = false)
    @Column(name = "tipo_movimiento")
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE

    @Basic(optional = false)
    @Column(name = "cantidad")
    private Integer cantidad;

    @Basic(optional = false)
    @Column(name = "stock_anterior")
    private Integer stockAnterior;

    @Basic(optional = false)
    @Column(name = "stock_nuevo")
    private Integer stockNuevo;

    @Basic(optional = false)
    @Column(name = "motivo")
    private String motivo;

    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden")
    @ManyToOne
    private OrdenesTrabajo idOrden;

    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;

    @Column(name = "fecha_movimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMovimiento;

    public MovimientosInventario() {
    }

    public MovimientosInventario(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Repuestos getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Repuestos idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Integer stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Integer getStockNuevo() {
        return stockNuevo;
    }

    public void setStockNuevo(Integer stockNuevo) {
        this.stockNuevo = stockNuevo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public OrdenesTrabajo getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(OrdenesTrabajo idOrden) {
        this.idOrden = idOrden;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
}
