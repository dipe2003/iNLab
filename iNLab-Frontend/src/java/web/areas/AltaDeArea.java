/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.areas;

import controladores.muestreos.ControladorAreas;
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
public class AltaDeArea implements Serializable {

    private ControladorAreas controladorAreas;

    private String nombre;
    private boolean productiva = false;

    public String getNombre() {
        return this.nombre;
    }

    public boolean isProductiva() {
        return productiva;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProductiva(boolean productiva) {
        this.productiva = productiva;
    }

    public void darAltaArea() throws IOException {
        if (nombre.isEmpty()) {
            MensajesWeb.MostrarError("frm-alta-area:nombreArea", "Faltan Datos Obligatorios:", "No se introdujo el nombre del area a registrar.");
        } else {
            if (controladorAreas.CrearArea(nombre, productiva) > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/areas/listaareas.xhtml");
            }else{
                MensajesWeb.MostrarError("frm-alta-area:botonAltaArea", "No se Creo el Area:", "Verifica los datos ingresados o consulta con el adminitrador.");
            }
        }
    }

    @PostConstruct
    public void init() {
        controladorAreas = new ControladorAreas();
    }

}
