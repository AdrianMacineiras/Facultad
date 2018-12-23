package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import es.udc.ws.app.client.service.AdminEspectaculoService;
import es.udc.ws.app.client.service.dto.AdminEspectaculoDto;
import es.udc.ws.app.client.service.exceptions.BankCardNonValidException;
import es.udc.ws.app.client.service.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.client.service.exceptions.InvalidPriceException;
import es.udc.ws.app.client.service.rest.xml.XmlAdminEspectaculoDtoConversor;

import es.udc.ws.app.client.service.rest.xml.XmlClientExceptionConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class RestAdminEspectaculoService implements AdminEspectaculoService{
	
	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestAdminEspectaculoService.endpointAddress";
    private String endpointAddress;

	 @Override
	    public Long addEspectaculo(AdminEspectaculoDto espectaculo) throws InputValidationException {

	        try {

	            HttpResponse response = Request.Post(getEndpointAddress() + "espectaculos").
	                    bodyStream(toInputStream(espectaculo), ContentType.create("application/xml")).
	                    execute().returnResponse();

	            validateStatusCode(HttpStatus.SC_CREATED, response);

	            return XmlAdminEspectaculoDtoConversor.toAdminEspectaculoDto(response.getEntity().getContent()).getIdEspectaculo();

	        } catch (InputValidationException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

	    }
	    

	    @Override
	    public void updateEspectaculo(AdminEspectaculoDto espectaculo) throws InputValidationException,
	            InstanceNotFoundException,InvalidPriceException {

	        try {

	            HttpResponse response = Request.Put(getEndpointAddress() + "espectaculos/" + espectaculo.getIdEspectaculo()).
	                    bodyStream(toInputStream(espectaculo), ContentType.create("application/xml")).
	                    execute().returnResponse();

	            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

	        } catch (InputValidationException | InstanceNotFoundException | InvalidPriceException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

	    }
	    
	    public void checkBook(Long idReserva,String numTarjetaCredito) throws InputValidationException, InstanceNotFoundException,
	    BankCardNonValidException, BookAlreadyUsedException{
	    	
	    	try {

	            HttpResponse response = Request.Post(getEndpointAddress() + "reservas/"+idReserva+"/check").
	                    bodyForm(
	                            Form.form().
	                            add("numTarjetaCredito", numTarjetaCredito).
	                            build()).
	                    execute().returnResponse();

	            validateStatusCode(HttpStatus.SC_OK , response);

	        } catch (InputValidationException | InstanceNotFoundException | BankCardNonValidException | BookAlreadyUsedException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    	
	    	
	    
	    	
	    }

	    @Override
	    public AdminEspectaculoDto getEspectaculo(Long idEspectaculo) throws InstanceNotFoundException {
	    	
	        try {

	            HttpResponse response = Request.Get(getEndpointAddress() + "espectaculos/" + idEspectaculo).
	                    execute().returnResponse();

	            validateStatusCode(HttpStatus.SC_OK, response);

	            return XmlAdminEspectaculoDtoConversor.toAdminEspectaculoDto(
	                    response.getEntity().getContent());

	        } catch (InstanceNotFoundException e) {
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

	    private InputStream toInputStream(AdminEspectaculoDto espectaculo) {

	        try {

	            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
	            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

	            outputter.output(XmlAdminEspectaculoDtoConversor.toXml(espectaculo), xmlOutputStream);

	            return new ByteArrayInputStream(xmlOutputStream.toByteArray());

	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }

	    }

	    private void validateStatusCode(int successCode, HttpResponse response)
	            throws InstanceNotFoundException, 
	            InputValidationException, ParsingException, UnsupportedOperationException, JDOMException, InvalidPriceException, BankCardNonValidException, BookAlreadyUsedException {

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
	    	          
	                	if (rootElement.getName()=="InvalidPriceException") {
	                		throw XmlClientExceptionConversor.fromInvalidPriceExceptionXml(response.getEntity().getContent());
	                	}
	                	else {
	                		if (rootElement.getName()=="BankCardNonValidException") {
		                		throw XmlClientExceptionConversor.fromBankCardNonValidExceptionXml(response.getEntity().getContent());
		                	
	                		}
	                	}
	                case HttpStatus.SC_FORBIDDEN:
	                	throw XmlClientExceptionConversor.fromBookAlreadyUsedExceptionXml(
	                            response.getEntity().getContent());
	                

	                default:
	                    throw new RuntimeException("HTTP error; status code = "
	                            + statusCode);
	            }

	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }

	    }
	
}
