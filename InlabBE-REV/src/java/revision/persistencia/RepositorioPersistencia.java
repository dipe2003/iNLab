/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author dipe2
 */
public class RepositorioPersistencia<T> implements IRepositorioPersistencia<T> {

    protected EntityManager entityManager;
    private Class<T> type;

    public RepositorioPersistencia(Class<T> entityClass, EntityManager entityManager) {
        this.type = entityClass;
        this.entityManager = entityManager;
    }

    @Override
    public T Find(final Object id) {
        return (T) entityManager.find(type, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> root = criteriaQuery.from(type);
        criteriaQuery.select(root);
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public void Delete(final Object objeto) {
        try {
            entityManager.getTransaction()
                    .begin();
            entityManager.merge(objeto);
            entityManager.remove(objeto);
            entityManager.getTransaction()
                    .commit();              
        } catch (Exception e) {
        }
    }

    @Override
    public T Update(final T t) {
        try {
            entityManager.getTransaction()
                    .begin();
            entityManager.merge(t);
            entityManager.getTransaction()
                    .commit();
        } catch (Exception e) {
        }
        return t;
    }

    @Override
    public T Create(final T t) {
        try {
            entityManager.getTransaction()
                    .begin();
            entityManager.persist(t);
            entityManager.getTransaction()
                    .commit();
        } catch (Exception e) {
        }
        return t;
    }

}
