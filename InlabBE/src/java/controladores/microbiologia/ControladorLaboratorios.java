/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.microbiologia;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import modelo.microbiologia.Laboratorio;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorLaboratorios {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    private final RepositorioPersistencia<Laboratorio> repositorioLaboratorio;

    public ControladorLaboratorios() {
        repositorioLaboratorio = fabricaRepositorio.GetRepositorioLaboratorio();
    }

    public Long CrearLaboratorio(String nombre, String detalles, boolean exExterno) {
        Laboratorio laboratorio = new Laboratorio(nombre, detalles, exExterno);
        return repositorioLaboratorio.Create(laboratorio).getId();
    }

    public Long ActualizarLaboratorio(Long idLaboratorio, String nuevoNombre, String nuevoDetalle, boolean esExterno) {
        Laboratorio laboratorio = repositorioLaboratorio.Find(idLaboratorio);
        laboratorio.setNombre(nuevoNombre);
        laboratorio.setDetalles(nuevoDetalle);
        laboratorio.setEsExterno(esExterno);
        return repositorioLaboratorio.Update(laboratorio).getId();
    }

    public List<Laboratorio> ListarLaboratorios() {
        return repositorioLaboratorio.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Laboratorio ObtenerLaboratorio(Long idLaboratorio) {
        return repositorioLaboratorio.Find(idLaboratorio);
    }
    
        public Long EliminarLaboratorio(Long idLaboratorio) {
        try {
            Laboratorio laboratorio = repositorioLaboratorio.Find(idLaboratorio);
            repositorioLaboratorio.Delete(laboratorio);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar Laboratorio: " + e.getMessage());
        }
        return -1L;
    }
}
