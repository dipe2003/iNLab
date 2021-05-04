/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.graficos;

import controladores.microbiologia.ControladorMicrobiologia;
import controladores.muestreos.ControladorMuestreos;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.microbiologia.Ensayo;
import modelo.microbiologia.Recuento;
import modelo.microbiologia.ResultadoRecuento;
import modelo.microbiologia.TipoLimite;
import modelo.muestreo.Muestreo;
import modelo.muestreo.TipoMuestra;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class Graficos implements Serializable {

    private ControladorMicrobiologia cMicro;
    private ControladorMuestreos cMuestreos;

    //<editor-fold desc="Muestreos">
    private List<Muestreo> muestreos;
    private TipoMuestra tipoMuestraSeleccionado;
    private TipoMuestra[] tiposMuestras = TipoMuestra.values();

    public List<Muestreo> getMuestreos() {
        return this.muestreos;
    }

    public void setMuestreos(List<Muestreo> muestreos) {
        this.muestreos = muestreos;
    }

    public Long totalMuestreos(TipoMuestra tipo) {
        return muestreos.stream()
                .filter(m -> m.getMuestra().getEspecificacionMuestra().getTipoMuestra() == tipo)
                .count();
    }

    public TipoMuestra getTipoMuestraSeleccionado() {
        return tipoMuestraSeleccionado;
    }

    public void setTipoMuestraSeleccionado(TipoMuestra tipoMuestraSeleccionado) {
        this.tipoMuestraSeleccionado = tipoMuestraSeleccionado;
    }

    public TipoMuestra[] getTiposMuestras() {
        return tiposMuestras;
    }

    public void setTiposMuestras(TipoMuestra[] tiposMuestras) {
        this.tiposMuestras = tiposMuestras;
    }

    //</editor-fold>
    //<editor-fold desc="Ensayos">
    private List<Ensayo> ensayos;
    private Long idEnsayoSeleccionado;
    private Ensayo ensayoSeleccionado;
    private Long totalMarginales = 0L;
    private Long totalInaceptables = 0L;
    private Long totalAceptables = 0L;

    public List<Ensayo> getEnsayos() {
        return ensayos;
    }

    public void setEnsayos(List<Ensayo> ensayos) {
        this.ensayos = ensayos;
    }

    public Long getIdEnsayoSeleccionado() {
        return idEnsayoSeleccionado;
    }

    public void setIdEnsayoSeleccionado(Long idEnsayoSeleccionado) {
        this.idEnsayoSeleccionado = idEnsayoSeleccionado;
        if (idEnsayoSeleccionado > 0) {
            setEnsayoSeleccionado(findEnsayo(idEnsayoSeleccionado));
        }
    }

    public Ensayo gtEnsayoSeleccionado() {
        return ensayoSeleccionado;
    }

    public void setEnsayoSeleccionado(Ensayo ensayoSeleccionado) {
        this.ensayoSeleccionado = ensayoSeleccionado;
    }

    private Ensayo findEnsayo(Long idEnsayo) {
        return ensayos.stream()
                .filter(e -> e.getId().equals(idEnsayo))
                .findFirst()
                .orElse(null);
    }

    public Long getTotalMarginales() {
        calcularTotalMarginales(tipoMuestraSeleccionado, idEnsayoSeleccionado);
        return this.totalMarginales;
    }

    public Long getTotalInaceptables() {
        calcularTotalInaceptables(tipoMuestraSeleccionado, idEnsayoSeleccionado);
        return totalInaceptables;
    }

    public Long getTotalAceptables() {
        calcularTotalAceptables(tipoMuestraSeleccionado, idEnsayoSeleccionado);
        return totalAceptables;
    }

    private void calcularTotalMarginales(TipoMuestra tipo, Long idEnsayo) {
        muestreos.stream()
                .filter(m -> m.getMuestra().getEspecificacionMuestra().getTipoMuestra() == tipo)
                .forEach(m -> {
                    totalMarginales += m.getAnalisis().stream()
                            .filter(a -> a.getRequisito().getLimiteVigente().getTipoLimite() == TipoLimite.Recuento
                            && a.getRequisito().getEnsayo().getId().equals(idEnsayo)
                            && ((ResultadoRecuento) a.getResultado()).EsMarginal())
                            .count();
                });
    }

    private void calcularTotalInaceptables(TipoMuestra tipo, Long idEnsayo) {
        muestreos.stream()
                .filter(m -> m.getMuestra().getEspecificacionMuestra().getTipoMuestra() == tipo)
                .forEach(m -> {
                    totalInaceptables += m.getAnalisis().stream()
                            .filter(a -> a.getRequisito().getLimiteVigente().getTipoLimite() == TipoLimite.Recuento
                            && a.getRequisito().getEnsayo().getId().equals(idEnsayo)
                            && !((ResultadoRecuento) a.getResultado()).isAceptable())
                            .count();
                });
    }

    private void calcularTotalAceptables(TipoMuestra tipo, Long idEnsayo) {
        muestreos.stream()
                .filter(m -> m.getMuestra().getEspecificacionMuestra().getTipoMuestra() == tipo)
                .forEach(m -> {
                    totalAceptables += m.getAnalisis().stream()
                            .filter(a -> a.getRequisito().getLimiteVigente().getTipoLimite() == TipoLimite.Recuento
                            && a.getRequisito().getEnsayo().getId().equals(idEnsayo)
                            && ((ResultadoRecuento) a.getResultado()).isAceptable())
                            .count();
                });
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        // muestreos
        cMuestreos = new ControladorMuestreos();
        muestreos = cMuestreos.ListarMuestreos();
        tipoMuestraSeleccionado = tiposMuestras[0];

        // ensayos
        cMicro = new ControladorMicrobiologia();
        ensayos = cMicro.ListarEnsayos();
        if (ensayos.size() > 0) {
            setIdEnsayoSeleccionado(ensayos.get(0).getId());
        }
    }

}
