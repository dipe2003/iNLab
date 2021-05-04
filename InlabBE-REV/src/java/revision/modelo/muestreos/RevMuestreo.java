/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.modelo.muestreos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import revision.modelo.microbiologia.RevResultado;
import revision.modelo.microbiologia.ValorDeteccion;

/**
 *
 * @author dipe2
 */
@Entity
public abstract class RevMuestreo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    private String denominacion;
    private String destino;
    private String monitor;
    private String observaciones;
    private String area;
    private boolean esRepeticion;

    @Temporal(TemporalType.DATE)
    private Date fechaMuestreo;

    @OneToMany(mappedBy = "revMuestreo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RevResultado> revResultados;

    /*
        Constructores
     */
    public RevMuestreo() {
        this.revResultados = new ArrayList<>();
    }

    public RevMuestreo(Long id, String denominacion, String destino, String monitor, String observaciones, String area,
            boolean esRepeticion, Date fechaMuestreo) {
        this.id = id;
        this.denominacion = denominacion;
        this.destino = destino;
        this.monitor = monitor;
        this.observaciones = observaciones;
        this.area = area;
        this.esRepeticion = esRepeticion;
        this.fechaMuestreo = fechaMuestreo;
        this.revResultados = new ArrayList<>();
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public String getDestino() {
        return destino;
    }

    public String getMonitor() {
        return monitor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getArea() {
        return area;
    }

    public boolean isEsRepeticion() {
        return esRepeticion;
    }

    public Date getFechaMuestreo() {
        return fechaMuestreo;
    }

    public List<RevResultado> getRevResultados() {
        return revResultados;
    }

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setEsRepeticion(boolean esRepeticion) {
        this.esRepeticion = esRepeticion;
    }

    public void setFechaMuestreo(Date fechaMuestreo) {
        this.fechaMuestreo = fechaMuestreo;
    }

    public void setRevResultado(List<RevResultado> revResultados) {
        this.revResultados = revResultados;
    }

    //</editor-fold>
    //<editor-fold desc="Metodos">
    public void RemoveRevResultado(RevResultado resultado) {
        Iterator it = this.revResultados.iterator();
        RevResultado res = (RevResultado) it.next();
        do {
            if (res.getId().equals(resultado.getId())) {
                it.remove();
                res.setRevMuestreo(null);
                break;
            }
            it.next();
        } while (it.hasNext());
    }

    public boolean ExisteResultado(RevResultado resultado) {
        return this.revResultados.stream()
                .anyMatch(res -> Objects.equals(res.getId(), resultado.getId()));
    }

    public RevResultado CrearResultadoRecuento(Long idResultado, String requisito, String analista, String laboratorio, String observaciones,
            float resultadoRecuento, float limiteRecuento, Date fechaResultado) {
        RevResultado resultado = new RevResultado(idResultado, requisito, analista, laboratorio, observaciones, resultadoRecuento, limiteRecuento,
                fechaResultado, this);
        this.revResultados.add(resultado);
        return resultado;
    }

    public RevResultado CrearResultadoDeteccion(Long idResultado, String requisito, String analista, String laboratorio, String observaciones,
            ValorDeteccion resultadoDeteccion, ValorDeteccion limiteDeteccion, Date fechaResultado) {
        RevResultado resultado = new RevResultado(idResultado, requisito, analista, laboratorio, observaciones, resultadoDeteccion, limiteDeteccion,
                fechaResultado, this);
        this.revResultados.add(resultado);
        return resultado;
    }

    public RevResultado GetRevResultado(Long id) {
        return this.revResultados
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .get();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RevMuestreo)) {
            return false;
        }
        RevMuestreo other = (RevMuestreo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.muestreos.RevMuestreo[ id=" + id + " ]";
    }
//</editor-fold>
}
