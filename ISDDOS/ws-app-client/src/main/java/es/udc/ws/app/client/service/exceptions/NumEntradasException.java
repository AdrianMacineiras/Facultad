package es.udc.ws.app.client.service.exceptions;


@SuppressWarnings("serial")
public class NumEntradasException extends Exception{
	   private int numEntradasRest;
	   private long idEspectaculo;

	    public NumEntradasException(long idEspectaculo,int numEntradasRest) {
	        super("There are not enough tickets of espectaculo with id: "+idEspectaculo
	        		+" Tickets available: "+numEntradasRest);
	        		

	        this.idEspectaculo = idEspectaculo;
	        this.numEntradasRest = numEntradasRest;
	    }

		public int getNumEntradasRest() {
			return numEntradasRest;
		}

		public void setNumEntradasRest(int numEntradasRest) {
			this.numEntradasRest = numEntradasRest;
		}

		public long getIdEspectaculo() {
			return idEspectaculo;
		}

		public void setIdEspectaculo(long idEspectaculo) {
			this.idEspectaculo = idEspectaculo;
		}
}
