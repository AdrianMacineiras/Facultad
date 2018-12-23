package ejercicio2;

public abstract class Expresion {
    
    private Representa representacion;
    
    public abstract double evalua();
    public abstract String representa();

    public Representa getRepresentacion() {
        return representacion;
    }

    public void setRepresentacion(Representa representacion) {
        this.representacion = representacion;
    }
    
}
