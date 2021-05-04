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
public class ResultadoBusqueda extends Resultado{

    @Enumerated(EnumType.STRING)
    private ValorDeteccion valor;
    
    /*
        Constructores
    */
    public ResultadoBusqueda(){}
    
    public ResultadoBusqueda(Analisis analisis, ValorDeteccion valor){
        super(analisis);
        this.valor = valor;
    }
    
    /*
        Getters
    */
    public ValorDeteccion getValor(){
        return this.valor;
    }
    
    /*
        Setters
    */
    public void setValor(ValorDeteccion valor){
        this.valor =valor;
    }
    
    /*
        Metodos
    */
    @Override
    public boolean isAceptable(){
        return ((Busqueda) this.getAnalisis().getRequisito().getLimiteVigente()).getValorBusquedaAceptable() == valor;
    }
}
