package ejercicio2;

import java.util.Comparator;

public class Compara1 implements Comparator<Cancion> {

    @Override
    public int compare(Cancion o1, Cancion o2) {
        int resultado = o1.getAutor().compareToIgnoreCase(o2.getAutor());
        if (resultado == 0) resultado = o1.getAlbum().compareToIgnoreCase(o2.getAlbum()); 
        if (resultado == 0) resultado = o1.getTitulo().compareToIgnoreCase(o2.getTitulo()); 
        return resultado;
    }
    
}
