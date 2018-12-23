package ejercicio2;

import static java.lang.Math.sqrt;

public class SQRT extends OperacionUnaria {

    public SQRT(Expresion Operando) {
        super(Operando);
    }

    @Override
    public double evalua() {
        return sqrt(ObtenerHijo().evalua());
    }

    @Override
    public String toString() {
        return "SQRT";
    }

    
}
