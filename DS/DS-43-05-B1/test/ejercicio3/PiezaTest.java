package ejercicio3;

import org.junit.Test;
import static org.junit.Assert.*;

public class PiezaTest {
    
    /**
     * Test of getTipo method, of class Pieza.
     */
    @Test
    public void testGetTipo() {
        Pieza pieza1 = new Pieza (PiezasTipo.REY, PiezasColor.NEGRO);      
        Pieza pieza2 = new Pieza (PiezasTipo.PEON, PiezasColor.BLANCO);      
        assertEquals(PiezasTipo.REY, pieza1.getTipo());
        assertEquals(PiezasTipo.PEON, pieza2.getTipo());
    }

    /**
     * Test of getColor method, of class Pieza.
     */
    @Test
    public void testGetColor() {
        Pieza pieza1 = new Pieza (PiezasTipo.REY, PiezasColor.NEGRO);      
        Pieza pieza2 = new Pieza (PiezasTipo.PEON, PiezasColor.BLANCO);
        assertEquals(PiezasColor.NEGRO, pieza1.getColor());
        assertEquals(PiezasColor.BLANCO, pieza2.getColor());
    }
}