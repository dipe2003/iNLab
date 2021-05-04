/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.modelo.muestreos;

import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author dipe2
 */
@Entity
public class RevMuestreoGenerica extends RevMuestreo {

    private boolean enContactoProducto;

    /*
        Constructores
     */
    public RevMuestreoGenerica() {
        super();
    }

    public RevMuestreoGenerica(Long id, String denominacion, String destino, String monitor, String observaciones, String area,
            boolean esRepeticion, Date fechaMuestreo, boolean contactaProducto) {
        super(id, denominacion, destino, monitor, observaciones, area, esRepeticion, fechaMuestreo);
        this.enContactoProducto = contactaProducto;
    }

    //<editor-fold desc="Getters">

    public boolean isEnContactoProducto() {
        return enContactoProducto;
    }
   

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setEnContactoProducto(boolean enContactoProducto) {
        this.enContactoProducto = enContactoProducto;
    }

    //</editor-fold>
}
