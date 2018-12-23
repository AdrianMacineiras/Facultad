package ejercicio3;
 
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;
 

public class ConcursoTest {
     
   @Test
    public void testConcursanteGanadorCircular() {
        Concurso instance = new Concurso();
        int expResult = 4;
        int result;
        result = instance.concursanteGanador(3, 5, instance.iteradorCircular());
        assertEquals(expResult,result);
    }
    
    @Test
    public void testConcursanteGanadorCircular2() {
        Concurso instance = new Concurso();
        int expResult = 1;
        int result;
        result = instance.concursanteGanador(4, 9, instance.iteradorCircular());
        assertEquals(expResult,result);
    }
    
    @Test
    public void testConcursanteGanadorRebote() {
        Concurso instance = new Concurso();
        int expResult = 1;
        int result;
        result = instance.concursanteGanador(3, 5, instance.iteradorRebote());
        assertEquals(expResult,result);
    }
    
    @Test
    public void testConcursanteGanadorRebote2() {
        Concurso instance = new Concurso();
        int expResult = 3;
        int result;
        result = instance.concursanteGanador(4, 9, instance.iteradorRebote());
        assertEquals(expResult,result);
    }
    
    @Test (expected = Exception.class)
    public void testRemoveExceptionRebote(){
        ArrayList<String> concursantes = new ArrayList<>();
        concursantes.add("David");
        concursantes.add("Elena");
        Iterator prueba = new ListaIteradorRebote(concursantes);
        prueba.remove();
    }
    
    @Test (expected = Exception.class)
    public void testRemoveExceptionCircular(){
        ArrayList<String> concursantes = new ArrayList<>();
        concursantes.add("David");
        concursantes.add("Elena");
        Iterator prueba = new ListaIteradorCircular(concursantes);
        prueba.remove();
    }
 
    @Test (expected = Exception.class)
    public void testRemoveExceptionReboteNext(){
        ArrayList<String> concursantes = new ArrayList<>();
        concursantes.add("David");
        concursantes.add("Elena");
        Iterator prueba = new ListaIteradorRebote(concursantes);
        prueba.next();
        prueba.remove();
        prueba.remove();
    } 
    
    @Test (expected = Exception.class)
    public void testRemoveExceptionCircularNext(){
        ArrayList<String> concursantes = new ArrayList<>();
        concursantes.add("David");
        concursantes.add("Elena");
        Iterator prueba = new ListaIteradorCircular(concursantes);
        prueba.next();
        prueba.remove();
        prueba.remove();
    } 
    
    
}