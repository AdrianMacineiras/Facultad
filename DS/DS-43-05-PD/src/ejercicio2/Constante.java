package ejercicio2;

public class Constante extends Expresion {
    
    private final Integer constante;

    public Constante(Integer constante) {
        this.constante = constante;
    }

    @Override
    public double evalua() {
        return constante;
    }
    
    @Override
    public String representa() {
        return constante.toString();
    }
    
}
