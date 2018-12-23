
package ejercicio2;

public class Multiplicacion extends OperacionBinaria {

    public Multiplicacion(Expresion OperandoIzq, Expresion OperandoDer) {
        super(OperandoIzq, OperandoDer);
    }

    @Override
    public double evalua() {
        return ObtenerHijoIzq().evalua() * ObtenerHijoDer().evalua();
    }

    @Override
    public String toString() {
        return "*";
    }

    
}
