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

import es.udc.ws.app.client.service.dto.UserEspectaculoDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlUserEspectaculoDtoConversor {
	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/espectaculos/xml");
	
    public static UserEspectaculoDto toClientEspectaculoDto(InputStream espectaculoXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(espectaculoXml);
            Element rootElement = document.getRootElement();

            return toClientEspectaculoDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static UserEspectaculoDto toClientEspectaculoDto(Element espectaculoElement)
            throws ParsingException, DataConversionException, ParseException {
        if (!"espectaculo".equals(espectaculoElement.getName())) {
            throw new ParsingException("Unrecognized element '" + espectaculoElement.getName() + "' ('espectaculo' expected)");
        }
        Element identifierElement = espectaculoElement.getChild("idEspectaculo", XML_NS);
        Long identifier = null;

        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }

        String descripcion = espectaculoElement.getChildTextNormalize("descripcion", XML_NS);

        String nombre = espectaculoElement.getChildTextNormalize("nombre", XML_NS);

        int duracion = Integer.valueOf(espectaculoElement.getChildTextTrim("duracion", XML_NS));
        
        int numEntradasRest = Integer.valueOf(espectaculoElement.getChildTextTrim("numEntradasRest", XML_NS));

        float precioReal = Float.valueOf(espectaculoElement.getChildTextTrim("precioReal", XML_NS));
        float precioDescontado = Float.valueOf(espectaculoElement.getChildTextTrim("precioDescontado", XML_NS));
        
        Calendar fechaEspectaculo = getFecha(espectaculoElement.getChildText("fechaEspectaculo", XML_NS));
        Calendar fechaLimReserva = getFecha(espectaculoElement.getChildText("fechaLimReserva", XML_NS));
        
        int horas = duracion/60;
        int minutos = duracion % 60;      
        Calendar fechaFin = fechaEspectaculo;      
        fechaFin.add(Calendar.MINUTE, minutos);
        fechaFin.add(Calendar.HOUR,   horas);

        return new UserEspectaculoDto(identifier, nombre, descripcion, fechaEspectaculo, fechaLimReserva,
        		precioReal, precioDescontado, numEntradasRest, fechaFin);
    }
    

    public static List<UserEspectaculoDto> toClientEspectaculoDtos(InputStream espectaculoXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(espectaculoXml);
            Element rootElement = document.getRootElement();

            if (!"espectaculos".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                        + rootElement.getName() + "' ('espectaculos' expected)");
            }
            List<Element> children = rootElement.getChildren();
            List<UserEspectaculoDto> espectaculoDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                espectaculoDtos.add(toClientEspectaculoDto(element));
            }

            return espectaculoDtos;
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
