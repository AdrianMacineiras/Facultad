package ejercicio1;

import java.util.ArrayList;

public class Universidad {
    protected ArrayList<Persona> personal = new ArrayList<>();  
    
    public void añadirPersonal(Persona persona){
        personal.add(persona);
    }
    
    public String imprimirNominas(){
        StringBuilder aux = new StringBuilder();
        double total = 0;
        for(int i=0; i < personal.size();i++){
            total = total + personal.get(i).devolverNomina();
            aux.append(personal.get(i).imprimirNomina());
            aux.append("\n");
        }
        aux.append("El gasto mensual de la UDC en personal es de ").append(total).append(" euros");
        return aux.toString();
    }
}