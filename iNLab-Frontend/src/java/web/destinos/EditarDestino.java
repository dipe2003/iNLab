/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.destinos;

import controladores.microbiologia.ControladorDestinos;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.muestreo.Destino;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class EditarDestino implements Serializable {

    private ControladorDestinos controladorDestinos;
    private ExternalContext context;

    //<editor-fold desc="Datos Nuevos">
    private String denominacion;

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    //</editor-fold>
    //<editor-fold desc="Destino a Editar">
    private Destino destino;

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }
    //</editor-fold>

    public void guardar() throws IOException {
        if (denominacion.isEmpty()) {
            MensajesWeb.MostrarError("form-editar-destino:mensajes-vista", "Faltan Datos Obligatorios.", "No se introdujo el nombre del Destino a registrar.");
        } else {
            if (!destino.getDenominacion().equalsIgnoreCase(denominacion)) {
                if (controladorDestinos.ActualizarDestino(destino.getId(), denominacion) > 0) {
                    redirigir();
                }else{
                     MensajesWeb.MostrarError("form-editar-destino:mensajes-vista", "No se pudo guardar.", "Verifica los datos ingresados o contacta con el administrador.");
                }
            }
        }
    }

    private void redirigir() throws IOException {
        if (context.isResponseCommitted()) {
            context = FacesContext.getCurrentInstance().getExternalContext();
        }
        context.redirect(context.getRequestContextPath() + "/destinos/listadestinos.xhtml");
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idDestino = 0L;
            idDestino = Long.valueOf(context.getRequestParameterMap().get("iddestino"));

            controladorDestinos = new ControladorDestinos();

            destino = controladorDestinos.ObtenerDestino(idDestino);
            denominacion = destino.getDenominacion();

        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}
