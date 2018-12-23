package es.udc.ws.app.client.service.exceptions;


@SuppressWarnings("serial")
public class InvalidPriceException extends Exception {
    private float precioDescontadoNuevo;
    private float precioDescontadoAntiguo;
    private long idEspectaculo;
    
    public InvalidPriceException(long idEspectaculo,float precioDescontado) {
    	super("Espectaculo's Precio Descontado: "+ precioDescontado +",with id =" +idEspectaculo 
        		+ "can't be changed because the new it's higher.");
    	
    	this.idEspectaculo = idEspectaculo;
    	this.precioDescontadoAntiguo = precioDescontado;
    }
    
    public InvalidPriceException(long idEspectaculo,float precioDescontadoNuevo, float precioDescontadoAntiguo) {
        super("Espectaculo's Precio Descontado: "+precioDescontadoAntiguo+",with id =" + idEspectaculo
        		+ "can't be changed to "+precioDescontadoNuevo+" because it's higher.");
        		
        this.idEspectaculo = idEspectaculo;
        this.precioDescontadoNuevo = precioDescontadoNuevo;
        this.precioDescontadoAntiguo = precioDescontadoAntiguo;
    }

    public Long getIdEspectaculo() {
        return idEspectaculo;
    }

    public float getPrecioDescontadoNuevo() {
        return precioDescontadoNuevo;
    }
    public float getPrecioDescontadoAntiguo() {
        return precioDescontadoAntiguo;
    }

    public void setPrecioDescontadoNuevo(float precioDescontadoNuevo) {
        this.precioDescontadoNuevo = precioDescontadoNuevo;
    }

    public void setPrecioDescontadoAntiguo(float precioDescontadoAntiguo) {
        this.precioDescontadoAntiguo = precioDescontadoAntiguo;
    }
    
    public void setIdEspectaculo(long idEspectaculo) {
    	this.idEspectaculo = idEspectaculo;
    }
}
