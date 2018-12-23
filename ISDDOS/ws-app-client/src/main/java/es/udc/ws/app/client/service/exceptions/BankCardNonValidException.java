package es.udc.ws.app.client.service.exceptions;
@SuppressWarnings("serial")
public class BankCardNonValidException extends Exception{
	 private long idReserva;
	 private String numTarjetaCredito;
	    public BankCardNonValidException(long idReserva,String numTarjetaCredito) {
	        super("Credit card number: "+numTarjetaCredito+"is not the same for book with id: "+idReserva);
	        		
	        this.idReserva = idReserva;
	        this.numTarjetaCredito = numTarjetaCredito;
	    }

	    public Long getIdReserva() {
	        return idReserva;
	    }
	    public void setIdReserva(long idReserva) {
	    	this.idReserva = idReserva;
	    }

		public String getNumTarjetaCredito() {
			return numTarjetaCredito;
		}

		public void setNumTarjetaCredito(String numTarjetaCredito) {
			this.numTarjetaCredito = numTarjetaCredito;
		}
}
