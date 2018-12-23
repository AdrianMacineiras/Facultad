package ejercicio2;

public abstract class OperacionBinaria extends Expresion{
    
    private final Expresion OperandoIzq;
    private final Expresion OperandoDer;

    public OperacionBinaria(Expresion OperandoIzq, Expresion OperandoDer) {
        this.OperandoIzq = OperandoIzq;
        this.OperandoDer = OperandoDer;
    }
    
    public Expresion ObtenerHijoIzq(){
        return OperandoIzq;
    }
    
    public Expresion ObtenerHijoDer(){
        return OperandoDer;
    }
    
    @Override
    public String representa(){
        return getRepresentacion().representaBinario(this);
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract double evalua();
    
}
