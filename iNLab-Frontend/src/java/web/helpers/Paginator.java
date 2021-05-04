/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dipe2
 */
public class Paginator <T>{
    
    public int calcularTotalPaginas(List lista, int maxItems) {
        float tmp = ((float)lista.size()) / ((float) maxItems);
        if((tmp - (int) tmp)>0){
            tmp++;
        }
        return (int) tmp;
    }

    public Map<Integer, List<T>> llenarDicPaginas(List listaElementos, int maxItems, int cantidaPaginas) {
        Map<Integer, List<T>> paginas = new HashMap<>();
        for (int i = 1; i <= cantidaPaginas; i++) {
            for (int b = 1; b <= maxItems;b++) {
                List<T> elementosPagina = new ArrayList<>();
                Iterator it = listaElementos.listIterator();
                while (it.hasNext() && b <= maxItems) {
                    elementosPagina.add(((T) it.next()));
                    it.remove();
                    b++;
                } ;
                paginas.put(i, elementosPagina);
                break;
            }
        }
        return paginas;
    }

    public List llenarIndicePaginas(List items) {
        List<Integer> indice = new ArrayList<>();
        for (int i = 1; i <= items.size(); i++) {
            indice.add(i);
        }
        return indice;
    }

    
}
