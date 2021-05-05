/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.areas;

import controladores.muestreos.ControladorAreas;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.muestreo.Area;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class EditarArea implements Serializable {

    private ControladorAreas controladorAreas;
    private ExternalContext context;

    //<editor-fold desc="Datos Nuevos">
    private String nombre;
    private boolean esProductiva;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsProductiva() {
        return esProductiva;
    }

    public void setEsProductiva(boolean esProductiva) {
        this.esProductiva = esProductiva;
    }

    //</editor-fold>
    //<editor-fold desc="Area a Editar">
    private Area area;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    //</editor-fold>

    public void guardar() throws IOException {
        if (nombre.isEmpty()) {
            MensajesWeb.MostrarError("form-editar-area:mensajes-vista", "Faltan Datos Obligatorios.", "No se introdujo el nombre del area a registrar.");
        } else {
            if (!area.isEsProductiva() != esProductiva || !area.getNombre().equalsIgnoreCase(nombre)) {
                if (controladorAreas.ActualizarArea(area.getId(), nombre, esProductiva) > 0) {
                    redirigir();
                } else {
                    MensajesWeb.MostrarError("form-editar-area:mensajes-vista", "No se pudo guardar.", "Verifica los datos ingresados o contacta con el administrador.");
                }
            }
        }
    }

    public void darBaja() throws IOException {
        if (controladorAreas.DarDeBajaArea(area.getId(), new Date()) > 0) {
            redirigir();
        } else {
            MensajesWeb.MostrarError("form-editar-area:mensajes-vista", "No se dio Baja.", "Contacta con el administrador.");
        }
    }

    public void restaurar() throws IOException {
        if (controladorAreas.DarDeAltaArea(area.getId()) > 0) {
            redirigir();
        } else {
            MensajesWeb.MostrarError("form-editar-area:mensajes-vista", "No se dio Alta.", "Contacta con el administrador.");
        }
    }

    private void redirigir() throws IOException {
        if (context.isResponseCommitted()) {
            context = FacesContext.getCurrentInstance().getExternalContext();
        }
        context.redirect(context.getRequestContextPath() + "/areas/listaareas.xhtml");
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idarea = 0L;
            idarea = Long.valueOf(context.getRequestParameterMap().get("idarea"));

            controladorAreas = new ControladorAreas();

            area = controladorAreas.ObtenerArea(idarea);
            nombre = area.getNombre();
            esProductiva = area.isEsProductiva();

        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}
