package ejercicio3;

import java.util.List;
import java.util.Iterator;

public class ListaIteradorCircular<E> implements Iterator<E> {

    private final List<E> listaIterada;
    private int puntero;
    private boolean canRemove = false;
  
    protected ListaIteradorCircular(List<E> lista) {
        this.listaIterada = lista;
        this.puntero = -1;
    }
    
    @Override
    public boolean hasNext() {        
        return this.listaIterada.size() > 1;
    }
    
    @Override
    public E next() {
        E participante;
        if (this.listaIterada.size()-1 > puntero) {
            this.puntero++;
        } else {
            this.puntero = 0;
        }
        
        participante = this.listaIterada.get(this.puntero);
        this.canRemove = true;
        
        return participante;
    }
    
    @Override
    public void remove() {
        if (this.canRemove) {
            this.canRemove = false;
            this.listaIterada.remove(this.puntero);
            this.puntero--;
        } else {
            throw new IllegalStateException();
        }
    }
}