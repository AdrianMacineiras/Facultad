package ejercicio4;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class MesaBillarTest {
    
    /**
     * Test of iniciaPartida method, of class MesaBillar.
     */
    @Test
    public void testIniciaPartida() {
        MesaBillar mesa = new MesaBillar();
        mesa.iniciaPartida();
        // Comprobar partida no iniciada
        assertTrue(mesa.esPartidaIniciada());
        // Comprobar cajetín vacío
        ArrayList cajetinPrueba = new ArrayList();
        assertEquals(cajetinPrueba, mesa.bolasCajetin());
        // Comprobar mesa llena
        cajetinPrueba.addAll(Arrays.asList(BolasBillar.values()));
        assertEquals(cajetinPrueba, mesa.bolasMesa());
    }

    /**
     * Test of meterBola method, of class MesaBillar.
     */
    @Test
    public void testMeterBola() {
        MesaBillar mesa = new MesaBillar();
        mesa.iniciaPartida();

        ArrayList bolasEnMesa = new ArrayList();
        ArrayList bolasEnCajetin = new ArrayList();
        bolasEnMesa.addAll(Arrays.asList(BolasBillar.values()));
        
        //Metemos una bola
        mesa.meterBola(BolasBillar.BOLA1);
        
        //Eliminamos de la mesa paralela la bola 1
        bolasEnMesa.remove(BolasBillar.BOLA1);
        
        //Añadimos al cajetín paralelo la bola 1
        bolasEnCajetin.add(BolasBillar.BOLA1);
        
        assertEquals(bolasEnMesa, mesa.bolasMesa());
        assertEquals(bolasEnCajetin, mesa.bolasCajetin());
    }

    @Test
    public void testMeterTodasBolas() {
        BolasBillar b = null;
        MesaBillar mesa = new MesaBillar();
        mesa.iniciaPartida();

        ArrayList bolasEnMesa = new ArrayList();
        ArrayList bolasEnCajetin = new ArrayList();

        //Metemos TODAS las bolas menos la blanca y la negra
        for (BolasBillar bolaBillar : BolasBillar.values()) {
            if (!(bolaBillar.equals(BolasBillar.BLANCA)) && !(bolaBillar.equals(BolasBillar.BOLA8))) {
                mesa.meterBola(bolaBillar);
            }
        }

        //Añadimos a la mesa paralela la Blanca y la Negra
        bolasEnMesa.add(BolasBillar.BLANCA);
        bolasEnMesa.add(BolasBillar.BOLA8);
                
       
        //Añadimos al cajetín paralelo todas las bolas
        bolasEnCajetin.addAll(Arrays.asList(BolasBillar.values()));
              
        //Eliminamos del cajetín paralelo la bola BLANCA y BOLA8
        bolasEnCajetin.remove(BolasBillar.BLANCA);
        bolasEnCajetin.remove(BolasBillar.BOLA8);        
        
        assertEquals(bolasEnMesa, mesa.bolasMesa());
        assertEquals(bolasEnCajetin, mesa.bolasCajetin());
    }

    /**
     * Test of esPartidaIniciada method, of class MesaBillar.
     */
    @Test
    public void testNoEsPartidaIniciada() {
        MesaBillar mesa = new MesaBillar();
        assertFalse(mesa.esPartidaIniciada());
    }
    
    /**
     * Test of esPartidaIniciada method, of class MesaBillar.
     */
    @Test
    public void testEsPartidaIniciada() {
        MesaBillar mesa = new MesaBillar();
        mesa.iniciaPartida();
        assertTrue(mesa.esPartidaIniciada());
    }    

    /**
     * Test of obtenerGanador method, of class MesaBillar.
     */
    @Test
    public void testObtenerGanadorLisas() {
        MesaBillar mesa = new MesaBillar();
        
        //Iniciar la partida
        mesa.iniciaPartida();
        
        // Meter la bola 1 LISA
        mesa.meterBola(BolasBillar.BOLA1);
                
        assertEquals(BolasBillar.TipoBolas.LISA, mesa.obtenerGanador());
    }
    
    /**
     * Test of obtenerGanador method, of class MesaBillar.
     */
    @Test
    public void testObtenerGanadorRayadas() {
        MesaBillar mesa = new MesaBillar();
        
        //Iniciar la partida
        mesa.iniciaPartida();
        
        // Meter la bola 10 RAYADA
        mesa.meterBola(BolasBillar.BOLA10);
                
        assertEquals(BolasBillar.TipoBolas.RAYADA, mesa.obtenerGanador());
    }    
    
    /**
     * Test of obtenerGanador method, of class MesaBillar.
     */
    @Test
    public void testObtenerGanadorEmpate() {
        MesaBillar mesa = new MesaBillar();
        
        //Iniciar la partida
        mesa.iniciaPartida();
        
        // Meter la bola 10 RAYADA
        mesa.meterBola(BolasBillar.BOLA10);
        
        // Meter la bola 1 LISA
        mesa.meterBola(BolasBillar.BOLA1);
                
        assertEquals(null, mesa.obtenerGanador());
    }            
}