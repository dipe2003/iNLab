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
public class MuestraOtra extends Muestra{

    public MuestraOtra() {
        super();          
    }
    public MuestraOtra(EspecificacionMuestra especificacion){
         super(especificacion);
    }
}
