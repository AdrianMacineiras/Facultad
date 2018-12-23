package es.udc.ws.app.client.service;


import es.udc.ws.app.client.service.dto.AdminEspectaculoDto;
import es.udc.ws.app.client.service.exceptions.BankCardNonValidException;
import es.udc.ws.app.client.service.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.client.service.exceptions.InvalidPriceException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface AdminEspectaculoService {
	   
	    public Long addEspectaculo(AdminEspectaculoDto espectaculo) throws InputValidationException;

	    public void updateEspectaculo(AdminEspectaculoDto espectaculo) throws InputValidationException,
	            InstanceNotFoundException,InvalidPriceException;
	    
	    public AdminEspectaculoDto getEspectaculo(Long idEspectaculo) throws InstanceNotFoundException;
	    
	    public void checkBook(Long idReserva,String numTarjetaCredito) 
	    		throws InputValidationException,InstanceNotFoundException,BookAlreadyUsedException,BankCardNonValidException;
}
