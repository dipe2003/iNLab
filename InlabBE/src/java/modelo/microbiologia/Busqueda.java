/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author dipe2
 */
@Entity
public class Busqueda extends Limite {

    @Enumerated(EnumType.STRING)
    private ValorDeteccion valorBusquedaAceptable;
    @Enumerated(EnumType.STRING)
    private ValorDeteccion valorBusquedaInaceptable;

    /*
        Constructores
     */
    public Busqueda() {
        super.setTipoLimite(TipoLimite.Busqueda);
    }

    public Busqueda(ValorDeteccion inaceptable, ValorDeteccion aceptable, Requisito requisito) {
        super(requisito, TipoLimite.Busqueda);
        this.valorBusquedaAceptable = aceptable;
        this.valorBusquedaInaceptable = inaceptable;
    }

    //<editor-fold desc="Getters">
    public ValorDeteccion getValorBusquedaAceptable() {
        return valorBusquedaAceptable;
    }

    public ValorDeteccion getValorBusquedaInaceptable() {
        return valorBusquedaInaceptable;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setValorBusquedaAceptable(ValorDeteccion valorBusquedaAceptable) {
        this.valorBusquedaAceptable = valorBusquedaAceptable;
    }

    public void setValorBusquedaInaceptable(ValorDeteccion valorBusquedaInaceptable) {
        this.valorBusquedaInaceptable = valorBusquedaInaceptable;
    }
    //</edutir-fold>
}
