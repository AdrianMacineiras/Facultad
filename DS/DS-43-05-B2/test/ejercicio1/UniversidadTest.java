package ejercicio1;
 
import org.junit.Test;
import static org.junit.Assert.*;
 
public class UniversidadTest {
     
    @Test
    public void testSexenios(){
        Profesor Manolo = new Profesor("Manolo","3141","41");
        int expResult = 3;
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        int result = Manolo.getSexenio();
        assertEquals(expResult,result);
    }
     
    @Test
    public void testExcesoSexenios(){
        Profesor Manolo = new Profesor("Manolo","3141","41");
        int expResult = 6;
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        Manolo.concederSexenio();
        int result = Manolo.getSexenio();
        assertEquals(expResult,result);
    }
     
     @Test
    public void testHorasExtras(){
        Informatico Manolo = new Informatico("Manolo","3141","41");
        int expResult = 37;
        Manolo.sethorasExtras(37);
        int result = Manolo.getHoras_extras();
        assertEquals(expResult,result);
    }
     
     @Test
    public void testHorasExtrasTrasImpresion(){
        Administrativo Jose_Juan = new Administrativo("Jose Juan","3343","81");
        int expResult = 0;
        Jose_Juan.sethorasExtras(37);
        Jose_Juan.imprimirNomina();
        int result = Jose_Juan.getHoras_extras();
        assertEquals(expResult,result);
    }
     
    @Test
    public void testImprimirNominas() {
        Universidad instance = new Universidad ();
        Informatico Marta = new Informatico("Marta", "1314","45");
        Informatico Eduardo = new Informatico("Eduardo", "4554", "27");
        Administrativo Carlos = new Administrativo("Carlos","6776","51");
        Profesor Elena = new Profesor("Elena","3141","41");
        Profesor Silvia = new Profesor("Silvia","3546","39");
        Administrativo Juan = new Administrativo("Juan","6778","42");
        Investigador Alberto = new Investigador("Alberto","13450","32");
        instance.añadirPersonal(Marta);
        instance.añadirPersonal(Eduardo);
        instance.añadirPersonal(Carlos);
        instance.añadirPersonal(Elena);
        instance.añadirPersonal(Silvia);
        instance.añadirPersonal(Alberto);
        instance.añadirPersonal(Juan);
        Elena.concederSexenio();
        Elena.concederSexenio();
        Eduardo.sethorasExtras(1);
        Carlos.sethorasExtras(2);
        Carlos.sethorasExtras(3);
        String expResult = "Marta(Informatico) : 960.0 euros"
                + "\nEduardo(Informatico con 1 horas extras) : 966.0 euros"
                + "\nCarlos(Administrativo con 5 horas extras) : 1140.0 euros"
                + "\nElena(Profesor con 2 sexenios) : 1384.0 euros"
                + "\nSilvia(Profesor) : 1184.0 euros"
                + "\nAlberto(Investigador) : 980.0 euros"
                + "\nJuan(Administrativo) : 1110.0 euros"
                + "\nEl gasto mensual de la UDC en personal es de 7724.0 euros";       
        String result = instance.imprimirNominas();       
        assertEquals(expResult, result);
    }
     
}