
package ejercicio2;

public class CambioSigno extends OperacionUnaria {

    public CambioSigno(Expresion Operando) {
        super(Operando);
    }

    @Override
    public double evalua() {
        return - ObtenerHijo().evalua();
    }

    @Override
    public String toString() {
        return "+/-";
    }

   
    
}
