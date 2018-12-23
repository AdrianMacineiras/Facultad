
package ejercicio2;

public class Suma extends OperacionBinaria {

    public Suma(Expresion OperandoIzq, Expresion OperandoDer) {
        super(OperandoIzq, OperandoDer);
    }

    
    @Override
    public double evalua() {
        return ObtenerHijoIzq().evalua() + ObtenerHijoDer().evalua();
    }

    @Override
    public String toString() {
        return "+";
    }


}
