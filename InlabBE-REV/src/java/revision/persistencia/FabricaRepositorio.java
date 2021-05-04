/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.persistencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import revision.modelo.microbiologia.RevResultado;
import revision.modelo.muestreos.RevMuestreo;


/**
 *
 * @author dipe2
 */
public class FabricaRepositorio {

    public RepositorioPersistencia GetRepositorioRevResultado() {
        return new RepositorioPersistencia<>(RevResultado.class, getEntityManager());
    }
   

    public RepositorioPersistencia GetRepositorioRevMuestreo() {
        return new RepositorioPersistencia<>(RevMuestreo.class, getEntityManager());
    }   

    private EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("InlabBE-REVPU");
        return emf.createEntityManager();
    }
}
