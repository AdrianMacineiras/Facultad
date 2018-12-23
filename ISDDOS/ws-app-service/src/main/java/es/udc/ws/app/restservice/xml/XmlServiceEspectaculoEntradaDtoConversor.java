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

import es.udc.ws.app.dto.ServiceEspectaculoEntradaDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceEspectaculoEntradaDtoConversor {
	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/espectaculos/xml");

    public static Document toXml(ServiceEspectaculoEntradaDto espectaculo) throws IOException {

        Element espectaculoEntradaElement = toJDOMElement(espectaculo);

        return new Document(espectaculoEntradaElement);
    }

    public static Document toXml(List<ServiceEspectaculoEntradaDto> espectaculo) throws IOException {

        Element espectaculosEntradaElement = new Element("espectaculos", XML_NS);
        for (int i = 0; i < espectaculo.size(); i++) {
            ServiceEspectaculoEntradaDto xmlEspectaculoEntradaDto = espectaculo.get(i);
            Element espectaculoEntradaElement = toJDOMElement(xmlEspectaculoEntradaDto);
            espectaculosEntradaElement.addContent(espectaculoEntradaElement);
        }

        return new Document(espectaculosEntradaElement);
    }


    public static Element toJDOMElement(ServiceEspectaculoEntradaDto espectaculo) {

        Element espectaculoEntradaElement = new Element("espectaculo", XML_NS);

        if (espectaculo.getIdEspectaculo() != null) {
            Element identifierElement = new Element("idEspectaculo", XML_NS);
            identifierElement.setText(espectaculo.getIdEspectaculo().toString());
            espectaculoEntradaElement.addContent(identifierElement);
        }

        Element nombreElement = new Element("nombre", XML_NS);
        nombreElement.setText(espectaculo.getNombre());
        espectaculoEntradaElement.addContent(nombreElement);
        
        Element descripcionElement = new Element("descripcion", XML_NS);
        descripcionElement.setText(espectaculo.getDescripcion());
        espectaculoEntradaElement.addContent(descripcionElement);
        
        if (espectaculo.getFechaEspectaculo() != null) {
            Element fechaEspectaculoElement = calendartoElement(espectaculo.getFechaEspectaculo(), "fechaEspectaculo");
            espectaculoEntradaElement.addContent(fechaEspectaculoElement);
        }
        Element duracionElement = new Element("duracion", XML_NS);
        duracionElement.setText(Integer.toString(espectaculo.getDuracion()));
        espectaculoEntradaElement.addContent(duracionElement);
        
        if (espectaculo.getFechaLimReserva() != null) {
            Element fechaLimReservaElement = calendartoElement(espectaculo.getFechaLimReserva(), "fechaLimReserva");
            espectaculoEntradaElement.addContent(fechaLimReservaElement);
        }

        Element maxEntradasElement = new Element("maxEntradas", XML_NS);
        maxEntradasElement.setText(Integer.toString(espectaculo.getMaxEntradas()));
        espectaculoEntradaElement.addContent(maxEntradasElement);
        
        Element precioRealElement = new Element("precioReal", XML_NS);
        precioRealElement.setText(Float.toString(espectaculo.getPrecioReal()));
        espectaculoEntradaElement.addContent(precioRealElement);
        
        Element precioDescontadoElement = new Element("precioDescontado", XML_NS);
        precioDescontadoElement.setText(Float.toString(espectaculo.getPrecioDescontado()));
        espectaculoEntradaElement.addContent(precioDescontadoElement);
        
        Element comisionVentaElement = new Element("comisionVenta", XML_NS);
        comisionVentaElement.setText(Float.toString(espectaculo.getComisionVenta()));
        espectaculoEntradaElement.addContent(comisionVentaElement);
        
        Element numEntradasRestElement = new Element("numEntradasRest", XML_NS);
        numEntradasRestElement.setText(Integer.toString(espectaculo.getNumEntradasRest()));
        espectaculoEntradaElement.addContent(numEntradasRestElement);
        

        return espectaculoEntradaElement;
    }
    
    
    private static Element calendartoElement (Calendar fecha, String tipo) {
    	Element fechaElement = new Element(tipo, XML_NS);
    	
    	String fechaStr = String.format("%02d-%02d-%04d %02d:%02d",fecha.get(Calendar.DATE), (fecha.get(Calendar.MONTH )+1),fecha.get(Calendar.YEAR),fecha.get(Calendar.HOUR_OF_DAY),fecha.get(Calendar.MINUTE));
    
    	fechaElement.setText(fechaStr);
    	
    	return fechaElement;
    
    }
    
    
    
    public static ServiceEspectaculoEntradaDto toServiceEspectaculoEntradaDto(InputStream espectaculoEntradaXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(espectaculoEntradaXml);
            Element rootElement = document.getRootElement();

            return toServiceEspectaculoEntradaDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
     
    }
    

    
    private static ServiceEspectaculoEntradaDto toServiceEspectaculoEntradaDto(Element espectaculoEntradaElement)
            throws ParsingException, DataConversionException, ParseException {
        if (!"espectaculo".equals(espectaculoEntradaElement.getName())) {
            throw new ParsingException("Unrecognized element '" + espectaculoEntradaElement.getName() + "' ('espectaculo' expected)");
        }
        Element identifierElement = espectaculoEntradaElement.getChild("idEspectaculo", XML_NS);
        Long identifier = null;
  
        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }
        String nombre = espectaculoEntradaElement.getChildTextNormalize("nombre", XML_NS);
        
        String descripcion = espectaculoEntradaElement.getChildTextNormalize("descripcion", XML_NS);


        Calendar fechaEspectaculo = getFecha(espectaculoEntradaElement.getChildText("fechaEspectaculo", XML_NS));
       
        
        int duracion = Short.valueOf(espectaculoEntradaElement.getChildTextTrim("duracion", XML_NS));
        
        Calendar fechaLimReserva = getFecha(espectaculoEntradaElement.getChildText("fechaLimReserva", XML_NS)); 

        int maxEntradas = Integer.valueOf(espectaculoEntradaElement.getChildTextTrim("maxEntradas", XML_NS));
        
        float precioReal = Float.valueOf(espectaculoEntradaElement.getChildTextTrim("precioReal", XML_NS));
        
        float precioDescontado = Float.valueOf(espectaculoEntradaElement.getChildTextTrim("precioDescontado", XML_NS));
        
        float comisionVenta = Float.valueOf(espectaculoEntradaElement.getChildTextTrim("comisionVenta", XML_NS));
        
        int numEntradasRest = Integer.valueOf(espectaculoEntradaElement.getChildTextTrim("numEntradasRest", XML_NS));

        return new ServiceEspectaculoEntradaDto(identifier,nombre, descripcion,fechaEspectaculo,duracion,fechaLimReserva,maxEntradas, precioReal,
    			precioDescontado, comisionVenta, numEntradasRest);
    }
    
    
    private final static Calendar getFecha(String DateElement) throws DataConversionException, ParseException {
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    	cal.setTime(sdf.parse(DateElement));
    	return cal;
    	
    }

}
