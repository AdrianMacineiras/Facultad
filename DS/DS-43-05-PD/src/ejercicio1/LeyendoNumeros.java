package ejercicio1;

public class LeyendoNumeros implements EstrategiaCampoCSV {

    @Override
    public int analiza(AnalizadorCSV csv, int puntero, StringBuilder cadena) {
        StringBuilder aux = new StringBuilder();
        Field cadenanumeros; 
        while(puntero<cadena.length()){
            if(cadena.charAt(puntero) == ',' || cadena.charAt(puntero) == '\n'){
                cadenanumeros = new NumberField(Integer.parseInt(aux.toString()));
                csv.añadirField(cadenanumeros);
                puntero++;
                break;
            }
            if((int)cadena.charAt(puntero) < 48 || (int)cadena.charAt(puntero) > 57 ){
                throw new IllegalArgumentException("ERROR: Número incorrecto");
            }
            else{
                aux.append(cadena.charAt(puntero));
            }
            puntero ++;
        }
        return puntero;    
    }
}    
