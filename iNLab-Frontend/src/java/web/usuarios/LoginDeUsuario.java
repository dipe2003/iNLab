/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.usuarios;

import controladores.usuario.ControladorUsuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.usuarios.Usuario;
import web.helpers.MensajesWeb;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class LoginDeUsuario implements Serializable {

    @Inject
    private SesionDeUsuario sesionUsuario;

    private ControladorUsuarios controladorUsuarios;

    private List<Usuario> lstUsuarios;

    private Long usuarioSeleccionado;
    private String password;

    public List<Usuario> getLstUsuarios() {
        return this.lstUsuarios;
    }

    public void setLstUsuarios(List<Usuario> value) {
        this.lstUsuarios = value;
    }

    public Long getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Long usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public void ingresar() throws IOException {
        if (comprobarDatos()) {
            if (sesionUsuario.IniciarSesion(usuarioSeleccionado, password) != false) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/index.xhtml");
            } else {
               MensajesWeb.MostrarError("form-login-usuario:boton-ingresar", "Error de autenticacion: ", "Los datos ingresados no son validos.");
            }
        }

    }

    private boolean comprobarDatos() {
        boolean ok = true;
        if (password == null || password.isEmpty()) {
            MensajesWeb.MostrarError("form-login-usuario:input-password", "Faltan datos: ", "No se introdujo password.");
            ok = false;
        }

        if (usuarioSeleccionado == null || usuarioSeleccionado.equals(0L)) {
            MensajesWeb.MostrarError("form-login-usuario:select-usuario", "Faltan datos: ", "No se selecciono Usuario.");
            ok = false;
        }
        return ok;
    }

    private void checkDisponibilidad() {
        if (lstUsuarios.isEmpty()) {
            try {
                String urlBase = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(urlBase + "/usuarios/altausuario.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginDeUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @PostConstruct
    public void init() {
        controladorUsuarios = new ControladorUsuarios();
        lstUsuarios = controladorUsuarios.ListarUsuarios(true);
        checkDisponibilidad();
    }
}
