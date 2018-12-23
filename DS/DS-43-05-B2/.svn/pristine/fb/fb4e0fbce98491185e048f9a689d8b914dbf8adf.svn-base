
package ejercicio2;

import java.util.Comparator;

public class Compara2 implements Comparator<Cancion> {

    @Override
    public int compare(Cancion o1, Cancion o2) {
       int resultado = o1.getEstilo().compareToIgnoreCase(o2.getEstilo());
       if (resultado == 0) resultado = o1.getTitulo().compareToIgnoreCase(o2.getTitulo());
       return resultado;
    }
    
}
