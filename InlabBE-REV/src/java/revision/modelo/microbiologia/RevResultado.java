/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.modelo.microbiologia;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import revision.modelo.muestreos.RevMuestreo;

/**
 *
 * @author dipe2
 */
@Entity
public class RevResultado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    
    private String requisito;
    private String analista;
    private String laboratorio;
    private String observaciones;
    private float resultadoRecuento;
    private float limiteRecuento;
    private ValorDeteccion resultadoDeteccion;
    private ValorDeteccion limiteDeteccion;
    private TipoResultado tipoResultado;
    @Temporal(TemporalType.DATE)
    private Date fechaResultado;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private RevMuestreo revMuestreo;
    
    //<editor-fold desc="Constructores">

    public RevResultado() {
    }

    public RevResultado(Long id, String requisito, String analista, String laboratorio, String observaciones, 
            float resultadoRecuento, float limiteRecuento, Date fechaResultado, RevMuestreo revMuestreo) {
        this.id = id;
        this.requisito = requisito;
        this.analista = analista;
        this.laboratorio = laboratorio;
        this.observaciones = observaciones;
        this.resultadoRecuento = resultadoRecuento;
        this.limiteRecuento = limiteRecuento;
        this.tipoResultado = TipoResultado.Recuento;
        this.fechaResultado = fechaResultado;       
        this.revMuestreo = revMuestreo;
    }

    
    public RevResultado(Long id, String requisito, String analista, String laboratorio, String observaciones, 
            ValorDeteccion resultadoDeteccion, ValorDeteccion limiteDeteccion, Date fechaResultado, RevMuestreo revMuestreo) {
        this.id = id;
        this.requisito = requisito;
        this.analista = analista;
        this.laboratorio = laboratorio;
        this.observaciones = observaciones;
        this.resultadoDeteccion = resultadoDeteccion;
        this.limiteDeteccion = limiteDeteccion;
        this.tipoResultado = TipoResultado.Recuento;
        this.fechaResultado = fechaResultado;    
        this.revMuestreo = revMuestreo;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Getters">
        
    public Long getId() {
        return id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRequisito() {
        return requisito;
    }

    public String getAnalista() {
        return analista;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public float getResultadoRecuento() {
        return resultadoRecuento;
    }

    public float getLimiteRecuento() {
        return limiteRecuento;
    }

    
    public ValorDeteccion getResultadoDeteccion() {
        return resultadoDeteccion;
    }

    public ValorDeteccion getLimiteDeteccion() {
        return limiteDeteccion;
    }

    
    public TipoResultado getTipoResultado() {
        return tipoResultado;
    }

    public Date getFechaResultado() {
        return fechaResultado;
    }

    public RevMuestreo getRevMuestreo() {
        return revMuestreo;
    }

    
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setRequisito(String requisito) {
        this.requisito = requisito;
    }

    public void setAnalista(String analista) {
        this.analista = analista;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setResultadoRecuento(float resultadoRecuento) {
        this.resultadoRecuento = resultadoRecuento;
    }

    public void setLimiteRecuento(float limiteRecuento) {
        this.limiteRecuento = limiteRecuento;
    }
    
    
    public void setResultadoDeteccion(ValorDeteccion resultadoDeteccion) {
        this.resultadoDeteccion = resultadoDeteccion;
    }

    public void setLimiteDeteccion(ValorDeteccion limiteDeteccion) {
        this.limiteDeteccion = limiteDeteccion;
    }
    
    public void setTipoResultado(TipoResultado tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public void setFechaResultado(Date fechaResultado) {
        this.fechaResultado = fechaResultado;
    }

    public void setRevMuestreo(RevMuestreo revMuestreo) {
        this.revMuestreo = revMuestreo;
    }

    
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    
    public boolean EsAceptable(){
        if(tipoResultado == TipoResultado.Busqueda){
            return resultadoDeteccion != this.limiteDeteccion;
        }else{
            return resultadoRecuento < limiteRecuento;
        }        
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
        if (!(object instanceof RevResultado)) {
            return false;
        }
        RevResultado other = (RevResultado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.microbiologia.RevResultado[ id=" + id + " ]";
    }
    //</editor-fold>
}
