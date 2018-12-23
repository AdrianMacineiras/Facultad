package ejercicio1;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AnalizadorCSVTest {
    
       
    @Test
    public void testLectorString() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris\",66952000\n");
        List<Field> expResult;
        expResult = new ArrayList();
        List<Field> result;
        Field s1 = new StringField("País");
        expResult.add(s1);
        Field s2 = new StringField("Área");
        expResult.add(s2);
        Field s3 = new StringField("Capital");
        expResult.add(s3);
        Field s4 = new StringField("Población");
        expResult.add(s4);
        Field s5 = new StringField("España");
        expResult.add(s5);
        Field n1 = new NumberField(504645);
        expResult.add(n1);
        Field s6 = new StringField("Madrid");
        expResult.add(s6);
        Field n2 = new NumberField(46439864);
        expResult.add(n2);
        Field s7 = new StringField("Portugal");
        expResult.add(s7);
        Field n3 = new NumberField(92391);
        expResult.add(n3);
        Field s8 = new StringField("Lisboa");
        expResult.add(s8);
        Field n4 = new NumberField(10562178);
        expResult.add(n4);
        Field s9 = new StringField("Francia");
        expResult.add(s9);
        Field n5 = new NumberField(675417);
        expResult.add(n5);
        Field s10 = new StringField("Paris");
        expResult.add(s10);
        Field n6 = new NumberField(66952000);
        expResult.add(n6);       
        AnalizadorCSV instance = new AnalizadorCSV();
        result = instance.analizadorString(cadena);
        assertEquals(result,expResult);
    }
    
    //Si los leyendoNumeros o leyendoCaracteres no cumplen las condiciones de finalizacion, ni las condiciones
    //para una excepcion, no los añadimos a la ListaCSV, lo representamos en los siguientes test.    
    @Test
    public void testLectorString2() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris\",66952000");
        List<Field> expResult;
        expResult = new ArrayList();
        List<Field> result;
        Field s1 = new StringField("País");
        expResult.add(s1);
        Field s2 = new StringField("Área");
        expResult.add(s2);
        Field s3 = new StringField("Capital");
        expResult.add(s3);
        Field s4 = new StringField("Población");
        expResult.add(s4);
        Field s5 = new StringField("España");
        expResult.add(s5);
        Field n1 = new NumberField(504645);
        expResult.add(n1);
        Field s6 = new StringField("Madrid");
        expResult.add(s6);
        Field n2 = new NumberField(46439864);
        expResult.add(n2);
        Field s7 = new StringField("Portugal");
        expResult.add(s7);
        Field n3 = new NumberField(92391);
        expResult.add(n3);
        Field s8 = new StringField("Lisboa");
        expResult.add(s8);
        Field n4 = new NumberField(10562178);
        expResult.add(n4);
        Field s9 = new StringField("Francia");
        expResult.add(s9);
        Field n5 = new NumberField(675417);
        expResult.add(n5);
        Field s10 = new StringField("Paris");
        expResult.add(s10);
        AnalizadorCSV instance = new AnalizadorCSV();
        result = instance.analizadorString(cadena);
        assertEquals(result,expResult);
    }
    
    @Test
    public void testLectorString3() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris");
        List<Field> expResult;
        expResult = new ArrayList();
        List<Field> result;
        Field s1 = new StringField("País");
        expResult.add(s1);
        Field s2 = new StringField("Área");
        expResult.add(s2);
        Field s3 = new StringField("Capital");
        expResult.add(s3);
        Field s4 = new StringField("Población");
        expResult.add(s4);
        Field s5 = new StringField("España");
        expResult.add(s5);
        Field n1 = new NumberField(504645);
        expResult.add(n1);
        Field s6 = new StringField("Madrid");
        expResult.add(s6);
        Field n2 = new NumberField(46439864);
        expResult.add(n2);
        Field s7 = new StringField("Portugal");
        expResult.add(s7);
        Field n3 = new NumberField(92391);
        expResult.add(n3);
        Field s8 = new StringField("Lisboa");
        expResult.add(s8);
        Field n4 = new NumberField(10562178);
        expResult.add(n4);
        Field s9 = new StringField("Francia");
        expResult.add(s9);
        Field n5 = new NumberField(675417);
        expResult.add(n5);
        AnalizadorCSV instance = new AnalizadorCSV();
        result = instance.analizadorString(cadena);
        assertEquals(result,expResult);
    }
    
    
    @Test
    public void testtoString(){
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris");
        String result;
        String expResult = "AnalizadorCSV{listaCSV= \n" +
        "[Stringfield: País\n" +
        ", Stringfield: Área\n" +
        ", Stringfield: Capital\n" +
        ", Stringfield: Población\n" +
        ", Stringfield: España\n" +
        ", Numberfield: 504645\n" +
        ", Stringfield: Madrid\n" +
        ", Numberfield: 46439864\n" +
        ", Stringfield: Portugal\n" +
        ", Numberfield: 92391\n" +
        ", Stringfield: Lisboa\n" +
        ", Numberfield: 10562178\n" +
        ", Stringfield: Francia\n" +
        ", Numberfield: 675417\n" +
        "]}";
        AnalizadorCSV instance = new AnalizadorCSV();
        instance.analizadorString(cadena);
        System.out.println(instance.toString());
        result = instance.toString();
        assertEquals(result,expResult);
        
    }
    
    @Test
    public void testobtenerField(){
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris");
        Field campo = new StringField("Área");
        AnalizadorCSV instance = new AnalizadorCSV();
        instance.analizadorString(cadena);
        Field campo2 = instance.obtenerField(1);
        assertEquals(campo,campo2);
    }
    
    @Test
    public void testEquals1(){
        Field campo1 = null;
        Field campo2 = new StringField("Hola");
        assertFalse(campo2.equals(campo1));
    }
    
    @Test
    public void testEquals2(){
        Field campo1 = new StringField("Adios");
        assertTrue(campo1.equals(campo1));
    }
    
    @Test
    public void testEquals3(){
        Integer campo2 = 23;
        Field campo1 = new StringField("Adios");
        assertFalse(campo1.equals(campo2));
    }
    
    @Test (expected = Exception.class)
    public void testLectorStringExcepcion() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",pataca,\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris\",66952000\n");
        AnalizadorCSV instance = new AnalizadorCSV();
        instance.analizadorString(cadena);
    }
    
    @Test (expected = Exception.class)
    public void testLeyendoNumerosExcepcion() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",105\"62178\n\"Francia\",675417,\"Paris\",66952000\n");
        AnalizadorCSV instance = new AnalizadorCSV();
        instance.analizadorString(cadena);
    }
    
    @Test (expected = Exception.class)
    public void testLeyendoNumerosExcepcion2() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504Z645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris\",66952000\n");
        AnalizadorCSV instance = new AnalizadorCSV();
        instance.analizadorString(cadena);
    }
    
     @Test (expected = Exception.class)
    public void testLeyendoCaracteresExcepcion() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Población\"\n\"España\",504645,\"Madrid\",46439864\n");
        cadena.append("\"Portu,gal\",92391,\"Lisboa\",105\"62178\n\"Francia\",675417,\"Paris\",66952000\n");
        AnalizadorCSV instance = new AnalizadorCSV();
        instance.analizadorString(cadena);
    }
    
    @Test (expected = Exception.class)
    public void testLeyendoCaracteresExcepcion2() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("\"País\",\"Área\",\"Capital\",\"Pobl\nación\"\n\"España\",504Z645,\"Madrid\",46439864\n");
        cadena.append("\"Portugal\",92391,\"Lisboa\",10562178\n\"Francia\",675417,\"Paris\",66952000\n");
        AnalizadorCSV instance = new AnalizadorCSV();
        instance.analizadorString(cadena);
    }
    
}