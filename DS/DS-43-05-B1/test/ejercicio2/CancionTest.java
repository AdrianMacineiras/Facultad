/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejercicio2;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrian
 */
public class CancionTest {
    
    public CancionTest() {
    }

    /**
     * Test of getAutor method, of class Cancion.
     */
    @Test
    public void testGetAutor() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        assertEquals("U2", cancion.getAutor());
    }

    /**
     * Test of getTitulo method, of class Cancion.
     */
    @Test
    public void testGetTitulo() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        assertEquals("One", cancion.getTitulo());
    }

    /**
     * Test of getAlbum method, of class Cancion.
     */
    @Test
    public void testGetAlbum() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        assertEquals("Achtung Baby", cancion.getAlbum());
    }

    /**
     * Test of getEstilo method, of class Cancion.
     */
    @Test
    public void testGetEstilo() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        assertEquals("Rock", cancion.getEstilo());
    }

    /**
     * Test of toString method, of class Cancion.
     */
    @Test
    public void testToString() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        assertEquals("One - U2 - Achtung Baby", cancion.toString());
    }

    /**
     * Test of toString method, of class Cancion. The title is truncated.
     */
    @Test
    public void testToStringTruncate1() {
        Cancion cancion = new Cancion("Another one bites the dust", "Queen", "The Game", "Rock");
        assertEquals("Another one bites th - Queen - The Game", cancion.toString());
    }

    /**
     * Test of toString method, of class Cancion. The grupo is truncated.
     */
    @Test
    public void testToStringTruncate2() {
        Cancion cancion = new Cancion("Verano", "No me pises que llevo chanclas", "Exitos", "Pop");
        assertEquals("Verano - No me pises que llev - Exitos", cancion.toString());
    }

    /**
     * Test of toString method, of class Cancion. The album should be truncated,
     * but it is not
     */
    @Test
    public void testToStringNoTruncate4() {
        Cancion cancion = new Cancion("Músico Loco", "El último de la fila", "Nuevo pequeno catálogo de seres y estares", "Pop");
        assertNotEquals("Musico Loco - El último de la fila - Nuevo pequeno catálogo de", cancion.toString());
    }

    /**
     * Test of toString method, of class Cancion. Todo should be truncated
     */
    @Test
    public void testToStringNoTruncate6() {
        Cancion cancion = new Cancion("No me pises que llevo chanclas", "Another one bites the dust", "Nuevo pequeno catálogo de seres y estares", "Pop");
        assertNotEquals("No me pises que llev - Another one bites th - Nuevo pequeno catálogo de", cancion.toString());
    }

    /**
     * Test of equals method, of class Cancion.
     */
    @Test
    public void testEquals() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        Cancion cancion2 = new Cancion("One", "U2", "The best of U2", "Rock");
        assertEquals(cancion, cancion2);

    }

    /**
     * Test of equals method, of class Cancion.
     */
    @Test
    public void testNotEqualsDifferentAuthor() {

        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        Cancion cancion2 = new Cancion("One", "U2 and Mary J. Blige", "The best of U2", "Rock");
        assertNotEquals(cancion, cancion2);
    }

    /**
     * Test of equals method, of class Cancion.
     */
    @Test
    public void testNotEqualsDifferentTitleAuthor() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        Cancion cancion2 = new Cancion("Two", "U2 and Mary J. Blige", "The best of U2", "Rock");
        assertNotEquals(cancion, cancion2);
    }

    /**
     * Test of hashCode method, of class Cancion.
     */
    @Test
    public void testHashCode() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        Cancion cancion2 = new Cancion("One", "U2", "The best of U2", "Rock");
        assertEquals(cancion.hashCode(), cancion2.hashCode());
    }

    @Test
    public void testHashCodeDifferentAuthor() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        Cancion cancion2 = new Cancion("One", "U2 and Mary J. Blige", "Achtung Baby", "Rock");
        assertNotEquals(cancion.hashCode(), cancion2.hashCode());
    }

    @Test
    public void testHashCodeDifferentTitleAuthor() {
        Cancion cancion = new Cancion("One", "U2", "Achtung Baby", "Rock");
        Cancion cancion2 = new Cancion("Two", "U2 and Mary J. Blige", "Achtung Baby", "Rock");
        assertNotEquals(cancion.hashCode(), cancion2.hashCode());
    }
    
}
