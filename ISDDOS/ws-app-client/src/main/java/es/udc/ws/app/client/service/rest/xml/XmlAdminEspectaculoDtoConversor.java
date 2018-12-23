package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
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

import es.udc.ws.app.client.service.dto.AdminEspectaculoDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlAdminEspectaculoDtoConversor {
	
	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/espectaculos/xml");

    public static Document toXml(AdminEspectaculoDto espectaculo) throws IOException {

        Element espectaculoElement = toJDOMElement(espectaculo);

        return new Document(espectaculoElement);
    }

    public static Document toXml(List<AdminEspectaculoDto> espectaculos) throws IOException {

        Element espectaculosElement = new Element("espectaculos", XML_NS);
        for (int i = 0; i < espectaculos.size(); i++) {
        	AdminEspectaculoDto xmlEspectaculoDto = espectaculos.get(i);
            Element espectaculoElement = toJDOMElement(xmlEspectaculoDto);
            espectaculosElement.addContent(espectaculoElement);
        }

        return new Document(espectaculosElement);
    }

    public static Element toJDOMElement(AdminEspectaculoDto espectaculo) {

        Element espectaculoElement = new Element("espectaculo", XML_NS);

        if (espectaculo.getIdEspectaculo() != null) {
            Element identifierElement = new Element("idEspectaculo", XML_NS);
            identifierElement.setText(espectaculo.getIdEspectaculo().toString());
            espectaculoElement.addContent(identifierElement);
        }
        
        Element nombreElement = new Element("nombre", XML_NS);
        nombreElement.setText(espectaculo.getNombre());
        espectaculoElement.addContent(nombreElement);
        
        Element descripcionElement = new Element("descripcion", XML_NS);
        descripcionElement.setText(espectaculo.getDescripcion());
        espectaculoElement.addContent(descripcionElement);

        
        if (espectaculo.getFechaEspectaculo() != null) {
            Element fechaEspectaculoElement = calendartoElement(espectaculo.getFechaEspectaculo(), "fechaEspectaculo");
            espectaculoElement.addContent(fechaEspectaculoElement);
        }
        
        if (espectaculo.getFechaLimReserva() != null) {
            Element fechaLimReservaElement = calendartoElement(espectaculo.getFechaLimReserva(), "fechaLimReserva");
            espectaculoElement.addContent(fechaLimReservaElement);
        }
        
        
        Element duracionElement = new Element("duracion", XML_NS);
        duracionElement.setText(Integer.toString(espectaculo.getDuracion()));
        espectaculoElement.addContent(duracionElement);

        Element precioRealElement = new Element("precioReal", XML_NS);
        precioRealElement.setText(Float.toString(espectaculo.getPrecioReal()));
        espectaculoElement.addContent(precioRealElement);

        Element precioDescontadoElement = new Element("precioDescontado", XML_NS);
        precioDescontadoElement.setText(Float.toString(espectaculo.getPrecioDescontado()));
        espectaculoElement.addContent(precioDescontadoElement);
        
        Element comisionVentaElement = new Element("comisionVenta", XML_NS);
        comisionVentaElement.setText(Float.toString(espectaculo.getComisionVenta()));
        espectaculoElement.addContent(comisionVentaElement);
        
        Element numEntradasRestantesElement = new Element("numEntradasRest", XML_NS);
        numEntradasRestantesElement.setText(Integer.toString(espectaculo.getNumEntradasRest()));
        espectaculoElement.addContent(numEntradasRestantesElement);
        
        Element maxEntradasElement = new Element("maxEntradas", XML_NS);
        maxEntradasElement.setText(Integer.toString(espectaculo.getMaxEntradas()));
        espectaculoElement.addContent(maxEntradasElement);
      
        
        return espectaculoElement;
    }
    
    private static Element calendartoElement (Calendar fecha, String tipo) {
    	Element fechaElement = new Element(tipo, XML_NS);
    	
    	String fechaStr = String.format("%02d-%02d-%04d %02d:%02d",fecha.get(Calendar.DATE), (fecha.get(Calendar.MONTH )+1),fecha.get(Calendar.YEAR),fecha.get(Calendar.HOUR_OF_DAY),fecha.get(Calendar.MINUTE));
    
    	fechaElement.setText(fechaStr);
    	
    	return fechaElement;
    
    }

    
	
    public static AdminEspectaculoDto toAdminEspectaculoDto(InputStream espectaculoXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(espectaculoXml);
            Element rootElement = document.getRootElement();

            return toAdminEspectaculoDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static AdminEspectaculoDto toAdminEspectaculoDto(Element espectaculoElement)
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
       
        int maxEntradas = Integer.parseInt(espectaculoElement.getChildTextTrim("maxEntradas", XML_NS));
        
        float comisionVenta = Float.valueOf(espectaculoElement.getChildTextTrim("comisionVenta", XML_NS));
        
        Calendar fechaEspectaculo = getFecha(espectaculoElement.getChildText("fechaEspectaculo", XML_NS));
        Calendar fechaLimReserva = getFecha(espectaculoElement.getChildText("fechaLimReserva", XML_NS));
        

        return new AdminEspectaculoDto(identifier, nombre, descripcion, fechaEspectaculo, duracion, fechaLimReserva, maxEntradas,
        		precioReal, precioDescontado, comisionVenta, numEntradasRest );
    }
    

    public static List<AdminEspectaculoDto> toAdminEspectaculoDtos(InputStream espectaculoXml)
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
            List<AdminEspectaculoDto> espectaculoDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                espectaculoDtos.add(toAdminEspectaculoDto(element));
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
