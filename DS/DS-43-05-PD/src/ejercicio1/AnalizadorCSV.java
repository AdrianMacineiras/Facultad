package ejercicio1;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorCSV {
     private final List<Field> listaCSV = new ArrayList<>();
     private EstrategiaCampoCSV estrategia;
    
    public List<Field> analizadorString (StringBuilder cadena){
        int puntero = 0;
        EstrategiaCampoCSV numeros = new LeyendoNumeros();
        EstrategiaCampoCSV caracteres = new LeyendoCaracteres();
        while (puntero < cadena.length()){
            if(cadena.charAt(puntero) == ',' || cadena.charAt(puntero) == '\n'){
                 puntero++;
                 continue;
            }
            if((int)cadena.charAt(puntero) >= 48 && (int)cadena.charAt(puntero) <= 57 ){
                    setEstrategia(numeros);
                    puntero = analizaCSV(puntero,cadena);
                    continue;
            }
            if ((int)cadena.charAt(puntero) == 34){
                setEstrategia(caracteres);
                puntero = analizaCSV(puntero,cadena);
                continue;
            }
            throw new IllegalArgumentException("ERROR: Carácter no entre comillas");
        }
        return listaCSV;
    }
         
    @Override
    public String toString() {
        return "AnalizadorCSV{" + "listaCSV= \n" + listaCSV + '}';
    }
    
    public void añadirField (Field campo){
        listaCSV.add(campo);
    }
    
    public Field obtenerField(int posicion){
        return listaCSV.get(posicion);
    }
    
    public void setEstrategia(EstrategiaCampoCSV estrategia){
        this.estrategia = estrategia;
    }
    
    public int analizaCSV(int puntero, StringBuilder cadena){
        return estrategia.analiza(this,puntero,cadena);
    }
     
}
