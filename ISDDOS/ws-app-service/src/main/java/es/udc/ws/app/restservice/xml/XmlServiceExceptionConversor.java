package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.util.Calendar;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import es.udc.ws.app.model.espectaculoservice.exceptions.BankCardNonValidException;
import es.udc.ws.app.model.espectaculoservice.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.model.espectaculoservice.exceptions.FechaLimiteException;
import es.udc.ws.app.model.espectaculoservice.exceptions.InvalidPriceException;
import es.udc.ws.app.model.espectaculoservice.exceptions.NumEntradasException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class XmlServiceExceptionConversor {

    public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/espectaculos/xml");

    public static Document toInputValidationExceptionXml(InputValidationException ex) throws IOException {

        Element exceptionElement = new Element("InputValidationException", XML_NS);

        Element messageElement = new Element("message", XML_NS);
        messageElement.setText(ex.getMessage());
        exceptionElement.addContent(messageElement);

        return new Document(exceptionElement);
    }

    public static Document toInstanceNotFoundException(InstanceNotFoundException ex) throws IOException {

        Element exceptionElement = new Element("InstanceNotFoundException", XML_NS);

        if (ex.getInstanceId() != null) {
            Element instanceIdElement = new Element("instanceId", XML_NS);
            instanceIdElement.setText(ex.getInstanceId().toString());

            exceptionElement.addContent(instanceIdElement);
        }

        if (ex.getInstanceType() != null) {
            Element instanceTypeElement = new Element("instanceType", XML_NS);
            instanceTypeElement.setText(ex.getInstanceType());

            exceptionElement.addContent(instanceTypeElement);
        }
        return new Document(exceptionElement);
    }
    
    public static Document toInvalidPriceExceptionXml(InvalidPriceException ex) throws IOException {
    Element exceptionElement = new Element("InvalidPriceException", XML_NS);

		Element precioDescAntiguoElement = new Element("precioDescontadoAntiguo", XML_NS);
		precioDescAntiguoElement.setText(Float.toString(ex.getPrecioDescontadoAntiguo()));
	    exceptionElement.addContent(precioDescAntiguoElement);
	    
		Element precioDescNuevoElement = new Element("precioDescontadoNuevo", XML_NS);
		precioDescNuevoElement.setText(Float.toString(ex.getPrecioDescontadoAntiguo()));
	    exceptionElement.addContent(precioDescNuevoElement);
	
	    Element identifierElement = new Element("idEspectaculo", XML_NS);
	    identifierElement.setText(Long.toString(ex.getIdEspectaculo()));
	    exceptionElement.addContent(identifierElement);
        return new Document(exceptionElement);
    }
    
    public static Document toNumEntradasExceptionXml(NumEntradasException ex) throws IOException {
    Element exceptionElement = new Element("NumEntradasException", XML_NS);
    

    	Element numEntradasElement = new Element("numEntradasRest", XML_NS);
        numEntradasElement.setText(Integer.toString(ex.getNumEntradasRest()));
        exceptionElement.addContent(numEntradasElement);
   
        Element identifierElement = new Element("idEspectaculo", XML_NS);
        identifierElement.setText(Long.toString(ex.getIdEspectaculo()));
        exceptionElement.addContent(identifierElement);
    
    
   
        return new Document(exceptionElement);
    }
    
    public static Document toFechaLimiteExceptionXml(FechaLimiteException ex) throws IOException {
    	Element exceptionElement = new Element("NumEntradasException", XML_NS);

    	Element fechaLimReservaElement =calendartoElement(ex.getFechaReserva(),"fechaLimReserva");
        exceptionElement.addContent(fechaLimReservaElement);
        
        Element fechaReservaElement = calendartoElement(ex.getFechaReserva(),"fechaReserva");
        exceptionElement.addContent(fechaReservaElement);

        Element identifierElement = new Element("idEspectaculo", XML_NS);
        identifierElement.setText(Long.toString(ex.getIdEspectaculo()));
        exceptionElement.addContent(identifierElement);
        
        
            return new Document(exceptionElement);
     }
    
    public static Document toBookAlreadyUsedExceptionXml(BookAlreadyUsedException ex) throws IOException {
        Element exceptionElement = new Element("BookAlreadyUsedException", XML_NS);
        
	        Element identifierElement = new Element("idReserva", XML_NS);
	        identifierElement.setText(Long.toString(ex.getIdReserva()));
	        exceptionElement.addContent(identifierElement);
            return new Document(exceptionElement);
    }
        
    public static Document toBankCardNonValidExceptionXml(BankCardNonValidException ex) throws IOException {
            Element exceptionElement = new Element("BankCardNonValidException", XML_NS);
            
            Element identifierElement = new Element("idReserva", XML_NS);
            identifierElement.setText(Long.toString(ex.getIdReserva()));
            exceptionElement.addContent(identifierElement);
            
            Element numTarjetaCreditoElement = new Element("numTarjetaCredito", XML_NS);
            numTarjetaCreditoElement.setText(ex.getNumTarjetaCredito());
            exceptionElement.addContent(numTarjetaCreditoElement);
            
                return new Document(exceptionElement);
     }
   
    private static Element calendartoElement(Calendar fecha, String tipo) {
    	Element fechaElement = new Element(tipo, XML_NS);
    	
    	String fechaStr = String.format("%02d-%02d-%04d %02d:%02d",fecha.get(Calendar.DATE), (fecha.get(Calendar.MONTH )+1),fecha.get(Calendar.YEAR),fecha.get(Calendar.HOUR_OF_DAY),fecha.get(Calendar.MINUTE));
    
    	fechaElement.setText(fechaStr);
    	
    	return fechaElement;
    
    }
    
}