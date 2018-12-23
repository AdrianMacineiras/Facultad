package ejercicio2;

public abstract class OperacionUnaria extends Expresion{
    
    private final Expresion Operando;

    public OperacionUnaria(Expresion Operando) {
        this.Operando = Operando;
    }
    
    
    public Expresion ObtenerHijo(){
        return Operando;
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract double evalua();
    
   @Override
    public String representa(){
        return getRepresentacion().representaUnario(this);
    }
    
}
