package es.udc.ws.app.client.service.exceptions;

@SuppressWarnings("serial")
public class BookAlreadyUsedException extends Exception {
	 private long idReserva;
	    public BookAlreadyUsedException(long idReserva) {
	        super("Book with id: "+idReserva+" has been already checked.");
	        		
	        this.idReserva = idReserva;
	        
	    }

	    public Long getIdReserva() {
	        return idReserva;
	    }
	    public void setIdReserva(long idReserva) {
	    	this.idReserva = idReserva;
	    }
}
