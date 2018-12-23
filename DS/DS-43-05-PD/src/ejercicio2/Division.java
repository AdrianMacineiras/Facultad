package ejercicio2;

public class Division extends OperacionBinaria {

    public Division(Expresion OperandoIzq, Expresion OperandoDer) {
        super(OperandoIzq, OperandoDer);
    }

    @Override
    public double evalua() {
         return ObtenerHijoIzq().evalua() / ObtenerHijoDer().evalua();
    }

    @Override
    public String toString() {
         return "/";
    }

 
}
