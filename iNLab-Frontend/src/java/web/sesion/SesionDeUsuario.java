/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.sesion;

import controladores.usuario.ControladorUsuarios;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import modelo.usuarios.Permiso;
import modelo.usuarios.Usuario;

/**
 *
 * @author dipe2
 */
@Stateful
@SessionScoped
@Named
public class SesionDeUsuario implements Serializable{

    private Usuario usuarioLogueado;
    private ControladorUsuarios controladorUsuarios;
    public static int MAX_PAGINA;

    public ControladorUsuarios getControladorUsuarios() {
        return controladorUsuarios;
    }

    public void setControladorUsuarios(ControladorUsuarios controladorUsuarios) {
        this.controladorUsuarios = controladorUsuarios;
    }    

    public Usuario getUsuarioLogueado() {
        return this.usuarioLogueado;
    }
    
    public Permiso getTipoUsuario(){
        return this.usuarioLogueado.getTipo();
    }

    public boolean IniciarSesion(Long idUsuario, String password) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        usuarioLogueado = controladorUsuarios.ObtenerUsuario(idUsuario, password);
        if (usuarioLogueado != null){
            request.getSession().setAttribute("UsuarioLogueado", usuarioLogueado);
        }        
        return usuarioLogueado != null;
    }

    @PostConstruct
    public void init() {
        controladorUsuarios = new ControladorUsuarios();
        MAX_PAGINA = 10;
    }
    
    public void cerrarSesion(){
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.getSession().invalidate();
            usuarioLogueado = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/graficos/graficos.xhtml");
        }catch(Exception ex){
            System.out.println("Error al cerrar sesion: " + ex.getMessage());
        }
        
    }

}
