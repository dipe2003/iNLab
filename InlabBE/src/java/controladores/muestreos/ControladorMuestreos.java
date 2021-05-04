/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.muestreos;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import modelo.muestreo.Almacenamiento;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.Muestra;
import modelo.muestreo.MuestraAmbiente;
import modelo.muestreo.MuestraOperario;
import modelo.muestreo.MuestraProducto;
import modelo.muestreo.Muestreo;
import modelo.usuarios.Usuario;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorMuestreos {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();

    private final RepositorioPersistencia<Usuario> repositorioUsuarios;
    private final RepositorioPersistencia<Muestreo> repositorioMuestreos;
    private final RepositorioPersistencia<EspecificacionMuestra> repositorioEspMuestras;

    public ControladorMuestreos() {
        repositorioUsuarios = fabricaRepositorio.GetRepositorioUsuarios();
        repositorioMuestreos = fabricaRepositorio.GetRepositorioMuestreo();
        repositorioEspMuestras = fabricaRepositorio.GetRepositorioEspecificacionesMuestra();
    }

    // <editor-fold desc="Muestreo Producto">
    public Long CrearMuestreoProducto(Date fechaMuestreo, String observaciones, boolean esRepeticion, Almacenamiento almacenamiento,
            Long idUsuarioMonitor, Long idEspecificacionMuestra, Date fechaOrigen, Date fechaProduccion, String lote) {
        try {
            Usuario usuario = repositorioUsuarios.Find(idUsuarioMonitor);
            EspecificacionMuestra especificacion = repositorioEspMuestras.Find(idEspecificacionMuestra);
            Muestra muestra = especificacion.CrearMuestraProducto(fechaOrigen, fechaProduccion, lote);
            Muestreo muestreo = new Muestreo(fechaMuestreo, observaciones, esRepeticion, almacenamiento, usuario, muestra);
            return repositorioMuestreos.Create(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long CrearMuestreoProducto(Date fechaMuestreo, String observaciones, boolean esRepeticion, Almacenamiento almacenamiento,
            Long idUsuarioMonitor, Long idEspecificacionMuestra, Date fechaOrigen, Date fechaProduccion) {
        try {
            Usuario usuario = repositorioUsuarios.Find(idUsuarioMonitor);
            EspecificacionMuestra especificacion = repositorioEspMuestras.Find(idEspecificacionMuestra);
            Muestra muestra = especificacion.CrearMuestraProducto(fechaOrigen, fechaProduccion);
            Muestreo muestreo = new Muestreo(fechaMuestreo, observaciones, esRepeticion, almacenamiento, usuario, muestra);
            return repositorioMuestreos.Create(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long ActualizarMuestreoProducto(Long idMuestreo, Date nuevaFechaMuestreo, String nuevaObservaciones, boolean esRepeticion, Almacenamiento nuevoAlmacenamiento,
            Long nuevoIdUsuarioMonitor, Date nuevaFechaOrigen, Date nuevaFechaProduccion, String lote) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        if (!Objects.equals(muestreo.getUsuarioMonitor().getId(), nuevoIdUsuarioMonitor)) {
            muestreo.setUsuarioMonitor(repositorioUsuarios.Find(nuevoIdUsuarioMonitor));
        }
        MuestraProducto muestra = (MuestraProducto) muestreo.getMuestra();
        muestra.setFechaOrigen(nuevaFechaOrigen);
        muestra.setFechaProduccion(nuevaFechaProduccion);
        muestra.setLote(lote);
        muestreo.setFechaMuestreo(nuevaFechaMuestreo);
        muestreo.setObservaciones(nuevaObservaciones);
        muestreo.setEsRepeticion(esRepeticion);
        muestreo.setAlmacenamiento(nuevoAlmacenamiento);

        return repositorioMuestreos.Update(muestreo).getId();
    }

    public Long ActualizarMuestreoProducto(Long idMuestreo, Date nuevaFechaMuestreo, String nuevaObservaciones, boolean esRepeticion, Almacenamiento nuevoAlmacenamiento,
            Long nuevoIdUsuarioMonitor, Date nuevaFechaOrigen, Date nuevaFechaProduccion) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        if (!Objects.equals(muestreo.getUsuarioMonitor().getId(), nuevoIdUsuarioMonitor)) {
            muestreo.setUsuarioMonitor(repositorioUsuarios.Find(nuevoIdUsuarioMonitor));
        }
        MuestraProducto muestra = (MuestraProducto) muestreo.getMuestra();
        muestra.setFechaOrigen(nuevaFechaOrigen);
        muestra.setFechaProduccion(nuevaFechaProduccion);
        muestreo.setFechaMuestreo(nuevaFechaMuestreo);
        muestreo.setObservaciones(nuevaObservaciones);
        muestreo.setEsRepeticion(esRepeticion);
        muestreo.setAlmacenamiento(nuevoAlmacenamiento);

        return repositorioMuestreos.Update(muestreo).getId();
    }
    // </editor-fold>

    // <editor-fold desc="Muestreo Ambiente">
    public Long CrearMuestreoAmbiente(Date fechaMuestreo, String observaciones, boolean esRepeticion, Almacenamiento almacenamiento,
            Long idUsuarioMonitor, Long idEspecificacionMuestra, boolean enContactoProducto) {
        try {
            Usuario usuario = repositorioUsuarios.Find(idUsuarioMonitor);
            EspecificacionMuestra especificacion = repositorioEspMuestras.Find(idEspecificacionMuestra);
            Muestra muestra = especificacion.CrearMuestraAmbiente(enContactoProducto);
            Muestreo muestreo = new Muestreo(fechaMuestreo, observaciones, esRepeticion, almacenamiento, usuario, muestra);
            return repositorioMuestreos.Create(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long ActualizarMuestreoAmbiente(Long idMuestreo, Date nuevaFechaMuestreo, String nuevaObservaciones, boolean esRepeticion, Almacenamiento nuevoAlmacenamiento,
            Long nuevoIdUsuarioMonitor, boolean enContactoProducto) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        if (!Objects.equals(muestreo.getUsuarioMonitor().getId(), nuevoIdUsuarioMonitor)) {
            muestreo.setUsuarioMonitor(repositorioUsuarios.Find(nuevoIdUsuarioMonitor));
        }
        MuestraAmbiente muestra = (MuestraAmbiente) muestreo.getMuestra();
        muestra.setContactaProducto(enContactoProducto);
        muestreo.setFechaMuestreo(nuevaFechaMuestreo);
        muestreo.setObservaciones(nuevaObservaciones);
        muestreo.setEsRepeticion(esRepeticion);
        muestreo.setAlmacenamiento(nuevoAlmacenamiento);

        return repositorioMuestreos.Update(muestreo).getId();
    }

    //</editor-fold>
    //<editor-fold desc="Muestra Operario">
    public Long CrearMuestreoOperario(Date fechaMuestreo, String observaciones, boolean esRepeticion, Almacenamiento almacenamiento,
            Long idUsuarioMonitor, Long idEspecificacionMuestra, String nombreOperario, int padron) {
        try {
            Usuario usuario = repositorioUsuarios.Find(idUsuarioMonitor);
            EspecificacionMuestra especificacion = repositorioEspMuestras.Find(idEspecificacionMuestra);
            Muestra muestra = especificacion.CrearMuestraOperario(nombreOperario, padron);
            Muestreo muestreo = new Muestreo(fechaMuestreo, observaciones, esRepeticion, almacenamiento, usuario, muestra);
            return repositorioMuestreos.Create(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long ActualizarMuestreoOperario(Long idMuestreo, Date nuevaFechaMuestreo, String nuevaObservaciones, boolean esRepeticion, Almacenamiento nuevoAlmacenamiento,
            Long nuevoIdUsuarioMonitor, String nuevoNombreOperario, int nuevoPadron) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        if (!Objects.equals(muestreo.getUsuarioMonitor().getId(), nuevoIdUsuarioMonitor)) {
            muestreo.setUsuarioMonitor(repositorioUsuarios.Find(nuevoIdUsuarioMonitor));
        }
        MuestraOperario muestra = (MuestraOperario) muestreo.getMuestra();
        muestra.setNombre(nuevoNombreOperario);
        muestra.setPadron(nuevoPadron);
        muestreo.setFechaMuestreo(nuevaFechaMuestreo);
        muestreo.setObservaciones(nuevaObservaciones);
        muestreo.setEsRepeticion(esRepeticion);
        muestreo.setAlmacenamiento(nuevoAlmacenamiento);

        return repositorioMuestreos.Update(muestreo).getId();
    }
    // </editor-fold>

    // <editor-fold desc="Muesttreo Otra Muestra">
    public Long CrearMuestreoOtraMuestra(Date fechaMuestreo, String observaciones, boolean esRepeticion, Almacenamiento almacenamiento,
            Long idUsuarioMonitor, Long idEspecificacionMuestra) {
        try {
            Usuario usuario = repositorioUsuarios.Find(idUsuarioMonitor);
            EspecificacionMuestra especificacion = repositorioEspMuestras.Find(idEspecificacionMuestra);
            Muestra muestra = especificacion.CrearMuestraOtra();
            Muestreo muestreo = new Muestreo(fechaMuestreo, observaciones, esRepeticion, almacenamiento, usuario, muestra);
            return repositorioMuestreos.Create(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }
    // </editor-fold>

    //<editor-fold desc="General">
    public Long ActualizarMuestreo(Long idMuestreo, Date nuevaFechaMuestreo, String nuevaObservaciones, boolean esRepeticion,
            Almacenamiento nuevoAlmacenamiento, Long nuevoIdUsuarioMonitor) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        if (!Objects.equals(muestreo.getUsuarioMonitor().getId(), nuevoIdUsuarioMonitor)) {
            muestreo.setUsuarioMonitor(repositorioUsuarios.Find(nuevoIdUsuarioMonitor));
        }
        muestreo.setFechaMuestreo(nuevaFechaMuestreo);
        muestreo.setObservaciones(nuevaObservaciones);
        muestreo.setEsRepeticion(esRepeticion);
        muestreo.setAlmacenamiento(nuevoAlmacenamiento);

        return repositorioMuestreos.Update(muestreo).getId();
    }

    public Long ActualizarMuestreo(Long idMuestreo, Long idEspecificacionMuestra) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        EspecificacionMuestra esp = repositorioEspMuestras.Find(idEspecificacionMuestra);
        esp.getMuestras().add(muestreo.getMuestra());
        muestreo.getMuestra().setEspecificacionMuestra(esp);
        return repositorioMuestreos.Update(muestreo).getId();
    }

    public List<Muestreo> ListarMuestreos() {
        return repositorioMuestreos.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Muestreo ObtenerMuestreo(Long idMuestreo) {
        return repositorioMuestreos.Find(idMuestreo);
    }
    
    public Long EliminarMuestreo(Long idMuestreo){
        try {
            Muestreo m = repositorioMuestreos.Find(idMuestreo);
            repositorioMuestreos.Delete(m);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar Muestreo: " + e.getMessage());
        }
        return -1L;
    }
    //</editor-fold>
}
