/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.muestreo;

import javax.persistence.Entity;

/**
 *
 * @author dipe2
 */
@Entity
public class MuestraAmbiente extends Muestra{
    private boolean contactaProducto;

    public MuestraAmbiente() {
        super();
    }
    /*
        Constructores
     */
    public MuestraAmbiente(EspecificacionMuestra especificacion, boolean contactaProducto) {
        super(especificacion);
        this.contactaProducto = contactaProducto;
    }

    /*
        Getters
     */
    public boolean getContactaProducto() {
        return this.contactaProducto;
    }

    /*
        Setters
     */
    public void setContactaProducto(boolean contactaProducto) {
        this.contactaProducto = contactaProducto;
    }
}
