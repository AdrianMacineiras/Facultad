/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejercicio1;

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
public class PalindromoTest {
    
    public PalindromoTest() {
    }

    /**
     * Test of esPalindromo method, of class Palindromo.
     */
    @Test
    public void testEsPalindromo() {
        assertTrue(Palindromo.esPalindromo("radar"));
        assertTrue(Palindromo.esPalindromo("reconocer"));
        assertTrue(Palindromo.esPalindromo("a"));
        assertTrue(Palindromo.esPalindromo("aba"));
    }
    
      /**
     * Test of esPalindromo method, of class Palindromo.
     * Comprueba que una palabra es palindroma, se ignoran diferencias entre mayúsculas y minúsculas
     */
    @Test
    public void testEsPalindromoMayusculas() {
        assertTrue(Palindromo.esPalindromo("Radar"));
        assertTrue(Palindromo.esPalindromo("ReCoNocEr"));        
    }
    
    /**
     * Test of esPalindromo method, of class Palindromo.
     * Comprueba que una frase es palindroma, se ignoran los espacios en blanco
     */
    @Test
    public void testEsPalindromoEspacios() {
        assertTrue(Palindromo.esPalindromo("Dabale arroz a la zorra el abad"));
        assertTrue(Palindromo.esPalindromo("amor a roma"));
    }
    
     /**
     * Test of esPalindromo method, of class Palindromo.
     * Comprueba que una frase es palindroma, se ignoran los espacios en blanco
     */
    @Test
    public void testEsPalindromoOtrosCaracteres() {
        assertTrue(Palindromo.esPalindromo("¿Dabale -arroz- a, la _zorra_ el abad?."));
        assertTrue(Palindromo.esPalindromo("¡amor a roma!"));
    }
     /**
     * Test of esPalindromo method, of class Palindromo.
     * Comprueba que una frase NO es palindroma
     */
    @Test
    public void testNoEsPalindromo() {
        assertFalse(Palindromo.esPalindromo("Dabale arroz a el zorro el abad"));
        assertFalse(Palindromo.esPalindromo("Esto no es un palindromo"));
        assertFalse(Palindromo.esPalindromo("pi"));
    }
}

