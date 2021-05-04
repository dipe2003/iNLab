/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.modelo.muestreos;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dipe2
 */
@Entity
public class RevMuestreoProducto extends RevMuestreo {

    @Temporal(TemporalType.DATE)
    private Date fechaProduccion;
    @Temporal(TemporalType.DATE)
    private Date fechaOrigen;

    /*
        Constructores
     */
    public RevMuestreoProducto() {
        super();
    }

    public RevMuestreoProducto(Long id, String denominacion, String destino, String monitor, String observaciones, String area,
            boolean esRepeticion, Date fechaMuestreo, Date fechaProduccion, Date fechaOrigen) {
        super(id, denominacion, destino, monitor, observaciones, area, esRepeticion, fechaMuestreo);
        this.fechaProduccion = fechaProduccion;
        this.fechaOrigen = fechaOrigen;
    }

    //<editor-fold desc="Getters">
    public Date getFechaProduccion() {
        return fechaProduccion;
    }

    public Date getFechaOrigen() {
        return fechaOrigen;
    }

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setFechaProduccion(Date fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public void setFechaOrigen(Date fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    //</editor-fold>
}
