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
public class ResultadoRecuento extends Resultado{
    private float valor;
    
    /*
        Constructores
    */
    public ResultadoRecuento(){
        super();
    }
    public ResultadoRecuento(Analisis analisis, float valor){
        super(analisis);
        this.valor = valor;
    }
    
    /*
        Getters
    */
    public float getValor(){
        return this.valor;
    }
    
    /*
        Setters
    */

    public void setValor(float valor){
        this.valor = valor;
    }
    
    /*
        Metodo
    */
    @Override
    public boolean isAceptable(){
        return ((Recuento) this.getAnalisis().getRequisito().getLimiteVigente()).getValorRecuentoInaceptable() > valor;
    }
    
    public boolean EsMarginal(){
        Recuento recuento = (Recuento) this.getAnalisis().getRequisito().getLimiteVigente();
        if(recuento.getValorRecuentoMarginal() != 0L){
            if(this.valor>= recuento.getValorRecuentoMarginal() && this.valor <recuento.getValorRecuentoInaceptable()){
                return true;
            }
        }
        return false;
    }
}
