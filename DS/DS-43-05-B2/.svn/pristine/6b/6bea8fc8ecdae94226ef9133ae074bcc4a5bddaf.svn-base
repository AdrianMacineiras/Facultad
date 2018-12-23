package ejercicio3;

import java.util.ArrayList;
import java.util.Iterator;

public class Concurso{
    
    private final ArrayList<Integer> concursantes = new ArrayList<>();

   public int concursanteGanador(int k, int n, Iterator iterador){
        Integer i;
        for (i=1; i<=n; i++){
            concursantes.add(i);
        }       
        while (iterador.hasNext()){                  
            for (i=1; i<=k; i++){
            iterador.next();
            }
            iterador.remove();           
            }     
        return concursantes.get(0);    
   }
    
   public Iterator iteradorRebote(){
       Iterator it = new ListaIteradorRebote(concursantes);
       return it;
   }
    
   public Iterator iteradorCircular(){
       Iterator it = new ListaIteradorCircular(concursantes);
       return it;
   }
    
   
}
