/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.helpers;

import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author dipe2
 */
public class MensajesWeb {

    public static void MostrarError(String idComponente, String textoResumen, String textoDetalle) {
        FacesContext.getCurrentInstance().addMessage(idComponente, new FacesMessage(FacesMessage.SEVERITY_ERROR, textoResumen, textoDetalle));
        FacesContext.getCurrentInstance().renderResponse();
    }

    public static void MostrarInfo(String idComponente, String textoResumen, String textoDetalle) {
        FacesContext.getCurrentInstance().addMessage(idComponente, new FacesMessage(FacesMessage.SEVERITY_INFO, textoResumen, textoDetalle));
        FacesContext.getCurrentInstance().renderResponse();
    }

    public static void MostrarAdvertencia(String idComponente, String textoResumen, String textoDetalle) {
        FacesContext.getCurrentInstance().addMessage(idComponente, new FacesMessage(FacesMessage.SEVERITY_WARN, textoResumen, textoDetalle));
        FacesContext.getCurrentInstance().renderResponse();
    }

}
