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
import modelo.microbiologia.Ensayo;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class EditarEnsayo implements Serializable {

    private ControladorMicrobiologia controladorMicrobiologia;
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
    //<editor-fold desc="Ensayo a Editar">
    private Ensayo ensayo;

    public Ensayo getEnsayo() {
        return ensayo;
    }

    public void setEnsayo(Ensayo ensayo) {
        this.ensayo = ensayo;
    }

//</editor-fold>
    public void guardar() throws IOException {
        if (denominacion.isEmpty()) {
            MensajesWeb.MostrarError("form-editar-ensayo:input-denominacion", "Faltan datos: ", "No se introdujo Denominacion del Ensayo.");
        } else {
            if (!ensayo.getDenominacion().equalsIgnoreCase(denominacion)) {
                if (controladorMicrobiologia.ActualizarEnsayo(ensayo.getId(), denominacion) > 0) {
                    context.redirect(context.getRequestContextPath() + "/microbiologia/ensayos/listaensayos.xhtml");
                }else{
                    MensajesWeb.MostrarError("form-editar-ensayo:botonEditarEnsayo", "No se pudo guardar: ", "Verifica los datos ingresados o contacta con el administrador.");
                }
            }
            
        }
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idEnsayo = 0L;
            idEnsayo = Long.valueOf(context.getRequestParameterMap().get("idensayo"));

            controladorMicrobiologia = new ControladorMicrobiologia();

            ensayo = controladorMicrobiologia.ObtenerEnsayo(idEnsayo);
            denominacion = ensayo.getDenominacion();

        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}
