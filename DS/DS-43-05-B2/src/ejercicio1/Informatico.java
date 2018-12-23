package ejercicio1;

public class Informatico extends PAS {

    public Informatico(String nombre, String dni, String edad) {
        super(nombre, dni, edad);
        nomina = 40*6*4;
    }

    @Override
    public double devolverNomina() {
        return nomina + (horasextras * 6);
    }
    
    @Override
    public String imprimirNomina(){
        String aux; 
        if(horasextras > 0){
            aux = nombre + "(Informatico con "+ horasextras +" horas extras) : "+  devolverNomina() + " euros";
        } else {
            aux = nombre + "(Informatico) : "+  devolverNomina() + " euros";
        }
        horasextras = 0;
        return aux;
    }
    

    
}
