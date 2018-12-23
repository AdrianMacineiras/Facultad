package es.udc.ws.app.client.service.rest.xml;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.util.xml.exceptions.ParsingException;


public class XmlClientReservaDtoConversor {
	
    public final static Namespace XML_NS = Namespace
            .getNamespace("http://ws.udc.es/reservas/xml");

    public static ClientReservaDto toClientReservaDto(InputStream reservaXml)
            throws ParsingException {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(reservaXml);
            Element rootElement = document.getRootElement();

            return toClientReservaDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientReservaDto toClientReservaDto(Element reservaElement)
            throws ParsingException, DataConversionException,
            NumberFormatException, ParseException {
        if (!"reserva".equals(reservaElement.getName())) {
            throw new ParsingException("Unrecognized element '"
                    + reservaElement.getName() + "' ('reserva' expected)");
        }
        Element reservaIdElement = reservaElement.getChild("idReserva", XML_NS);
        Long idReserva = null;
        if (reservaIdElement != null) {
            idReserva = Long.valueOf(reservaIdElement.getTextTrim());
        }

        Element espectaculoIdElement = reservaElement.getChild("idEspectaculo", XML_NS);
        Long idEspectaculo = null;
        if (espectaculoIdElement != null) {
            idEspectaculo = Long.valueOf(espectaculoIdElement.getTextTrim());
        }
        
        String email = reservaElement.getChildTextNormalize("email", XML_NS);

        String numTarjetaCredito = reservaElement.getChildTextNormalize("numTarjetaCredito", XML_NS);
        
        int numEntradasTotal = Integer.valueOf(reservaElement.getChildTextTrim("numEntradasTotal", XML_NS));

        float precio = Float.valueOf(reservaElement.getChildTextTrim("precio", XML_NS));
       
        Calendar fechaReserva = getFecha(reservaElement.getChildText("fechaReserva", XML_NS));

        boolean check = Boolean.valueOf(reservaElement.getChildTextTrim("check", XML_NS));

        return new ClientReservaDto(idReserva, idEspectaculo, email, numTarjetaCredito,precio,
    			fechaReserva,numEntradasTotal, check);
    }
    public static List<ClientReservaDto> toClientReservaDtos(InputStream reservaXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(reservaXml); //
            Element rootElement = document.getRootElement();

            if (!"reservas".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                        + rootElement.getName() + "' ('reservas' expected)");
            }
            List<Element> children = rootElement.getChildren();
            List<ClientReservaDto> reservaDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                reservaDtos.add(toClientReservaDto(element));
            }

            return reservaDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
	
    private final static Calendar getFecha(String DateElement) throws DataConversionException, ParseException{

    	
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    	cal.setTime(sdf.parse(DateElement));
    	return cal;
    }
    
    


}
