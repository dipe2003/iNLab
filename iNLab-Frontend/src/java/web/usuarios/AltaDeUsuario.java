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
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import modelo.usuarios.Permiso;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@Named
@RequestScoped
public class AltaDeUsuario implements Serializable {

    private ControladorUsuarios controladorUsuarios;

    private List<Permiso> permisos;
    private Permiso permisoSeleccionado;

    private String nombre;
    private String password;
    private String repeticionPassword;

    public List<Permiso> getPermisos() {
        return this.permisos;
    }

    public void setPermisos(List<Permiso> values) {
        this.permisos = values;
    }

    public Permiso getPermisoSeleccionado() {
        return permisoSeleccionado;
    }

    public void setPermisoSeleccionado(Permiso permisoSeleccionado) {
        this.permisoSeleccionado = permisoSeleccionado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getRepeticionPassword() {
        return repeticionPassword;
    }

    public void setRepeticionPassword(String repeticionPassword) {
        this.repeticionPassword = repeticionPassword;
    }

    private boolean comprobarDatos(String nombre, String password, String repPassword, Permiso permisoSeleccionado) {
        boolean ok = true;
        if (nombre == null || nombre.isEmpty()) {
            MensajesWeb.MostrarError("form-alta-usuario:mensajes-vista", "Error: ", "El nombre es obligatorio.");
            ok = false;
        }
        if (password == null || repPassword == null) {
            MensajesWeb.MostrarError("form-alta-usuario:mensajes-vista", "Error: ", "Debe ingresar password y confirmarlo.");
            ok = false;
        } else {
            if (!password.equals(repPassword)) {
                MensajesWeb.MostrarError("form-alta-usuario:mensajes-vista", "Error: ", "El password y su confirmacion no coinciden.");
                ok = false;
            }
        }
        if (permisoSeleccionado == null) {
            MensajesWeb.MostrarError("form-alta-usuario:mensajes-vista", "Error: ", "No se selecciono Tipo de Usuario");
            ok = false;
        }
        return ok;
    }

    public void darAltaUsuario() throws IOException {
        if (comprobarDatos(nombre, password, repeticionPassword, permisoSeleccionado)) {
            if (controladorUsuarios.CrearUsuario(nombre, permisoSeleccionado, password) > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/usuarios/listausuarios.xhtml");
            }
        }
        MensajesWeb.MostrarError("form-alta-usuario:mensajes-vista", "Error: ", "No se pudo crear, verifica los datos ingresados o contacta con el Administrrador");
    }

    @PostConstruct
    public void init() {
        controladorUsuarios = new ControladorUsuarios();
        permisos = controladorUsuarios.Permisos();
    }

}
