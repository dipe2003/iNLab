/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.revisions;

import revision.controladores.muestreos.ControladorRevMuestreos;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.view.ViewScoped;

import javax.inject.Named;
import revision.controladores.muestreos.ControladorRevResultados;
import revision.modelo.muestreos.RevMuestreo;
import revision.modelo.muestreos.TipoMuestra;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class ListaDeRevisionesMuestreo implements Serializable {

    private ControladorRevMuestreos controladorRevMuestreos;
    private ControladorRevResultados controladorRevResultados;
    private ExternalContext context;

    private TipoMuestra tipoMuestra;

    private List<RevMuestreo> muestreos;

    public TipoMuestra getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(TipoMuestra tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public List<RevMuestreo> getMuestreos() {
        return muestreos;
    }

    public void setMuestreos(List<RevMuestreo> muestreos) {
        this.muestreos = muestreos;
    }

    public void eliminarMuestreo(Long id) throws IOException {
        if (controladorRevMuestreos.EliminarMuestreo(id) > 0) {
            redirigir();
        }
        // TODO: redirigir error.
    }

    public void quitarEnsayo(Long idMuestro, Long idEnsayo) throws IOException {
        if (controladorRevResultados.QuitarResultado(idMuestro, idEnsayo) > 0) {
            redirigir();
        }
        // TODO: redirigir error
    }

    private void redirigir() throws IOException {
        context.redirect(context.getRequestContextPath() + "/revisiones/listarevisionesmuestreos.xhtml?tipomuestra=" + tipoMuestra);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    //<editor-fold desc="Paginas">
    private Map<Integer, List<RevMuestreo>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;

    public Map<Integer, List<RevMuestreo>> getDicPaginas() {
        return dicPaginas;
    }

    public void setDicPaginas(Map<Integer, List<RevMuestreo>> dicPaginas) {
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
        Paginator<RevMuestreo> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(muestreos, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(muestreos, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            tipoMuestra = TipoMuestra.valueOf(context.getRequestParameterMap().get("tipomuestra"));
            controladorRevMuestreos = new ControladorRevMuestreos();
            controladorRevResultados = new ControladorRevResultados();
            muestreos = new ArrayList<>();

            if (tipoMuestra.toString().equalsIgnoreCase("Producto")) {
                muestreos = controladorRevMuestreos.ListarRevMuestreos().stream()
                        .filter(m->m.getClass().getSimpleName().equalsIgnoreCase("RevMuestreoProducto"))
                        .toList();
            } else {
                muestreos = controladorRevMuestreos.ListarRevMuestreos().stream()
                        .filter(m->!m.getClass().getSimpleName().equalsIgnoreCase("RevMuestreoProducto"))
                        .toList();
            }

            prepararPagina();

        } catch (Exception ex) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/errores/error_404.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(ListaDeRevisionesMuestreo.class.getName()).log(Level.SEVERE, null, ex1);
            }
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

}
