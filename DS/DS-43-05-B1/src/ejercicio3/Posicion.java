package ejercicio3;
 
public class Posicion {
 
    private char Fila;
    private char Columna;
    private Pieza Pieza;

    public Posicion (char columnaChar , char filaChar) { 
        this.Fila = filaChar;
        this.Columna = columnaChar;
        this.Pieza = null;
    }
 
    public char getFila () { 
        return Fila;
    }
 
    public char getColumna () { 
        return Columna;
    }
 
    public Pieza getPieza() {
        return Pieza;
    }
 
    public void setPieza(Pieza Pieza) {
        this.Pieza = Pieza;
    }
 
     @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Posicion other = (Posicion) obj;
        if (this.Fila != other.Fila) {
            return false;
        }
        if (this.Columna != other.Columna) {
            return false;
        }
        return true;
    }
}