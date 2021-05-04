/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.usuarios;

import controladores.usuario.ControladorUsuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.usuarios.ControladorSeguridad;
import modelo.usuarios.Permiso;
import modelo.usuarios.Usuario;
import web.helpers.MensajesWeb;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class ListaDeUsuarios implements Serializable {

    private ControladorUsuarios controladorUsuarios;
    private ExternalContext context;

    private List<Usuario> usuarios;
    private List<Usuario> usuariosSinFiltro;

    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(List<Usuario> value) {
        this.usuarios = value;
    }

    //<editor-fold desc="Filtros">
    private String nombreBuscar;
    private TipoUsuario tipoUsuarioSeleccionado;
    private TipoUsuario[] permisos = TipoUsuario.values();
    private Vigencia vigenciaSeleccionada;

    private boolean filtradoNombre;
    private boolean filtradoTipo;
    private boolean filtradoVigencia;

    public String getNombreBuscar() {
        return nombreBuscar;
    }

    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }

    public TipoUsuario getTipoUsuarioSeleccionado() {
        return tipoUsuarioSeleccionado;
    }

    public void setTipoUsuarioSeleccionado(TipoUsuario tipoUsuarioSeleccionado) {
        this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
    }

    public Vigencia getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(Vigencia vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public TipoUsuario[] getPermisos() {
        return permisos;
    }

    public void setPermisos(TipoUsuario[] permisos) {
        this.permisos = permisos;
    }
    
    public void darBaja(Long idUsuario) throws IOException {
        if (controladorUsuarios.DarDeBajaUsuario(idUsuario, new Date()) > 0) {
            redirigir();
        }else {
            MensajesWeb.MostrarError("form-listar-usuarios:botonDarDeBajaUsuario", "No se dio Baja:", "Contacta con el administrador.");
        }
    }

    public void restaurar(Long idUsuario) throws IOException {
        if (controladorUsuarios.DarDeAltaUsuario(idUsuario) > 0) {
            redirigir();
        }else {
            MensajesWeb.MostrarError("form-listar-usuarios:botonDarAltaUsuario", "No se dio Alta:", "Contacta con el administrador.");
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

    //<editor-fold desc="Filtros">
    public void botonBuscarNombre(String nombre) {
        if (nombre.equals("") || nombre.isEmpty()) {
            resetTodo();
        } else {
            usuarios = usuariosSinFiltro;
            if (filtradoTipo) {
                usuarios = filtrarTipo(usuarios, tipoUsuarioSeleccionado);
            }
            if (filtradoVigencia) {
                usuarios = filtrarVigencia(usuarios, vigenciaSeleccionada);
            }
            usuarios = buscarNombre(usuarios, nombre);
            filtradoNombre = true;
        }
        prepararPagina();
    }

    public void botonFiltrarTipo(TipoUsuario tipo) {
        if (tipo == TipoUsuario.Todo) {
            resetTodo();
        } else {
            usuarios = usuariosSinFiltro;
            if (filtradoNombre) {
                usuarios = buscarNombre(usuarios, nombreBuscar);
            }

            if (filtradoVigencia) {
                usuarios = filtrarVigencia(usuarios, vigenciaSeleccionada);
            }
            usuarios = filtrarTipo(usuarios, tipoUsuarioSeleccionado);
            filtradoTipo = true;
        }
        prepararPagina();
    }

    public void botonFiltrarVigencia(Vigencia vigencia) {
        if (vigencia.equals(Vigencia.Todas)) {
            resetTodo();
        } else {
            usuarios = usuariosSinFiltro;
            if (filtradoNombre) {
                usuarios = buscarNombre(usuarios, nombreBuscar);
            }

            if (filtradoTipo) {
                usuarios = filtrarTipo(usuarios, tipoUsuarioSeleccionado);
            }
            usuarios = filtrarVigencia(usuarios, vigencia);
            filtradoVigencia = true;
        }
        prepararPagina();
    }

    public void botonResetTodo() {
        usuarios = resetTodo();
        filtradoNombre = false;
        filtradoTipo = false;
        filtradoVigencia = false;
        prepararPagina();
    }

    private List buscarNombre(List<Usuario> list, String nombre) {
        return list.stream()
                .filter(a -> a.getNombre().toLowerCase().startsWith(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List resetTodo() {
        usuarios = usuariosSinFiltro.stream()
                .collect(Collectors.toList());
        nombreBuscar = "";
        tipoUsuarioSeleccionado = TipoUsuario.Todo;
        vigenciaSeleccionada = Vigencia.Todas;
        return usuarios;
    }

    private List filtrarTipo(List<Usuario> list, TipoUsuario tipo) {
        if (tipo == TipoUsuario.Todo) {
            return list;
        }
        List<Usuario> usuariosfiltrados = new ArrayList<>();
        list.stream().forEach(u -> {
            if (u.getTipo() == Permiso.valueOf(tipo.toString())) {
                usuariosfiltrados.add(u);
            }
        });
        return usuariosfiltrados;
    }

    private List filtrarVigencia(List<Usuario> list, Vigencia vigencia) {
        List<Usuario> usuariosfiltrados = new ArrayList<>();
        list.stream().forEach(u -> {
            switch (vigencia) {
                case Vigentes:
                    if (u.isVigente()) {
                        usuariosfiltrados.add(u);
                    }
                    break;

                default:
                    if (!u.isVigente()) {
                        usuariosfiltrados.add(u);
                    }
                    break;
            }
        }
        );
        return usuariosfiltrados;
    }

    public enum Vigencia {
        Todas,
        Vigentes,
        No_Vigentes;
    }

    public enum TipoUsuario {
        Todo,
        Administrador,
        Analista,
        Monitor,
        Verificador
    }

    //</editor-fold>
    //<editor-fold desc="Paginas">
    private Map<Integer, List<Usuario>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;

    public Map<Integer, List<Usuario>> getDicPaginas() {
        return dicPaginas;
    }

    public void setDicPaginas(Map<Integer, List<Usuario>> dicPaginas) {
        this.dicPaginas = dicPaginas;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual;
    }

    public List<Integer> getListaPaginas() {
        return listaPaginas;
    }

    public void setListaPaginas(List<Integer> listaPaginas) {
        this.listaPaginas = listaPaginas;
    }

    private void prepararPagina() {
        Paginator<Usuario> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(usuarios, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(usuarios, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance().getExternalContext();
        controladorUsuarios = new ControladorUsuarios();
        usuarios = controladorUsuarios.ListarUsuarios(true);

        usuariosSinFiltro = controladorUsuarios.ListarUsuarios(true);

        tipoUsuarioSeleccionado = TipoUsuario.Todo;
        vigenciaSeleccionada = ListaDeUsuarios.Vigencia.Todas;

        prepararPagina();
    }
}
