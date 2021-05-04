/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.muestreo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dipe2
 */
@Entity
public class MuestraProducto extends Muestra {
    @Temporal(TemporalType.DATE)
    private Date fechaOrigen;
    @Temporal(TemporalType.DATE)
    private Date fechaProduccion;
    private String lote;

    /*
        Constructores
     */
    public MuestraProducto() {
        super();
    }
    
    public MuestraProducto(EspecificacionMuestra especificacion, Date fechaOrigen, Date fechaProduccion){
        super(especificacion);
        this.fechaOrigen = fechaOrigen;
        this.fechaProduccion =  fechaProduccion;
    }
    
    public MuestraProducto(EspecificacionMuestra especificacion, Date fechaOrigen, Date fechaProduccion, String lote){
        super(especificacion);
        this.fechaOrigen = fechaOrigen;
        this.fechaProduccion =  fechaProduccion;
        this.lote = lote;
    }

    //<editor-fold desc="Getters">
    public Date getFechaOrigen() {
        return this.fechaOrigen;
    }

    public Date getFechaProduccion() {
        return this.fechaProduccion;
    }

    public String getLote() {
        return this.lote;
    }

    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setFechaOrigen(Date fecha) {
        this.fechaOrigen = fecha;
    }

    public void setFechaProduccion(Date fecha) {
        this.fechaProduccion = fecha;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
    //</editor-fold>
}
