package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceReservaDto;
import es.udc.ws.app.model.espectaculoservice.EspectaculoServiceFactory;
import es.udc.ws.app.model.espectaculoservice.exceptions.BankCardNonValidException;
import es.udc.ws.app.model.espectaculoservice.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.model.espectaculoservice.exceptions.FechaLimiteException;
import es.udc.ws.app.model.espectaculoservice.exceptions.NumEntradasException;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceReservaDtoConversor;
import es.udc.ws.app.serviceutil.ReservaToReservaDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class ReservasServlet extends HttpServlet {
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			
			String[] parts = path.split("/");
			
			String reservaIdAsString = parts[1];
			String strCheck = parts[2];
	        Long reservaId;
	        try {
	        	reservaId = Long.valueOf(reservaIdAsString);
	        } catch (NumberFormatException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                            "Invalid Request: " + "invalid id reserva '" + reservaIdAsString + "'")),
	                    null);
	            return;
	        }
	        
	        
	        if(!strCheck.equals("check")) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                            "Invalid Request: the second argument is invalid." )),
	                    null);
	            return;
	        	
	        }		
	        
	        String numTarjetaCredito = req.getParameter("numTarjetaCredito");
	        if (numTarjetaCredito == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                            "Invalid Request: " + "parameter 'numTarjetaCredito' is mandatory")),
	                    null);
	
	            return;
	        }
	        
	        try {
	        	EspectaculoServiceFactory.getService().checkBook(reservaId, numTarjetaCredito);
	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                    XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
	            return;
	        } catch (InputValidationException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
	            return;
			} catch (BookAlreadyUsedException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
	                    XmlServiceExceptionConversor.toBookAlreadyUsedExceptionXml(ex), null);
			} catch (BankCardNonValidException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CONFLICT,
	                    XmlServiceExceptionConversor.toBankCardNonValidExceptionXml(ex), null);
			}
			
		}
		else {		
	        String idEspectaculoParameter = req.getParameter("idEspectaculo");
	        if (idEspectaculoParameter == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
	                            new InputValidationException("Invalid Request: " + "parameter 'idEspectaculo' is mandatory")),
	                    null);
	            return;
	        }
	        Long idEspectaculo;
	        try {
	        	idEspectaculo = Long.valueOf(idEspectaculoParameter);
	        } catch (NumberFormatException ex) {
	            ServletUtils
	                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                            XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                                    "Invalid Request: " + "parameter 'idEspectaculo' is invalid '" + idEspectaculoParameter + "'")),
	                            null);
	
	            return;
	        }
	        String email = req.getParameter("email");
	        if (email == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
	                            new InputValidationException("Invalid Request: " + "parameter 'email' is mandatory")),
	                    null);
	            return;
	        }
	        String numTarjetaCredito = req.getParameter("numTarjetaCredito");
	        if (numTarjetaCredito == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                            "Invalid Request: " + "parameter 'numTarjetaCreditor' is mandatory")),
	                    null);
	
	            return;
	        }
	        String numEntradasTotalParameter = req.getParameter("numEntradasTotal");
	      
	        if (numEntradasTotalParameter == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
	                            new InputValidationException("Invalid Request: " + "parameter 'numEntradasTotal' is mandatory")),
	                    null);
	            return;
	        }
	        int numEntradasTotal;
	        try {
	        	numEntradasTotal = Integer.valueOf(numEntradasTotalParameter);
	        	
	        } catch (NumberFormatException ex) {
	            ServletUtils
	                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                            XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                                    "Invalid Request: " + "parameter 'numEntradasTotal' is invalid '" + numEntradasTotalParameter + "'")),
	                            null);
	
	            return;
	        }
	        
	        Reserva reserva;
	        try {
	        	reserva = EspectaculoServiceFactory.getService().book(idEspectaculo, email, numTarjetaCredito, numEntradasTotal);
	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                    XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
	            return;
	        } catch (InputValidationException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
	            return;
	        } catch (NumEntradasException ex) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CONFLICT,
	                    XmlServiceExceptionConversor.toNumEntradasExceptionXml(ex), null);
	        	return;
			} catch (FechaLimiteException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CONFLICT,
	                    XmlServiceExceptionConversor.toFechaLimiteExceptionXml(ex), null);
				return;
			}
	        
	        ServiceReservaDto reservaDto = ReservaToReservaDtoConversor.toReservaDto(reserva);

	        String reservaURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + reserva.getIdReserva().toString();

	        Map<String, String> headers = new HashMap<>(1);
	        headers.put("Location", reservaURL);
	
	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
	                XmlServiceReservaDtoConversor.toResponse(reservaDto), headers);
		}
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
        	       	
        	 String email = req.getParameter("email");       
             List<Reserva> reservas = EspectaculoServiceFactory.getService().findBookByUser(email);
     
             

             List<ServiceReservaDto> reservasDto = ReservaToReservaDtoConversor.toReservasDtos(reservas);

             
             ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                     XmlServiceReservaDtoConversor.toXml(reservasDto), null);
           
            return;
        }
       
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                XmlServiceExceptionConversor.toInputValidationExceptionXml(
                        new InputValidationException("Invalid Request: " + "invalid reserva id")),
                null);
    }
    
    

}
