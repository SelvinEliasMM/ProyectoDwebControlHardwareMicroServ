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
@Table(name = "Diagnosticos")
public class Diagnosticos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_diagnostico")
    private Integer idDiagnostico;

    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden")
    @ManyToOne(optional = false)
    private OrdenesTrabajo idOrden;

    @JoinColumn(name = "id_tecnico", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuarios idTecnico;

    @Column(name = "fecha_diagnostico")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDiagnostico;

    @Basic(optional = false)
    @Column(name = "diagnostico_tecnico")
    private String diagnosticoTecnico;

    @Column(name = "solucion_propuesta")
    private String solucionPropuesta;

    @Basic(optional = false)
    @Column(name = "requiere_repuesto")
    private Boolean requiereRepuesto = false;

    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioCreacion;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @JoinColumn(name = "usuario_modificacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioModificacion;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Column(name = "estado_registro")
    private Boolean estadoRegistro = true;

    public Diagnosticos() {
    }

    public Diagnosticos(Integer idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public Integer getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(Integer idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public OrdenesTrabajo getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(OrdenesTrabajo idOrden) {
        this.idOrden = idOrden;
    }

    public Usuarios getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Usuarios idTecnico) {
        this.idTecnico = idTecnico;
    }

    public Date getFechaDiagnostico() {
        return fechaDiagnostico;
    }

    public void setFechaDiagnostico(Date fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public String getDiagnosticoTecnico() {
        return diagnosticoTecnico;
    }

    public void setDiagnosticoTecnico(String diagnosticoTecnico) {
        this.diagnosticoTecnico = diagnosticoTecnico;
    }

    public String getSolucionPropuesta() {
        return solucionPropuesta;
    }

    public void setSolucionPropuesta(String solucionPropuesta) {
        this.solucionPropuesta = solucionPropuesta;
    }

    public Boolean getRequiereRepuesto() {
        return requiereRepuesto;
    }

    public void setRequiereRepuesto(Boolean requiereRepuesto) {
        this.requiereRepuesto = requiereRepuesto;
    }

    public Usuarios getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuarios usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuarios getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuarios usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
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
}
