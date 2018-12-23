package ejercicio3;

import java.util.List;
import java.util.Iterator;

public class IteradorCircular<E> implements Iterator<E> {
    private final List<E> listaIterada;
    private boolean borrar;
    private int puntero;
    
    public IteradorCircular(List<E> lista){
        this.listaIterada = lista;
        this.borrar = false;
        this.puntero = -1;
    }
    
    @Override
    public boolean hasNext() {
        return this.listaIterada.size()>1;
    }

    @Override
    public E next() {
         E participante;
        if (this.listaIterada.size()-1>this.puntero) {
            this.puntero++;
        } else {
            this.puntero = 0;
        }
        
        participante = this.listaIterada.get(this.puntero);
        this.borrar = true;
        
        return participante;
    }

    @Override
    public void remove() {
        if (this.borrar) {
            this.borrar = false;
            this.listaIterada.remove(this.puntero);
            this.puntero--;
        } else {
            throw new IllegalStateException();
        }
    }
}
