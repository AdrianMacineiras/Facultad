package es.udc.ws.app.model.espectaculoservice;

import java.util.Calendar;
import java.util.List;
import es.udc.ws.app.model.espectaculo.*;
import es.udc.ws.app.model.espectaculoservice.exceptions.*;
import es.udc.ws.app.model.reserva.*;
import es.udc.ws.util.exceptions.*;


public interface EspectaculoService {
	    public Espectaculo addEspectaculo(Espectaculo espectaculo) throws InputValidationException;

	    public void updateEspectaculo(Espectaculo espectaculo) throws InputValidationException,
	            InstanceNotFoundException,InvalidPriceException;
	    
	    public Espectaculo findEspectaculo(Long idEspectaculo) throws InstanceNotFoundException;
	    
	    public List<Espectaculo> findEspectaculoByKeywords(String keywords, Calendar fechaInicio,Calendar fechaLimite);
	    
	    public List<Reserva> findBookByUser(String email);
	    public Reserva book(Long idEspectaculo, String email, String numTarjetaCredito, int numEntradas)
	            throws InstanceNotFoundException, InputValidationException,NumEntradasException,FechaLimiteException;

	    public void checkBook(Long idReserva,String numTarjetaCredito) 
	    		throws InputValidationException,InstanceNotFoundException,BookAlreadyUsedException,BankCardNonValidException;
	           
}
