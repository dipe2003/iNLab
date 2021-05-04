/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.ensayos;

import controladores.microbiologia.ControladorMicrobiologia;
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
@ViewScoped
@Named
public class AltaDeEnsayo implements Serializable {

    private ControladorMicrobiologia controladorMicrobiologia;

    private String denominacion;

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void darAltaEnsayo() throws IOException {
        if (denominacion.isEmpty()) {
            MensajesWeb.MostrarError("form-alta-ensayo:input-denominacion", "Faltan datos: ", "No se introdujo Denominacion del Ensayo.");
        } else {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (controladorMicrobiologia.CrearEnsayo(denominacion) > 0) {
                context.redirect(context.getRequestContextPath() + "/microbiologia/ensayos/listaensayos.xhtml");
            } else {
                MensajesWeb.MostrarError("form-alta-ensayo:botonAltaEnsayo", "No se pudo guardar: ", "Verifica los datos ingresados o contacta con el administrador.");
            }
        }
    }

    @PostConstruct
    public void init() {
        controladorMicrobiologia = new ControladorMicrobiologia();
    }

}
