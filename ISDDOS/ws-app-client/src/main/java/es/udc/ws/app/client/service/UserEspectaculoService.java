package es.udc.ws.app.client.service;

import java.util.List;

import es.udc.ws.app.client.service.dto.UserEspectaculoDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.FechaLimiteException;
import es.udc.ws.app.client.service.exceptions.NumEntradasException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface UserEspectaculoService {
	
	public List<UserEspectaculoDto> findEspectaculoByKeywords(String keywords);
    
    public List<ClientReservaDto> findBookByUser(String email);
    public ClientReservaDto book(Long idEspectaculo, String email, String numTarjetaCredito, int numEntradas)
            throws InstanceNotFoundException, InputValidationException,NumEntradasException,FechaLimiteException;
	
}
