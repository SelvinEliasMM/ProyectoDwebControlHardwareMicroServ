/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author selvi
 */
@Entity
@Table(name = "Ordenes_Trabajo")
@NamedQueries({
    @NamedQuery(name = "OrdenesTrabajo.findAll", query = "SELECT o FROM OrdenesTrabajo o"),
    @NamedQuery(name = "OrdenesTrabajo.findByIdOrden", query = "SELECT o FROM OrdenesTrabajo o WHERE o.idOrden = :idOrden"),
    @NamedQuery(name = "OrdenesTrabajo.findByFechaIngreso", query = "SELECT o FROM OrdenesTrabajo o WHERE o.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "OrdenesTrabajo.findByProblemaReportado", query = "SELECT o FROM OrdenesTrabajo o WHERE o.problemaReportado = :problemaReportado"),
    @NamedQuery(name = "OrdenesTrabajo.findByNotasEspeciales", query = "SELECT o FROM OrdenesTrabajo o WHERE o.notasEspeciales = :notasEspeciales"),
    @NamedQuery(name = "OrdenesTrabajo.findByEstadoReparacion", query = "SELECT o FROM OrdenesTrabajo o WHERE o.estadoReparacion = :estadoReparacion"),
    @NamedQuery(name = "OrdenesTrabajo.findByFechaEntregaEstimada", query = "SELECT o FROM OrdenesTrabajo o WHERE o.fechaEntregaEstimada = :fechaEntregaEstimada"),
    @NamedQuery(name = "OrdenesTrabajo.findByFechaCreacion", query = "SELECT o FROM OrdenesTrabajo o WHERE o.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "OrdenesTrabajo.findByFechaModificacion", query = "SELECT o FROM OrdenesTrabajo o WHERE o.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "OrdenesTrabajo.findByEstadoRegistro", query = "SELECT o FROM OrdenesTrabajo o WHERE o.estadoRegistro = :estadoRegistro")})
public class OrdenesTrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_orden")
    private Integer idOrden;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "problema_reportado")
    private String problemaReportado;
    @Column(name = "notas_especiales")
    private String notasEspeciales;
    @Column(name = "estado_reparacion")
    private String estadoReparacion;
    @Column(name = "fecha_entrega_estimada")
    @Temporal(TemporalType.DATE)
    private Date fechaEntregaEstimada;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado_registro")
    private Boolean estadoRegistro;
    @Column(name = "requiere_respaldo")
    private Boolean requiereRespaldo = false;
    // token unico del ticket
    @Column(name = "token_seguimiento", unique = true, updatable = false)
    private String tokenSeguimiento;
    @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo")
    @ManyToOne(optional = false)
    private Equipos idEquipo;
    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioCreacion;
    @JoinColumn(name = "usuario_modificacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioModificacion;
    @JoinColumn(name = "id_usuario_tecnico", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuarioTecnico;

    public OrdenesTrabajo() {
    }

    public OrdenesTrabajo(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public OrdenesTrabajo(Integer idOrden, String problemaReportado) {
        this.idOrden = idOrden;
        this.problemaReportado = problemaReportado;
    }

    public String getTokenSeguimiento() { return tokenSeguimiento; }

    public void setTokenSeguimiento(String tokenSeguimiento) { this.tokenSeguimiento = tokenSeguimiento; }

    public Integer getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getProblemaReportado() {
        return problemaReportado;
    }

    public void setProblemaReportado(String problemaReportado) {
        this.problemaReportado = problemaReportado;
    }

    public String getNotasEspeciales() {
        return notasEspeciales;
    }

    public void setNotasEspeciales(String notasEspeciales) {
        this.notasEspeciales = notasEspeciales;
    }

    public String getEstadoReparacion() {
        return estadoReparacion;
    }

    public void setEstadoReparacion(String estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    public Date getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(Date fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(Boolean estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public Equipos getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Equipos idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Usuarios getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuarios usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Usuarios getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuarios usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Usuarios getIdUsuarioTecnico() {
        return idUsuarioTecnico;
    }

    public void setIdUsuarioTecnico(Usuarios idUsuarioTecnico) {
        this.idUsuarioTecnico = idUsuarioTecnico;
    }

    public Boolean getRequiereRespaldo() {
        return requiereRespaldo;
    }

    public void setRequiereRespaldo(Boolean requiereRespaldo) {
        this.requiereRespaldo = requiereRespaldo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrden != null ? idOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenesTrabajo)) {
            return false;
        }
        OrdenesTrabajo other = (OrdenesTrabajo) object;
        if ((this.idOrden == null && other.idOrden != null) || (this.idOrden != null && !this.idOrden.equals(other.idOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.erp.hardware.modelos.OrdenesTrabajo[ idOrden=" + idOrden + " ]";
    }
    
}
