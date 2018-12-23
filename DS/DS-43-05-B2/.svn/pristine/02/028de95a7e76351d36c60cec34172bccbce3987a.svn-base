package ejercicio3;

import java.util.List;
import java.util.Iterator;

public class ListaIteradorRebote<E> implements Iterator<E> {

    private final List<E> listaIterada;
    private int puntero;
    private boolean canRemove = false;
    private boolean forward = true;

    protected ListaIteradorRebote(List<E> lista) {
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
        if (forward) {
            this.puntero++;
            if (puntero == this.listaIterada.size()-1) 
                forward = false;    
        } else {
            this.puntero--;
            if (puntero == 0) 
                forward = true; 
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
            if(forward)
                    this.puntero--;
            if(this.puntero == 0)
                    forward=true;
        } else {
            throw new IllegalStateException();
        }
    }
}