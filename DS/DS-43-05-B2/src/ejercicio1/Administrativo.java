package ejercicio1;

public class Administrativo extends PAS {
    
    public Administrativo(String nombre, String dni, String edad) {
        super(nombre, dni, edad);
        nomina = 37*7.5*4;
    }

    @Override
    public String imprimirNomina(){
        String aux; 
        if(horasextras > 0){
            aux = nombre + "(Administrativo con "+ horasextras +" horas extras) : "+  devolverNomina() + " euros";
        } else {
            aux = nombre + "(Administrativo) : "+  devolverNomina() + " euros";
        }
        horasextras = 0;
        return aux;
    }
    
    @Override
    public double devolverNomina() {
       return  nomina + (horasextras * 6);
    }
    
}
