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
@Table(name = "Historial_Estados")
public class HistorialEstados implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden")
    @ManyToOne(optional = false)
    private OrdenesTrabajo idOrden;

    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;

    @Column(name = "estado_anterior")
    private String estadoAnterior;

    @Basic(optional = false)
    @Column(name = "estado_nuevo")
    private String estadoNuevo;

    @Column(name = "fecha_cambio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCambio;

    @Column(name = "comentario")
    private String comentario;

    public HistorialEstados() {
    }

    public HistorialEstados(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
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

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
