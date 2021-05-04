/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

import javax.persistence.Entity;

/**
 *
 * @author dipe2
 */
@Entity
public class Recuento extends Limite {

    private float valorRecuentoMarginal;
    private float valorRecuentoInaceptable;

    /*
        Constructores
     */
    public Recuento() {
        super.setTipoLimite(TipoLimite.Recuento);
    }

    public Recuento(float marginal, float inaceptable, Requisito requisito) {
        super(requisito, TipoLimite.Recuento);
        this.valorRecuentoInaceptable = inaceptable;
        this.valorRecuentoMarginal = marginal;
    }

    //<editor-fold desc="Getters">
    public float getValorRecuentoMarginal() {
        return valorRecuentoMarginal;
    }

    public float getValorRecuentoInaceptable() {
        return valorRecuentoInaceptable;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setValorRecuentoMarginal(float valorRecuentoMarginal) {
        this.valorRecuentoMarginal = valorRecuentoMarginal;
    }

    public void setValorRecuentoInaceptable(float valorRecuentoInaceptable) {
        this.valorRecuentoInaceptable = valorRecuentoInaceptable;
    }

    //</editor-fold>
}
