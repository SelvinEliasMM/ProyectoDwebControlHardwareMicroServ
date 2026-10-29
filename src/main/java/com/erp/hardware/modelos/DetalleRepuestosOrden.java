package com.erp.hardware.modelos;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "Detalle_Repuestos_Orden")
public class DetalleRepuestosOrden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden")
    @ManyToOne(optional = false)
    private OrdenesTrabajo idOrden;

    @JoinColumn(name = "id_repuesto", referencedColumnName = "id_repuesto")
    @ManyToOne(optional = false)
    private Repuestos idRepuesto;

    @Basic(optional = false)
    @Column(name = "cantidad")
    private Integer cantidad;

    @Basic(optional = false)
    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", insertable = false, updatable = false)
    private BigDecimal subtotal;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioCreacion;

    public DetalleRepuestosOrden() {
    }

    public DetalleRepuestosOrden(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public OrdenesTrabajo getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(OrdenesTrabajo idOrden) {
        this.idOrden = idOrden;
    }

    public Repuestos getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Repuestos idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        if (subtotal != null) return subtotal;
        if (cantidad != null && precioUnitario != null) {
            return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
        return BigDecimal.ZERO;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Usuarios getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuarios usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }
}
