package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.exceptions.BankCardNonValidException;
import es.udc.ws.app.client.service.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.client.service.exceptions.FechaLimiteException;
import es.udc.ws.app.client.service.exceptions.InvalidPriceException;
import es.udc.ws.app.client.service.exceptions.NumEntradasException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientExceptionConversor {
    public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

    public final static Namespace XML_NS = 
            Namespace.getNamespace("http://ws.udc.es/espectaculos/xml");

    public static InputValidationException fromInputValidationExceptionXml(InputStream ex) 
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element message = rootElement.getChild("message", XML_NS);

            return new InputValidationException(message.getText());
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static InstanceNotFoundException fromInstanceNotFoundExceptionXml(InputStream ex) 
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element instanceId = rootElement.getChild("instanceId", XML_NS);
            Element instanceType = rootElement.getChild("instanceType", XML_NS);

            return new InstanceNotFoundException(instanceId.getText(), instanceType.getText());
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    
    public static BookAlreadyUsedException fromBookAlreadyUsedExceptionXml(InputStream ex) 
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element idReserva= rootElement.getChild("idReserva", XML_NS);

            return new BookAlreadyUsedException(Long.valueOf(idReserva.getText()));
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    
    public static BankCardNonValidException fromBankCardNonValidExceptionXml(InputStream ex) 
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element idReserva= rootElement.getChild("idReserva", XML_NS);
            Element numTarjetaCredito = rootElement.getChild("numTarjetaCredito", XML_NS);

            return new BankCardNonValidException(Long.valueOf(idReserva.getText()), numTarjetaCredito.getText());
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    
    public static NumEntradasException fromNumEntradasExceptionXml(InputStream ex) 
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element idEspectaculo= rootElement.getChild("idEspectaculo", XML_NS);
            Element numEntradasRest = rootElement.getChild("numEntradasRest", XML_NS);
            
            //Element message = rootElement.getChild("message", XML_NS);

            return new NumEntradasException(Long.valueOf(idEspectaculo.getText()), Integer.valueOf(numEntradasRest.getText()));
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    
    public static FechaLimiteException fromFechaLimiteExceptionXml(InputStream ex) 
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element idEspectaculo= rootElement.getChild("idEspectaculo", XML_NS);
            Element fechaLimiteEspectaculoElement = rootElement.getChild("fechalimReserva", XML_NS);
            Element fechaReservaElement = rootElement.getChild("fechaReserva", XML_NS);
            
            Calendar fechaLimiteEspectaculo = null;
            if (fechaLimiteEspectaculoElement != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
                fechaLimiteEspectaculo = Calendar.getInstance();
                fechaLimiteEspectaculo.setTime(sdf.parse(fechaLimiteEspectaculoElement.getText()));
            }
            
            Calendar fechaReserva = null;
            if (fechaLimiteEspectaculoElement != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
                fechaReserva = Calendar.getInstance();
                fechaReserva.setTime(sdf.parse(fechaReservaElement.getText()));
            }

            return new FechaLimiteException(Long.valueOf(idEspectaculo.getText()),fechaLimiteEspectaculo,fechaReserva);
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    
    public static InvalidPriceException fromInvalidPriceExceptionXml(InputStream ex) 
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();
            
            Element idReserva= rootElement.getChild("idEspectaculo", XML_NS);
            Element precioDescontadoAntiguo= rootElement.getChild("precioDescontadoAntiguo", XML_NS);
            Element precioDescontadoNuevo= rootElement.getChild("precioDescontadoNuevo", XML_NS);
           
            return new InvalidPriceException(Long.valueOf(idReserva.getText()),Float.valueOf(precioDescontadoNuevo.getText()),Float.valueOf(precioDescontadoAntiguo.getText()));
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
