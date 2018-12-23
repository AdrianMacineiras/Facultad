
package ejercicio1;

public class LeyendoCaracteres implements EstrategiaCampoCSV {

    @Override
    public int analiza(AnalizadorCSV csv, int puntero, StringBuilder cadena) {
        StringBuilder aux = new StringBuilder();
        Field cadenafield;
        boolean doblescomillas = false;
         while(puntero<cadena.length()){
            if((int)cadena.charAt(puntero) != 34){
                if(cadena.charAt(puntero) == ',' || cadena.charAt(puntero) == '\n' ){
                   throw new IllegalArgumentException("ERROR: Faltan comillas finales");
                }
                aux.append(cadena.charAt(puntero));
                puntero++;
            }
            else{
                if (!doblescomillas){
                    doblescomillas = true;
                    puntero++;
                    continue;
                }
            cadenafield = new StringField(aux.toString());
            csv.añadirField(cadenafield);
            puntero++;
            break;
            }
        }
        return puntero; 
    }
    
}
