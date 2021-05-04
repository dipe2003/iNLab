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
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class AltaDeDestino implements Serializable {

    private ControladorDestinos controladorDestinos;

    private String denominacion;

    public String getDenominacion() {
        return this.denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void darAltaArea() throws IOException {
        if (denominacion.isEmpty()) {
            MensajesWeb.MostrarError("form-alta-destino:nombreDestino", "Faltan Datos Obligatorios:", "No se introdujo el nombre del Destino a registrar.");
        } else {
            if (controladorDestinos.CrearDestino(denominacion) > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/destinos/listadestinos.xhtml");
            } else {
                MensajesWeb.MostrarError("form-alta-destino:botonAltaDestino", "No se pudo guardar:", "Verifica los datos ingresados o contacta con el administrador.");
            }
        }
    }

    @PostConstruct
    public void init() {
        controladorDestinos = new ControladorDestinos();
    }

}
