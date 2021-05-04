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
import modelo.muestreo.Area;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorAreas {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    private final RepositorioPersistencia<Area> repositorioAreas;

    public ControladorAreas() {
        repositorioAreas = fabricaRepositorio.GetRepositorioAreas();
    }

    public Long CrearArea(String nombre, boolean esProductiva) {
        Area area = new Area(nombre, esProductiva);
        return repositorioAreas.Create(area).getId();
    }

    public Long DarDeBajaArea(Long idArea, Date fecha) {
        Area area = repositorioAreas.Find((idArea));
        area.setFechaObsoleto(fecha);
        return repositorioAreas.Update(area).getId();
    }

    public Long ActualizarArea(Long idArea, String nuevoNombre, boolean esProductiva) {
        Area area = repositorioAreas.Find(idArea);
        area.setNombre(nuevoNombre);
        area.setEsProductiva(esProductiva);
        return repositorioAreas.Update(area).getId();
    }

    public Long DarDeAltaArea(Long idArea) {
        Area area = repositorioAreas.Find(idArea);
        area.setFechaObsoleto(null);
        return repositorioAreas.Update(area).getId();
    }

    public List<Area> ListarAreas(boolean soloVigentes) {
        if (soloVigentes) {
            return repositorioAreas.findAll()
                    .stream()
                    .filter(a -> a.isEsVigente())
                    .sorted()
                    .collect(Collectors.toList());
        }
        return repositorioAreas.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Area ObtenerArea(Long idArea) {
        return repositorioAreas.Find(idArea);
    }

    public Long EliminarArea(Long idArea) {
        try {
            Area area = repositorioAreas.Find(idArea);
            repositorioAreas.Delete(area);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar Area: " + e.getMessage());
        }
        return -1L;
    }
}
