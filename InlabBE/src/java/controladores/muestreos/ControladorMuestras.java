/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.muestreos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import modelo.muestreo.MuestraAmbiente;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.Muestra;
import modelo.muestreo.MuestraOperario;
import modelo.muestreo.MuestraOtra;
import modelo.muestreo.MuestraProducto;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorMuestras {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    private final RepositorioPersistencia<EspecificacionMuestra> repositorioEspMuestras;
    private final RepositorioPersistencia<Muestra> repositorioMuestras;

    public ControladorMuestras() {
        repositorioEspMuestras = fabricaRepositorio.GetRepositorioEspecificacionesMuestra();
        repositorioMuestras = fabricaRepositorio.GetRepositorioMuestra();
    }


    //</editor-fold>
    // <editor-fold desc="Muestra de Producto">
    public Long CrearMuestraProducto(Long idEspecificacionMuestra, Date fechOrigen, Date fechaProduccion, String lote) {
        EspecificacionMuestra especificacionMuestra = repositorioEspMuestras.Find(idEspecificacionMuestra);
        try {
            Muestra muestra = especificacionMuestra.CrearMuestraProducto(fechOrigen, fechaProduccion, lote);
            return repositorioMuestras.Create(muestra).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long CrearMuestraProducto(Long idEspecificacionMuestra, Date fechOrigen, Date fechaProduccion) {
        EspecificacionMuestra especificacionMuestra = repositorioEspMuestras.Find(idEspecificacionMuestra);
        try {
            Muestra muestra = especificacionMuestra.CrearMuestraProducto(fechOrigen, fechaProduccion);
            return repositorioMuestras.Create(muestra).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long ActualizarMuestraProducto(Long idMuestra, Date nuevaFechaOrigen, Date nuevaFechaProduccion, String nuevoLote) {
        MuestraProducto muestra = (MuestraProducto) repositorioMuestras.Find(idMuestra);
        muestra.setFechaOrigen(nuevaFechaOrigen);
        muestra.setFechaProduccion(nuevaFechaProduccion);
        muestra.setLote(nuevoLote);

        return repositorioMuestras.Update(muestra).getId();
    }

    public Long ActualizarMuestraProducto(Long idMuestra, Date nuevaFechaOrigen, Date nuevaFechaProduccion) {
        MuestraProducto muestra = (MuestraProducto) repositorioMuestras.Find(idMuestra);
        muestra.setFechaOrigen(nuevaFechaOrigen);
        muestra.setFechaProduccion(nuevaFechaProduccion);

        return repositorioMuestras.Update(muestra).getId();
    }

    public List<Muestra> ListarMuestrasProducto(boolean soloVigentes) {
        if (soloVigentes) {
            return repositorioMuestras.findAll()
                    .stream()
                    .filter(m -> m.getClass() == MuestraProducto.class)
                    .filter(m -> m.getEspecificacionMuestra().isVigente() == true)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return repositorioMuestras.findAll()
                .stream()
                .filter(m -> m.getClass() == MuestraProducto.class)
                .sorted()
                .collect(Collectors.toList());
    }

    //</editor-fold>
    // <editor-fold desc="Muestra de Ambiente">
    public Long CrearMuestraAmbiente(Long idEspecificacionMuestra, boolean enContactoProducto) {
        EspecificacionMuestra especificacionMuestra = repositorioEspMuestras.Find(idEspecificacionMuestra);
        try {
            Muestra muestra = especificacionMuestra.CrearMuestraAmbiente(enContactoProducto);
            return repositorioMuestras.Create(muestra).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long ActualizarMuestraAmbiente(Long idMuestra, boolean enContactoProducto) {
        MuestraAmbiente muestra = (MuestraAmbiente) repositorioMuestras.Find(idMuestra);
        muestra.setContactaProducto(enContactoProducto);

        return repositorioMuestras.Update(muestra).getId();
    }

    public List<Muestra> ListarMuestrasAmbientes(boolean soloVigentes) {
        if (soloVigentes) {
            return repositorioMuestras.findAll()
                    .stream()
                    .filter(m -> m.getClass() == MuestraAmbiente.class)
                    .filter(m -> m.getEspecificacionMuestra().isVigente() == true)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return repositorioMuestras.findAll()
                .stream()
                .filter(m -> m.getClass() == MuestraAmbiente.class)
                .sorted()
                .collect(Collectors.toList());
    }

    //</editor-fold>
    // <editor-fold desc="Muestra de Operario">
    public Long CrearMuestraOperario(Long idEspecificacionMuestra, String nombreOperario, int padron) {
        EspecificacionMuestra especificacionMuestra = repositorioEspMuestras.Find(idEspecificacionMuestra);
        try {
            Muestra muestra = especificacionMuestra.CrearMuestraOperario(nombreOperario, padron);
            return repositorioMuestras.Create(muestra).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long ActualizarMuestraOperario(Long idMuestra, String nuevoNombreOperario, int padron) {
        MuestraOperario muestra = (MuestraOperario) repositorioMuestras.Find(idMuestra);
        muestra.setPadron(padron);
        muestra.setNombre(nuevoNombreOperario);

        return repositorioMuestras.Update(muestra).getId();
    }

    public List<Muestra> ListarMuestrasOperario(boolean soloVigentes) {
        if (soloVigentes) {
            return repositorioMuestras.findAll()
                    .stream()
                    .filter(m -> m.getClass() == MuestraOperario.class)
                    .filter(m -> m.getEspecificacionMuestra().isVigente() == true)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return repositorioMuestras.findAll()
                .stream()
                .filter(m -> m.getClass() == MuestraOperario.class)
                .sorted()
                .collect(Collectors.toList());
    }

    //</editor-fold>
    // <editor-fold desc="Muestra de Otro tipo">
    public Long CrearMuestraOtra(Long idEspecificacionMuestra) {
        EspecificacionMuestra especificacionMuestra = repositorioEspMuestras.Find(idEspecificacionMuestra);
        try {
            Muestra muestra = especificacionMuestra.CrearMuestraOtra();
            return repositorioMuestras.Create(muestra).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long ActualizarOtraMuestra(Long idMuestra) {
        MuestraOtra muestra = (MuestraOtra) repositorioMuestras.Find(idMuestra);
        return repositorioMuestras.Update(muestra).getId();
    }

    public List<Muestra> ListarMuestrasOtro(boolean soloVigentes) {
        if (soloVigentes) {
            return repositorioMuestras.findAll()
                    .stream()
                    .filter(m -> m.getClass() == MuestraOtra.class)
                    .filter(m -> m.getEspecificacionMuestra().isVigente() == true)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return repositorioMuestras.findAll()
                .stream()
                .filter(m -> m.getClass() == MuestraOtra.class)
                .sorted()
                .collect(Collectors.toList());
    }

    //</editor-fold>
}
