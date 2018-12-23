package ejercicio1;

public abstract class PAS extends Persona {
    
    protected int horasextras;
    
    public PAS(String nombre, String dni, String edad) {
        super(nombre, dni, edad);
        horasextras = 0;
    }

    public int getHoras_extras() {
        return horasextras;
    }
    
    public void sethorasExtras(int horas_extras){
        horasextras += horas_extras;
    }
}
