package ejercicio3;
 
 
 
public class Pieza {
    private PiezasTipo tipo;
    private PiezasColor color;
    private Posicion pos;
     
    public Pieza (PiezasTipo tipo, PiezasColor color){
        this.tipo = tipo;
        this.color = color;
        this.pos = null;
    }
 
    public PiezasTipo getTipo() {
        return tipo;
    }
 
    public PiezasColor getColor() {
        return color;
    }
 
    public Posicion getPos() {
        return pos;
    }
 
    public void setPos(Posicion pos) {
        this.pos = pos;
    }
     
    public char letraPieza(){
        char letra = 'p'; //Por defecto PEON
          if (tipo == tipo.TORRE) letra = 't';
          if (tipo == tipo.REY) letra = 'r';
          if (tipo == tipo.DAMA) letra = 'd';
          if (tipo == tipo.ALFIL) letra = 'a';
          if (tipo == tipo.CABALLO) letra = 'c';
        if (color == color.BLANCO) letra = (char)(letra - 32);
        return letra;
    }
     
}