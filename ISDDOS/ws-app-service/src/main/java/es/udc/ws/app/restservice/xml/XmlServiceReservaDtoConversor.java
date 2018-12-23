package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import es.udc.ws.app.dto.ServiceReservaDto;


public class XmlServiceReservaDtoConversor {
	
	public final static Namespace XML_NS = Namespace
            .getNamespace("http://ws.udc.es/reservas/xml");

    public static Document toResponse(ServiceReservaDto reserva)
            throws IOException {

        Element reservaElement = toXml(reserva);

        return new Document(reservaElement);
    }
    
    public static Document toXml(List<ServiceReservaDto> reserva) throws IOException {

        Element reservasElement = new Element("reservas", XML_NS);
        for (int i = 0; i < reserva.size(); i++) {
            ServiceReservaDto xmlReservaDto = reserva.get(i);
            Element reservaElement = toJDOMElement(xmlReservaDto);
            reservasElement.addContent(reservaElement);
        }

        return new Document(reservasElement);
    }
    
    public static Element toJDOMElement(ServiceReservaDto reserva) {

        Element reservaElement = new Element("reserva", XML_NS);

        if (reserva.getIdReserva() != null) {
            Element identifierElement = new Element("idReserva", XML_NS);
            identifierElement.setText(reserva.getIdReserva().toString());
            reservaElement.addContent(identifierElement);
        }
        
        Element identifierElement = new Element("idEspectaculo", XML_NS);
        identifierElement.setText(reserva.getIdEspectaculo().toString());
        reservaElement.addContent(identifierElement);
        
        Element numEntradasTotalElement = new Element("numEntradasTotal", XML_NS);
        numEntradasTotalElement.setText(Integer.toString(reserva.getNumEntradasTotal()));
        reservaElement.addContent(numEntradasTotalElement);

        Element precioElement = new Element("precio", XML_NS);
        precioElement.setText(Double.toString(reserva.getPrecio()));
        reservaElement.addContent(precioElement);

        Element emailElement = new Element("email", XML_NS);
        emailElement.setText(reserva.getEmail());
        reservaElement.addContent(emailElement);

        Element numTarjetaCreditoElement = new Element("numTarjetaCredito", XML_NS);
        numTarjetaCreditoElement.setText(reserva.getNumTarjetaCredito());
        reservaElement.addContent(numTarjetaCreditoElement);
        
        if (reserva.getFechaReserva() != null) {
            Element fechaReservaElement = calendartoElement(reserva.getFechaReserva(), "fechaReserva");
            reservaElement.addContent(fechaReservaElement);
        }
        
        Element checkElement = new Element("check", XML_NS);
        checkElement.setText(String.valueOf(reserva.getCheck()));
        reservaElement.addContent(checkElement);

        return reservaElement;
    }

    public static Element toXml(ServiceReservaDto reserva) {

        Element reservaElement = new Element("reserva", XML_NS);

        if (reserva.getIdReserva() != null) {
            Element reservaIdElement = new Element("idReserva", XML_NS);
            reservaIdElement.setText(reserva.getIdReserva().toString());
            reservaElement.addContent(reservaIdElement);
        }

        if (reserva.getIdEspectaculo() != null) {
            Element espectaculoIdElement = new Element("idEspectaculo", XML_NS);
            espectaculoIdElement.setText(reserva.getIdEspectaculo().toString());
            reservaElement.addContent(espectaculoIdElement);
        }
        
        Element emailElement = new Element("email", XML_NS);
        emailElement.setText(reserva.getEmail());
        reservaElement.addContent(emailElement);
        
        Element numTarjetaCreditoElement = new Element("numTarjetaCredito", XML_NS);
        numTarjetaCreditoElement.setText(reserva.getNumTarjetaCredito());
        reservaElement.addContent(numTarjetaCreditoElement);
        
        Element precioElement = new Element("precio", XML_NS);
        precioElement.setText(Float.toString(reserva.getPrecio()));
        reservaElement.addContent(precioElement);

        if (reserva.getFechaReserva() != null) {
            Element fechaReservaElement = calendartoElement(reserva
                    .getFechaReserva(),"fechaReserva");
            reservaElement.addContent(fechaReservaElement);
        }
        
        Element numEntradasTotalElement = new Element("numEntradasTotal", XML_NS);
       
        numEntradasTotalElement.setText(Integer.toString(reserva.getNumEntradasTotal()));
        reservaElement.addContent(numEntradasTotalElement);
 
        Element checkElement = new Element("check", XML_NS); //
        checkElement.setText(Boolean.toString(reserva.getCheck()));
        reservaElement.addContent(checkElement);

        return reservaElement;
    }

    
    private static Element calendartoElement (Calendar fecha, String tipo) {
    	Element fechaElement = new Element(tipo, XML_NS);
    	
    	String fechaStr = String.format("%02d-%02d-%04d %02d:%02d",fecha.get(Calendar.DATE), (fecha.get(Calendar.MONTH )+1),fecha.get(Calendar.YEAR),fecha.get(Calendar.HOUR_OF_DAY),fecha.get(Calendar.MINUTE));
    
    	fechaElement.setText(fechaStr);
    	
    	return fechaElement;
    
    }

}
