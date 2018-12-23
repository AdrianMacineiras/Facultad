package ejercicio3;

import java.util.List;
import java.util.Iterator;

public class IteradorRebote<E> implements Iterator<E> {
    private final List<E> listaIterada;
    private boolean borrar, hacia_atras;
    private int puntero;
    
    public IteradorRebote(List<E> lista){
        this.listaIterada = lista;
        this.borrar = false;
        this.hacia_atras = false;
        this.puntero = -1;
    }
    
    @Override
    public boolean hasNext() {
        return this.listaIterada.size()>1;
    }

    @Override
    public E next() {
        E participante;
        if (!this.hacia_atras) {
           this.puntero++;
           if (this.puntero==this.listaIterada.size()-1) this.hacia_atras=true;
        }
        else{
           this.puntero--;
           if (puntero==0) hacia_atras=false;
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
            if (!this.hacia_atras) puntero--;
            if (this.puntero==0) hacia_atras = false;
        } else {
            throw new IllegalStateException();
        }
    }

    
}