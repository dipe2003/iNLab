/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.destinos;

import controladores.microbiologia.ControladorDestinos;
import controladores.microbiologia.ControladorMicrobiologia;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.muestreo.Destino;
import web.helpers.MensajesWeb;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class ListaDeDestinos implements Serializable {

    private ControladorDestinos controladorDestinos;

    private List<Destino> destinos;

    public List<Destino> getDestinos() {
        return this.destinos;
    }

    public void setDestinos(List<Destino> value) {
        this.destinos = value;
    }

    //<editor-fold desc="Metodos">
    public void AgregarLimite(Long idRequisito, Long idEnsayo) {
        FacesContext context = FacesContext.getCurrentInstance();
        String url = context.getExternalContext().getRequestContextPath();
        try {
            context.getExternalContext().redirect(url + "/microbiologia/requisitos/altalimite.xhtml?idreq=" + idRequisito + "&idens=" + idEnsayo);
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
        }
    }

    public void AgregarRequisito(Long idDestino) {
        FacesContext context = FacesContext.getCurrentInstance();
        String url = context.getExternalContext().getRequestContextPath();
        try {
            context.getExternalContext().redirect(url + "/microbiologia/requisitos/altarequisitodestino.xhtml?iddestino=" + idDestino);
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            System.out.println("Error al eliminar Requisito: " + ex.getMessage());
        }
    }

    public void eliminarRequisito(Long idRequisito, Long idEnsayo) throws IOException {
        if (idRequisito > 0) {
            if (new ControladorMicrobiologia().EliminarRequisito(idRequisito, idEnsayo) > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/destinos/listadestinos.xhtml");
                FacesContext.getCurrentInstance().renderResponse();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                MensajesWeb.MostrarError("form-lista-destinos:botonEliminarRequisito", "Erorr: ", "No se pudo eliminar el Requisito seleccionado.");
            }
        } else {
            MensajesWeb.MostrarError("form-lista-destinos:botonEliminarRequisito", "Erorr: ", "No se pudo eliminar el Ensayo seleccionado");
        }
    }

    public void eliminarDestino(Long idDestino) throws IOException {
        if (idDestino > 0) {
            if (controladorDestinos.EliminarDestino(idDestino) > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/destinos/listadestinos.xhtml");
                FacesContext.getCurrentInstance().renderResponse();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                MensajesWeb.MostrarError("form-lista-destinos:botonEliminar", "Erorr: ", "No se pudo eliminar el Destino seleccionado.");
            }
        } else {
            MensajesWeb.MostrarError("form-lista-destinos:botonEliminar", "Erorr: ", "No se pudo eliminar el Destino seleccionado");
        }
    }

    //</editor-fold>
    //<editor-fold desc="Filtros">
    private List<Destino> destinosSinFiltro;
    private String nombreBuscar;

    public String getNombreBuscar() {
        return nombreBuscar;
    }

    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }

    public void botonBuscarNombre(String nombre) {
        if (nombre.equals("") || nombre.isEmpty()) {
            resetTodo();
        } else {
            destinos = buscarNombre(destinosSinFiltro, nombre);
        }
        prepararPagina();
    }

    public void botonResetTodo() {
        destinos = resetTodo();
        prepararPagina();
    }

    private List buscarNombre(List<Destino> list, String nombre) {
        return list.stream()
                .filter(d -> d.getDenominacion().toLowerCase().startsWith(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List resetTodo() {
        destinos = destinosSinFiltro.stream()
                .collect(Collectors.toList());
        nombreBuscar = "";
        return destinos;
    }

    //</editor-fold>
    //<editor-fold desc="Paginas">
    private Map<Integer, List<Destino>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;

    public Map<Integer, List<Destino>> getDicPaginas() {
        return dicPaginas;
    }

    public void setDicPaginas(Map<Integer, List<Destino>> dicPaginas) {
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
        Paginator<Destino> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(destinos, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(destinos, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        controladorDestinos = new ControladorDestinos();
        destinos = controladorDestinos.ListarDestinos();
        destinosSinFiltro = controladorDestinos.ListarDestinos();
        prepararPagina();
    }
}
