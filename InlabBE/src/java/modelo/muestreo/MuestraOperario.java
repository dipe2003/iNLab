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
public class MuestraOperario extends Muestra {

    private String nombre;
    private int padron;

    public MuestraOperario() {
        super();
    }

    /*
        Constructores
     */
    public MuestraOperario(EspecificacionMuestra especificacion, String nombre, int padron) {
        super(especificacion);
        this.nombre = nombre;
        this.padron = padron;
    }

    /*
        Getters
     */
    public String getNombre() {
        return nombre;
    }

    public int getPadron() {
        return padron;
    }

    /*
        Setters
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPadron(int padron) {
        this.padron = padron;
    }
}
