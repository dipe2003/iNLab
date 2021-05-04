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
import modelo.microbiologia.Laboratorio;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class EditarLaboratorio implements Serializable {

    private ControladorLaboratorios controladorLaboratorios;
    private ExternalContext context;

    //<editor-fold desc="Datos Nuevos">
    private String nombre;
    private String detalles;
    private boolean esExterno;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsExterno() {
        return esExterno;
    }

    public void setEsExterno(boolean esExterno) {
        this.esExterno = esExterno;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    //</editor-fold>
    //<editor-fold desc="Laboratorio a Editar">
    private Laboratorio laboratorio;

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

//</editor-fold>
    public void guardar() throws IOException {
        if (nombre.isEmpty()) {
            MensajesWeb.MostrarError("form-editar-laboratorio:input-nombre-laboratorio", "Faltan Datos Obligatorios:", "No se introdujo el nombre del laboratorio a registrar.");
        } else {
            if (!laboratorio.isEsExterno() != esExterno || !laboratorio.getNombre().equalsIgnoreCase(nombre) || !laboratorio.getDetalles().equalsIgnoreCase(detalles)) {
                if (controladorLaboratorios.ActualizarLaboratorio(laboratorio.getId(), nombre, detalles, esExterno) > 0) {
                    redirigir();
                } else {
                    MensajesWeb.MostrarError("form-editar-laboratorio:botonEditarLaboratorio", "No se pudo guardar:", "Verifica los datos ingresados o contacta con el administrador.");
                }
            }
        }
    }

    private void redirigir() throws IOException {
        if (context.isResponseCommitted()) {
            context = FacesContext.getCurrentInstance().getExternalContext();
        }
        context.redirect(context.getRequestContextPath() + "/microbiologia/laboratorios/listalaboratorios.xhtml");
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idLaboratorio = 0L;
            idLaboratorio = Long.valueOf(context.getRequestParameterMap().get("idlab"));

            controladorLaboratorios = new ControladorLaboratorios();

            laboratorio = controladorLaboratorios.ObtenerLaboratorio(idLaboratorio);
            nombre = laboratorio.getNombre();
            detalles = laboratorio.getDetalles();
            esExterno = laboratorio.isEsExterno();

        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}
