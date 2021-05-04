/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.muestreo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dipe2
 */
@Entity
public class EspecificacionMuestra implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String denominacion;
    private String identificacion;
    @Temporal(TemporalType.DATE)
    private Date fechaObsoleto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoMuestra", length = 255)
    private TipoMuestra tipoMuestra;

    @ManyToOne
    private Area area;

    @OneToMany(mappedBy = "especificacionMuestra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Muestra> muestras;

    @ManyToOne
    private Destino destino;

    public EspecificacionMuestra() {
    }

    public EspecificacionMuestra(String denominacion, String identificacion, Area area, TipoMuestra tipo, Destino destino) {
        this.denominacion = denominacion;
        this.identificacion = identificacion;
        this.area = area;
        this.tipoMuestra = tipo;
        this.destino = destino;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public Date getFechaObsoleto() {
        return fechaObsoleto;
    }

    public Area getArea() {
        return this.area;
    }

    public TipoMuestra getTipoMuestra() {
        return tipoMuestra;
    }

    public List<Muestra> getMuestras() {
        return muestras;
    }

    public Destino getDestino() {
        return destino;
    }

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setFechaObsoleto(Date fechaObsoleto) {
        this.fechaObsoleto = fechaObsoleto;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setTipoMuestra(TipoMuestra tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public void setMuestras(List<Muestra> muestras) {
        this.muestras = muestras;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }
    //</editor-fold>

    public boolean isVigente() {
        return this.fechaObsoleto == null;
    }

    //<editor-fold desc="Metodos">
    public Muestra CrearMuestraProducto(Date fechaOrigen, Date fechaProduccion, String lote) throws Exception {
        if (this.tipoMuestra != TipoMuestra.Producto) {
            throw new Exception("El tipo de muestra a crear es diferente a la especificacion.");
        }
        Muestra muestra = new MuestraProducto(this, fechaOrigen, fechaProduccion, lote);
        this.muestras.add(muestra);
        return muestra;
    }

    public Muestra CrearMuestraProducto(Date fechaOrigen, Date fechaProduccion) throws Exception {
        if (this.tipoMuestra != TipoMuestra.Producto) {
            throw new Exception("El tipo de muestra a crear es diferente a la especificacion.");
        }
        Muestra muestra = new MuestraProducto(this, fechaOrigen, fechaProduccion);
        this.muestras.add(muestra);
        return muestra;
    }

    public Muestra CrearMuestraAmbiente(boolean enContactoProducto) throws Exception {
        if (this.tipoMuestra != TipoMuestra.Ambiente) {
            throw new Exception("El tipo de muestra a crear es diferente a la especificacion.");
        }
        Muestra muestra = new MuestraAmbiente(this, enContactoProducto);
        this.muestras.add(muestra);
        return muestra;
    }

    public Muestra CrearMuestraOperario(String nombreOperario, int padron) throws Exception {
        if (this.tipoMuestra != TipoMuestra.Operario) {
            throw new Exception("El tipo de muestra a crear es diferente a la especificacion.");
        }
        Muestra muestra = new MuestraOperario(this, nombreOperario, padron);
        this.muestras.add(muestra);
        return muestra;
    }

    public Muestra CrearMuestraOtra() throws Exception {
        if (this.tipoMuestra != TipoMuestra.Otra) {
            throw new Exception("El tipo de muestra a crear es diferente a la especificacion.");
        }
        Muestra muestra = new MuestraOtra(this);
        this.muestras.add(muestra);
        return muestra;
    }

    //</editor-fold>
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EspecificacionMuestra)) {
            return false;
        }
        EspecificacionMuestra other = (EspecificacionMuestra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.muestreo.EspecificacionMuestra[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        return this.denominacion.compareTo(((EspecificacionMuestra)o).denominacion);
    }

}
