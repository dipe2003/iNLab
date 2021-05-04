/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia.generica;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.microbiologia.Analisis;
import modelo.microbiologia.Ensayo;
import modelo.microbiologia.Laboratorio;
import modelo.microbiologia.Requisito;
import modelo.muestreo.Area;
import modelo.muestreo.Destino;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.Muestra;
import modelo.muestreo.Muestreo;
import modelo.usuarios.Usuario;

/**
 *
 * @author dipe2
 */
public class FabricaRepositorio {

    public RepositorioPersistencia GetRepositorioUsuarios() {
        return new RepositorioPersistencia<>(Usuario.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioAnalisis() {
        return new RepositorioPersistencia<>(Analisis.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioLaboratorio() {
        return new RepositorioPersistencia<>(Laboratorio.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioEnsayos() {
        return new RepositorioPersistencia<>(Ensayo.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioDestinos() {
        return new RepositorioPersistencia<>(Destino.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioEspecificacionesMuestra() {
        return new RepositorioPersistencia<>(EspecificacionMuestra.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioMuestra() {
        return new RepositorioPersistencia<>(Muestra.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioMuestreo() {
        return new RepositorioPersistencia<>(Muestreo.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioAreas() {
        return new RepositorioPersistencia<>(Area.class, getEntityManager());
    }

    public RepositorioPersistencia GetRepositorioRequisitos() {
        return new RepositorioPersistencia<>(Requisito.class, getEntityManager());
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("InlabBEPU");
        return emf.createEntityManager();
    }
}
