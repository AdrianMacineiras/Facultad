package ejercicio1;

public class Profesor extends PDI {
    
    private int sexenio;
    
    public Profesor(String nombre, String dni, String edad) {
        super(nombre, dni, edad);
        sexenio = 0;
        nomina = 37*8*4;
    }
    
    public void concederSexenio(){
        if(sexenio < 6) sexenio++;
    }

    public int getSexenio() {
        return sexenio;
    }
    
    @Override
    public String imprimirNomina(){
        String aux;
        if(sexenio > 0){
            aux = nombre + "(Profesor con " + sexenio + " sexenios) : "+  devolverNomina() + " euros";
        }else{
            aux = nombre + "(Profesor) : "+  devolverNomina() + " euros";
        }
        return aux;
    }
    
    @Override
    public double devolverNomina() {
        return this.nomina + (sexenio * 100);
    }
    
}
