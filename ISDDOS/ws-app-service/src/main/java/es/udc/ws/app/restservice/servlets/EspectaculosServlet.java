package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceEspectaculoEntradaDto;
import es.udc.ws.app.dto.ServiceEspectaculoSalidaDto;
import es.udc.ws.app.model.espectaculo.Espectaculo;
import es.udc.ws.app.model.espectaculoservice.EspectaculoServiceFactory;
import es.udc.ws.app.model.espectaculoservice.exceptions.InvalidPriceException;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceEspectaculoEntradaDtoConversor;
import es.udc.ws.app.restservice.xml.XmlServiceEspectaculoSalidaDtoConversor;
import es.udc.ws.app.serviceutil.EspectaculoToEspectaculoEntradaDtoConversor;
import es.udc.ws.app.serviceutil.EspectaculoToEspectaculoSalidaDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

@SuppressWarnings("serial")
public class EspectaculosServlet extends HttpServlet {

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(
							new InputValidationException("Invalid Request: " + "invalid path " + path)),
					null);
			return;
		}
    	ServiceEspectaculoEntradaDto xmlespectaculoentrada;
        try {
            xmlespectaculoentrada = XmlServiceEspectaculoEntradaDtoConversor.toServiceEspectaculoEntradaDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);

            return;

        }
        Espectaculo espectaculo = EspectaculoToEspectaculoEntradaDtoConversor.toEspectaculo(xmlespectaculoentrada);
        try {
            espectaculo = EspectaculoServiceFactory.getService().addEspectaculo(espectaculo);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
            return;
        }
        ServiceEspectaculoEntradaDto espectaculoDto = EspectaculoToEspectaculoEntradaDtoConversor.toEspectaculoEntradaDto(espectaculo);

        String espectaculoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + espectaculo.getIdEspectaculo();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", espectaculoURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlServiceEspectaculoEntradaDtoConversor.toXml(espectaculoDto), headers);
    }
	
	@Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid id espectaculo")),
                    null);
            return;
        }
        String espectaculoIdAsString = path.substring(1);
        Long espectaculoId;
        try {
            espectaculoId = Long.parseLong(espectaculoIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
                            "Invalid Request: " + "invalid id espectaculo '" + espectaculoIdAsString + "'")),
                    null);
            return;
        }

        ServiceEspectaculoEntradaDto espectaculoDto;
        try {
            espectaculoDto = XmlServiceEspectaculoEntradaDtoConversor.toServiceEspectaculoEntradaDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);
            return;

        }
        if (!espectaculoId.equals(espectaculoDto.getIdEspectaculo())) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid idEspectaculo")),
                    null);
            return;
        }
        Espectaculo espectaculo = EspectaculoToEspectaculoEntradaDtoConversor.toEspectaculo(espectaculoDto);
        try {
            EspectaculoServiceFactory.getService().updateEspectaculo(espectaculo);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
        } catch (InvalidPriceException ex) {
        	 ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CONFLICT,
                     XmlServiceExceptionConversor.toInvalidPriceExceptionXml(ex), null);
             return;
		}
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            String keywords = req.getParameter("keywords");
            
            Calendar fechaInicio = Calendar.getInstance();
            Calendar fechaLimite = Calendar.getInstance();
            fechaLimite.add(Calendar.DAY_OF_MONTH, 30); 
           
            List<Espectaculo> espectaculos = EspectaculoServiceFactory.getService().findEspectaculoByKeywords(keywords, fechaInicio, fechaLimite);
            List<ServiceEspectaculoSalidaDto> espectaculoDtos = EspectaculoToEspectaculoSalidaDtoConversor.toEspectaculoSalidaDtos(espectaculos);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceEspectaculoSalidaDtoConversor.toXml(espectaculoDtos), null);
        } else {
            String espectaculoIdAsString = path.substring(1);
            Long idEspectaculo;
            try {
            	idEspectaculo = Long.valueOf(espectaculoIdAsString);
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
                                "Invalid Request: " + "invalid espectaculo id'" + espectaculoIdAsString + "'")),
                        null);

                return;
            }
            Espectaculo espectaculo;
            try {
                espectaculo = EspectaculoServiceFactory.getService().findEspectaculo(idEspectaculo);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
                return;
            }
            ServiceEspectaculoEntradaDto espectaculoDto = EspectaculoToEspectaculoEntradaDtoConversor.toEspectaculoEntradaDto(espectaculo);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceEspectaculoEntradaDtoConversor.toXml(espectaculoDto), null);
        }
    }
}