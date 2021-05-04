/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.muestreos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import modelo.muestreo.Area;
import modelo.muestreo.Destino;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.TipoMuestra;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
public class ControladorEspecificacionMuestras {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    private final RepositorioPersistencia<EspecificacionMuestra> repositorioEspMuestras;
    private final RepositorioPersistencia<Area> repositorioAreas;
    private final RepositorioPersistencia<Destino> repositorioDestinos;

    public ControladorEspecificacionMuestras() {
        repositorioEspMuestras = fabricaRepositorio.GetRepositorioEspecificacionesMuestra();
        repositorioAreas = fabricaRepositorio.GetRepositorioAreas();
        repositorioDestinos = fabricaRepositorio.GetRepositorioDestinos();
    }

    public EspecificacionMuestra ObtenerEspecificacionMuestra(Long idEspecificacion) {
        return repositorioEspMuestras.Find(idEspecificacion);
    }

    public Long DarDeAltaEspecificacionMuestra(Long idEspecificacionMuestra) {
        EspecificacionMuestra especificacionMuestra = repositorioEspMuestras.Find(idEspecificacionMuestra);
        especificacionMuestra.setFechaObsoleto(null);
        return repositorioEspMuestras.Update(especificacionMuestra).getId();
    }

    public Long ActualizarEspecificacionMuestra(Long idEspecificacion, String nombreNuevo, String identificacionNueva, Long idAreaNueva, Long idDestinoNuevo) {
        EspecificacionMuestra muestra = repositorioEspMuestras.Find(idEspecificacion);
        if (!muestra.getArea().getId().equals(idAreaNueva)) {
            muestra.setArea(repositorioAreas.Find(idAreaNueva));
        }
        if (!muestra.getDestino().getId().equals(idDestinoNuevo)) {
            muestra.setDestino(repositorioDestinos.Find(idDestinoNuevo));
        }
        muestra.setDenominacion(nombreNuevo);
        muestra.setIdentificacion(identificacionNueva);
        return repositorioEspMuestras.Update(muestra).getId();
    }

    public Long DarDeBajaEspecificacionMuestra(Long idEspecificacionMuestra, Date fecha) {
        EspecificacionMuestra especificacionMuestra = repositorioEspMuestras.Find(idEspecificacionMuestra);
        especificacionMuestra.setFechaObsoleto(fecha);
        return repositorioEspMuestras.Update(especificacionMuestra).getId();
    }

    public List<EspecificacionMuestra> ListarEspecificacionMuestras(boolean soloVigentes) {
        if (soloVigentes) {
            return repositorioEspMuestras.findAll()
                    .stream()
                    .filter(m -> m.isVigente() == true)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return repositorioEspMuestras.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Long CrearEspecificacionMuestra(String nombre, String identificacion, Long idArea, TipoMuestra tipoEspecificacion, Long idDestino) {
        Area area = repositorioAreas.Find(idArea);
        Destino destino = repositorioDestinos.Find(idDestino);
        EspecificacionMuestra especificacion = new EspecificacionMuestra(nombre, identificacion, area, tipoEspecificacion, destino);
        return repositorioEspMuestras.Create(especificacion).getId();
    }

    public Long EliminarEspecificacionMuestra(Long idEspecificacionMuestra) {
        try {
            EspecificacionMuestra m = repositorioEspMuestras.Find(idEspecificacionMuestra);
            repositorioEspMuestras.Delete(m);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar especificacion de Muestra: " + e.getMessage());
        }
        return -1L;
    }
//<editor-fold desc="Especificaciones de Muestra>

}
