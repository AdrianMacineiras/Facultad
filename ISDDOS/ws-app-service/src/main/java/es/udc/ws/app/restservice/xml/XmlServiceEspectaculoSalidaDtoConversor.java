package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.dto.ServiceEspectaculoSalidaDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceEspectaculoSalidaDtoConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/espectaculos/xml");

    public static Document toXml(ServiceEspectaculoSalidaDto espectaculo) throws IOException {

        Element espectaculoElement = toJDOMElement(espectaculo);

        return new Document(espectaculoElement);
    }

    public static Document toXml(List<ServiceEspectaculoSalidaDto> espectaculos) throws IOException {

        Element espectaculosSalidaElement = new Element("espectaculos", XML_NS);
        for (int i = 0; i < espectaculos.size(); i++) {
            ServiceEspectaculoSalidaDto xmlEspectaculoSalidaDto = espectaculos.get(i);
            Element espectaculoSalidaElement = toJDOMElement(xmlEspectaculoSalidaDto);
            espectaculosSalidaElement.addContent(espectaculoSalidaElement);
        }

        return new Document(espectaculosSalidaElement);
    }

    public static Element toJDOMElement(ServiceEspectaculoSalidaDto espectaculo) {

        Element espectaculoSalidaElement = new Element("espectaculo", XML_NS);

        if (espectaculo.getIdEspectaculo() != null) {
            Element identifierElement = new Element("idEspectaculo", XML_NS);
            identifierElement.setText(espectaculo.getIdEspectaculo().toString());
            espectaculoSalidaElement.addContent(identifierElement);
        }
        
        Element nombreElement = new Element("nombre", XML_NS);
        nombreElement.setText(espectaculo.getNombre());
        espectaculoSalidaElement.addContent(nombreElement);
        
        Element descripcionElement = new Element("descripcion", XML_NS);
        descripcionElement.setText(espectaculo.getDescripcion());
        espectaculoSalidaElement.addContent(descripcionElement);
        
        if (espectaculo.getFechaEspectaculo() != null) {
            Element fechaEspectaculoElement = calendartoElement(espectaculo.getFechaEspectaculo(), "fechaEspectaculo");
            espectaculoSalidaElement.addContent(fechaEspectaculoElement);
        }
        
        if (espectaculo.getFechaLimReserva() != null) {
            Element fechaLimReservaElement = calendartoElement(espectaculo.getFechaLimReserva(), "fechaLimReserva");
            espectaculoSalidaElement.addContent(fechaLimReservaElement);
        }
        
        Element duracionElement = new Element("duracion", XML_NS);
        duracionElement.setText(Integer.toString(espectaculo.getDuracion()));
        espectaculoSalidaElement.addContent(duracionElement);

        Element precioRealElement = new Element("precioReal", XML_NS);
        precioRealElement.setText(Float.toString(espectaculo.getPrecioReal()));
        espectaculoSalidaElement.addContent(precioRealElement);

        Element precioDescontadoElement = new Element("precioDescontado", XML_NS);
        precioDescontadoElement.setText(Float.toString(espectaculo.getPrecioDescontado()));
        espectaculoSalidaElement.addContent(precioDescontadoElement);

        Element numEntradasRestantesElement = new Element("numEntradasRest", XML_NS);
        numEntradasRestantesElement.setText(Integer.toString(espectaculo.getNumEntradasRest()));
        espectaculoSalidaElement.addContent(numEntradasRestantesElement);

        return espectaculoSalidaElement;
    }
    
    private static Element calendartoElement (Calendar fecha, String tipo) {
    	Element fechaElement = new Element(tipo, XML_NS);
    	
    	String fechaStr = String.format("%02d-%02d-%04d %02d:%02d", fecha.get(Calendar.DATE) , (fecha.get(Calendar.MONTH )+1) , fecha.get(Calendar.YEAR) , fecha.get(Calendar.HOUR_OF_DAY),fecha.get(Calendar.MINUTE));
    
    	fechaElement.setText(fechaStr);
    	
    	return fechaElement;
    
    }
 
    

    public static ServiceEspectaculoSalidaDto toServiceEspectaculoSalidaDto(InputStream espectaculoSalidaXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(espectaculoSalidaXml);
            Element rootElement = document.getRootElement();

            return toServiceEspectaculoSalidaDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ServiceEspectaculoSalidaDto toServiceEspectaculoSalidaDto(Element espectaculoSalidaElement)
            throws ParsingException, DataConversionException, ParseException {
        if (!"espectaculo".equals(espectaculoSalidaElement.getName())) {
            throw new ParsingException("Unrecognized element '" + espectaculoSalidaElement.getName() + "' ('espectaculo' expected)");
        }
        Element identifierElement = espectaculoSalidaElement.getChild("idEspectaculo", XML_NS);
        Long identifier = null;

        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }

        String descripcion = espectaculoSalidaElement.getChildTextNormalize("descripcion", XML_NS);

        String nombre = espectaculoSalidaElement.getChildTextNormalize("nombre", XML_NS);

        int duracion = Integer.valueOf(espectaculoSalidaElement.getChildTextTrim("duracion", XML_NS));
        
        int numEntradasRest = Integer.valueOf(espectaculoSalidaElement.getChildTextTrim("numEntradasRest", XML_NS));

        float precioReal = Float.valueOf(espectaculoSalidaElement.getChildTextTrim("precioReal", XML_NS));
        float precioDescontado = Float.valueOf(espectaculoSalidaElement.getChildTextTrim("precioDescontado", XML_NS));
        
        Calendar fechaEspectaculo = getFecha(espectaculoSalidaElement.getChildText("fechaEspectaculo", XML_NS));
        Calendar fechaLimReserva = getFecha(espectaculoSalidaElement.getChildText("fechaLimReserva", XML_NS));

        return new ServiceEspectaculoSalidaDto(identifier, nombre, descripcion, fechaEspectaculo, duracion, fechaLimReserva,
        		precioReal, precioDescontado, numEntradasRest);
    }
    
    private final static Calendar getFecha(String DateElement) throws DataConversionException, ParseException {

    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    	cal.setTime(sdf.parse(DateElement));
    	return cal;
    	
    }
}
