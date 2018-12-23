package es.udc.ws.app.model.espectaculoservice;


import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class EspectaculoServiceFactory {
	   private final static String CLASS_NAME_PARAMETER = "EspectaculoServiceFactory.className";
	    private static EspectaculoService service = null;

	    private EspectaculoServiceFactory() {
	    }

	    @SuppressWarnings("rawtypes")
	    private static EspectaculoService getInstance() {
	        try {
	            String serviceClassName = ConfigurationParametersManager
	                    .getParameter(CLASS_NAME_PARAMETER);
	            Class serviceClass = Class.forName(serviceClassName);
	            return (EspectaculoService) serviceClass.newInstance();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

	    }

	    public synchronized static EspectaculoService getService() {

	        if (service == null) {
	            service = getInstance();
	        }
	        return service;

	    }
}
