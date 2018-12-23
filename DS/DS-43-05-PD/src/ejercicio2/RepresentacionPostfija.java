package ejercicio2;

public class RepresentacionPostfija implements Representa {

    @Override
    public String representaBinario(OperacionBinaria expresion) {
        StringBuilder aux = new StringBuilder();
        expresion.ObtenerHijoIzq().setRepresentacion(this);
        expresion.ObtenerHijoDer().setRepresentacion(this);
        aux.append(expresion.ObtenerHijoIzq().representa());
        aux.append(expresion.ObtenerHijoDer().representa());
        aux.append(expresion.toString());
        return aux.toString();
    }

    @Override
    public String representaUnario(OperacionUnaria expresion) {
        StringBuilder aux = new StringBuilder();
        expresion.ObtenerHijo().setRepresentacion(this);
        aux.append(expresion.ObtenerHijo().representa());
        aux.append(expresion.toString());
        return aux.toString();
    }

}
