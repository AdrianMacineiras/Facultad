package es.udc.ws.app.client.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class FechaLimiteException extends Exception {
	   private Calendar fechaLimiteEspectaculo;
	   private Calendar fechaReserva;
	   private long idEspectaculo;

	    public FechaLimiteException(long idEspectaculo,Calendar fechaLimiteEspectaculo, Calendar fechaReserva) {
	        super("The book could not be completed because the fechaLimite of espectaculo with id: "+idEspectaculo
	        		+" has been exceeded: "+fechaLimiteEspectaculo.getTime().toString());
	        		
	        
	        this.idEspectaculo = idEspectaculo;
	        this.fechaLimiteEspectaculo = fechaLimiteEspectaculo;
	        this.fechaReserva = fechaReserva;
	    }
		public Calendar getFechaLimiteEspectaculo() {
			return fechaLimiteEspectaculo;
		}
		public void setFechaLimiteEspectaculo(Calendar fechaLimiteEspectaculo) {
			this.fechaLimiteEspectaculo = fechaLimiteEspectaculo;
		}
		public Calendar getFechaReserva() {
			return fechaReserva;
		}
		public void setFechaReserva(Calendar fechaReserva) {
			this.fechaReserva = fechaReserva;
		}
		public long getIdEspectaculo() {
			return idEspectaculo;
		}
		public void setIdEspectaculo(long idEspectaculo) {
			this.idEspectaculo = idEspectaculo;
		}

}
