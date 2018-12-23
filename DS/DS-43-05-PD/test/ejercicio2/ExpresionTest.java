package ejercicio2;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExpresionTest {
    
    
    @Test
    public void testExpresiones1string(){
        String expected = "-5+/-4+SQRT2/";
        Expresion uno = new Constante(-5);
        Expresion cambiosigno = new CambioSigno(uno);
        Expresion dos = new Constante(4);
        Expresion suma = new Suma(cambiosigno,dos);
        Expresion sqrt = new SQRT(suma);
        Expresion tres = new Constante(2);
        Expresion div = new Division(sqrt,tres);
        Representa algo = new RepresentacionPostfija();
        div.setRepresentacion(algo);
        assertEquals(expected,div.representa());
    }
    
    @Test
    public void testExpresiones1evalua(){
        double expected = 1.5;
        Expresion uno = new Constante(-5);
        Expresion cambiosigno = new CambioSigno(uno);
        Expresion dos = new Constante(4);
        Expresion suma = new Suma(cambiosigno,dos);
        Expresion sqrt = new SQRT(suma);
        Expresion tres = new Constante(2);
        Expresion div = new Division(sqrt,tres);
        assertEquals(expected,div.evalua(),0);
    }
    
    @Test
    public void testExpresiones2string(){
        String expected = "259SQRT*+";
        Expresion uno = new Constante(9);
        Expresion sqrt = new SQRT(uno);
        Expresion dos = new Constante(5);
        Expresion multiplicacion = new Multiplicacion(dos,sqrt); 
        Expresion tres = new Constante(2);
        Expresion suma = new Suma(tres,multiplicacion);
        Representa algo = new RepresentacionPostfija();
        suma.setRepresentacion(algo);
        assertEquals(expected,suma.representa());
    }
    
    @Test
    public void testExpresiones2evalua(){
        double expected = 17.0;
        Expresion uno = new Constante(9);
        Expresion sqrt = new SQRT(uno);
        Expresion dos = new Constante(5);
        Expresion multiplicacion = new Multiplicacion(dos,sqrt); 
        Expresion tres = new Constante(2);
        Expresion suma = new Suma(tres,multiplicacion);
        assertEquals(expected,suma.evalua(),0);
    }
    
    @Test
    public void testExpresiones3string(){
        String expected = "23*4-";
        Expresion uno = new Constante(2);
        Expresion dos = new Constante(3);
        Expresion multiplicacion = new Multiplicacion(uno,dos);
        Expresion tres = new Constante(4);
        Expresion resta = new Resta(multiplicacion,tres); 
        Representa algo = new RepresentacionPostfija();
        resta.setRepresentacion(algo);
        assertEquals(expected,resta.representa());
    }
    
    @Test
    public void testExpresiones3evalua(){
        double expected = 2.0;
        Expresion uno = new Constante(2);
        Expresion dos = new Constante(3);
        Expresion multiplicacion = new Multiplicacion(uno,dos);
        Expresion tres = new Constante(4);
        Expresion resta = new Resta(multiplicacion,tres); 
        assertEquals(expected,resta.evalua(),0);
    }
    
}
