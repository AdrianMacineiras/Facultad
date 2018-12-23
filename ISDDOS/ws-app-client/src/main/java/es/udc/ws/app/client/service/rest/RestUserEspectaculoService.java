package es.udc.ws.app.client.service.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.UserEspectaculoService;
import es.udc.ws.app.client.service.dto.UserEspectaculoDto;
import es.udc.ws.app.client.service.exceptions.FechaLimiteException;
import es.udc.ws.app.client.service.exceptions.NumEntradasException;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.rest.xml.XmlUserEspectaculoDtoConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientExceptionConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientReservaDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class RestUserEspectaculoService implements UserEspectaculoService{
    
	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestUserEspectaculoService.endpointAddress";
    private String endpointAddress;

   
    @Override
    public List<UserEspectaculoDto> findEspectaculoByKeywords(String keywords) {

        try {
        		HttpResponse response = Request.Get(getEndpointAddress() + "espectaculos?keywords="
        				+ URLEncoder.encode(keywords, "UTF-8")).
        				execute().returnResponse();

        		validateStatusCode(HttpStatus.SC_OK, response);

        		return XmlUserEspectaculoDtoConversor.toClientEspectaculoDtos(response.getEntity()
        				.getContent());
        	
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public List<ClientReservaDto> findBookByUser(String email) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "reservas?email="
                            + URLEncoder.encode(email, "UTF-8")).
                    execute().returnResponse();
        	 
            validateStatusCode(HttpStatus.SC_OK, response);

            return XmlClientReservaDtoConversor.toClientReservaDtos(response.getEntity()
                    .getContent()); //

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ClientReservaDto book(Long idEspectaculo, String email, String numTarjetaCredito, int numEntradas)
            throws InstanceNotFoundException, InputValidationException,NumEntradasException,FechaLimiteException {

    	try {
    		
            HttpResponse response = Request.Post(getEndpointAddress() + "reservas").
                    bodyForm(
                            Form.form().
                            add("idEspectaculo", Long.toString(idEspectaculo)).
                            add("email", email).
                            add("numTarjetaCredito", numTarjetaCredito).
                            add("numEntradasTotal",Integer.toString(numEntradas)).
                            build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return XmlClientReservaDtoConversor.toClientReservaDto(
                    response.getEntity().getContent());

        } catch (InputValidationException | InstanceNotFoundException | NumEntradasException | FechaLimiteException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private void validateStatusCode(int successCode, HttpResponse response)
            throws InstanceNotFoundException, 
            InputValidationException, ParsingException, UnsupportedOperationException, JDOMException, NumEntradasException, FechaLimiteException {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
       
                case HttpStatus.SC_NOT_FOUND:
                    throw XmlClientExceptionConversor.fromInstanceNotFoundExceptionXml(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw XmlClientExceptionConversor.fromInputValidationExceptionXml(
                            response.getEntity().getContent());
                case HttpStatus.SC_CONFLICT:
                	
                    SAXBuilder builder = new SAXBuilder();
                    Document document = builder.build(response.getEntity().getContent());
                    Element rootElement = document.getRootElement();
                	if (rootElement.getName()=="NumEntradasException") {
                		throw XmlClientExceptionConversor.fromNumEntradasExceptionXml(response.getEntity().getContent());
                	}
                	else {
                		if (rootElement.getName()=="FechaLimiteException") {
	                		throw XmlClientExceptionConversor.fromFechaLimiteExceptionXml(response.getEntity().getContent());
	                	
                		}
                	}

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
