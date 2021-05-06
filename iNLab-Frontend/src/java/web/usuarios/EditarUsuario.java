/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.usuarios;

import controladores.usuario.ControladorUsuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.usuarios.ControladorSeguridad;
import modelo.usuarios.Permiso;
import modelo.usuarios.Usuario;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class EditarUsuario implements Serializable {

    private ControladorUsuarios controladorUsuarios;
    private ExternalContext context;

    //<editor-fold desc="Usuario Editado">
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario value) {
        this.usuario = value;
    }
    //</editor-fold>

    //<editor-fold desc="Datos Nuevos">
    private String nombre;
    private List<Permiso> permisos;
    private Permiso permisoSeleccionado;

    private String password;
    private String repPassword;
    private String passwordActual;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Permiso> getPermisos() {
        return this.permisos;
    }

    public void setPermisos(List<Permiso> values) {
        this.permisos = values;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getRepPassword() {
        return repPassword;
    }

    public void setRepPassword(String repPassword) {
        this.repPassword = repPassword;
    }

    public Permiso getPermisoSeleccionado() {
        return permisoSeleccionado;
    }

    public void setPermisoSeleccionado(Permiso permisoSeleccionado) {
        this.permisoSeleccionado = permisoSeleccionado;
    }

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    //</editor-fold>
    //<editor-fold desc="Metodos">
    public void guardar() throws IOException {
        if (controladorUsuarios.ActualizarUsuario(usuario.getId(), nombre, permisoSeleccionado) > 0) {
            redirigir();
        }else{
           MensajesWeb.MostrarError("form-editar-usuario:mensajes-vista", "No se dio guardaron los cambios:", "Contacta con el administrador."); 
        }
    }

    public void darBaja() throws IOException {
        if (controladorUsuarios.DarDeBajaUsuario(usuario.getId(), new Date()) > 0) {
            redirigir();
        }else {
            MensajesWeb.MostrarError("form-editar-usuario:mensajes-vista", "No se dio Baja:", "Contacta con el administrador.");
        }
    }

    public void restaurar() throws IOException {
        if (controladorUsuarios.DarDeAltaUsuario(usuario.getId()) > 0) {
            redirigir();
        }else {
            MensajesWeb.MostrarError("form-editar-usuario:mensajes-vista", "No se dio Alta:", "Contacta con el administrador.");
        }
    }

    public void cambiarPassword() throws IOException {
        if (passwordActual.equals(new ControladorSeguridad().getPasswordSeguro(password, usuario.getCredencialUsuario().getPasswordKey())) && password.equals(repPassword)) {
            if (controladorUsuarios.ActualizarCredencial(usuario.getId(), password) > 0) {
                redirigir();
            }
        } else {
            MensajesWeb.MostrarError("form-editar-usuario:mensajes-vista", "No se dio pudo actualizar Password:", "Contacta con el administrador.");
        }
    }

    private void redirigir() throws IOException {
        if(context.isResponseCommitted()){
            context = FacesContext.getCurrentInstance().getExternalContext();
        }
        context.redirect(context.getRequestContextPath() + "/usuarios/listausuarios.xhtml");
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    //</editor-fold>
    @PostConstruct
    public void init() {

        try {
            Long idUsuario = 0L;
            context = FacesContext.getCurrentInstance().getExternalContext();
            idUsuario = Long.valueOf(context.getRequestParameterMap().get("iduser"));

            controladorUsuarios = new ControladorUsuarios();
            permisos = controladorUsuarios.Permisos();

            usuario = controladorUsuarios.ObtenerUsuario(idUsuario);
            nombre = usuario.getNombre();
            permisoSeleccionado = usuario.getTipo();

        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}
