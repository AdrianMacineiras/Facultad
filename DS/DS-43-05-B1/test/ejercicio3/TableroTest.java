package ejercicio3;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class TableroTest {
    
    /**
     * Test of iniciarPartida method, of class Tablero.
     */
    @Test
    public void testCrearTablero() {
        Tablero tablero = new Tablero();
        tablero.iniciarPartida();   
        assertEquals("tcadract/pppppppp/8/8/8/8/PPPPPPPP/TCADRACT", tablero.toString()); 
    }                   
    
    /**
     * Test of iniciarPartida method, of class Tablero.
     */
    @Test
    public void testIniciarPartida() {
        Tablero tablero = new Tablero();
        tablero.mover(new Posicion('e', '2'), new Posicion('e', '4'));
        tablero.iniciarPartida();
        assertEquals("tcadract/pppppppp/8/8/8/8/PPPPPPPP/TCADRACT", tablero.toString()); 
    }
    
    /**
     * Test of buscaPiezas method, of class Tablero.
     * Este test asume que la búsqueda va por filas empezando por la posición a8
     */
    @Test
    public void testBuscaPiezas() {
        Tablero tablero = new Tablero();
        List expResult = new ArrayList(16);
        List result = tablero.buscaPiezas(PiezasTipo.PEON);
        
        //  PEONES 
        expResult.add(new Posicion('a','7'));
        expResult.add(new Posicion('b','7'));
        expResult.add(new Posicion('c','7'));
        expResult.add(new Posicion('d','7'));
        expResult.add(new Posicion('e','7'));
        expResult.add(new Posicion('f','7'));
        expResult.add(new Posicion('g','7'));
        expResult.add(new Posicion('h','7'));
        expResult.add(new Posicion('a','2'));
        expResult.add(new Posicion('b','2'));
        expResult.add(new Posicion('c','2'));
        expResult.add(new Posicion('d','2'));
        expResult.add(new Posicion('e','2'));
        expResult.add(new Posicion('f','2'));
        expResult.add(new Posicion('g','2'));
        expResult.add(new Posicion('h','2'));
        
        assertEquals(expResult,result);     
    }

    /**
     * Test of mover method, of class Tablero.
     */
    @Test
    public void testMover() {
        Posicion origen = new Posicion('a', '2');
        Posicion destino = new Posicion('b', '3');
        Tablero tablero = new Tablero();
        tablero.mover(origen, destino);
        String cadena = tablero.toString();
        assertEquals("tcadract/pppppppp/8/8/8/1P6/1PPPPPPP/TCADRACT", cadena);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testMoverSinPieza() {
        Tablero tablero = new Tablero();
        tablero.mover(new Posicion('a', '3'), new Posicion('b', '4'));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testMoverOrigenErroneo() {
        Tablero tablero = new Tablero();
        tablero.mover(new Posicion('3', 'a'), new Posicion('b', '4'));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testMoverDestinoErroneo() {
        Tablero tablero = new Tablero();
        tablero.mover(new Posicion('a', '3'), new Posicion('b', '0'));
    }
}
