/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.laboratorios;

import controladores.microbiologia.ControladorLaboratorios;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class AltaDeLaboratorio implements Serializable {

    private ControladorLaboratorios controladorLaboratorios;

    private String nombre;
    private String detalles;
    private boolean esExterno;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public boolean isEsExterno() {
        return esExterno;
    }

    public void setEsExterno(boolean esExterno) {
        this.esExterno = esExterno;
    }
  

    public void darAltaLaboarorio() throws IOException {
        if (controladorLaboratorios.CrearLaboratorio(nombre, detalles, esExterno) > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() +"/microbiologia/laboratorios/listalaboratorios.xhtml");
        }
    }

    @PostConstruct
    public void init() {
        controladorLaboratorios = new ControladorLaboratorios();
    }

}
