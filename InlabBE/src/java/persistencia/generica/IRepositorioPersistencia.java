/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia.generica;

import java.util.List;

/**
 *
 * @author dipe2
 */
public interface IRepositorioPersistencia<T> {
    public T Find(T t);
    public List<T> findAll();
    public void Delete(T t);
    public T Update(T t);
    public T Create(T t);
}
