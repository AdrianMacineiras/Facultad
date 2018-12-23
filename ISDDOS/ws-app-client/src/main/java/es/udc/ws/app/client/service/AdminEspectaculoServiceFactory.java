package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class AdminEspectaculoServiceFactory {
	private final static String CLASS_NAME_PARAMETER = "AdminEspectaculoServiceFactory.className";
	private static Class<AdminEspectaculoService> serviceClass = null;
	
	private AdminEspectaculoServiceFactory() {
	}
	
	@SuppressWarnings("unchecked")
	private synchronized static Class<AdminEspectaculoService> getServiceClass() {
	
	if (serviceClass == null) {
	    try {
	        String serviceClassName = ConfigurationParametersManager
	                .getParameter(CLASS_NAME_PARAMETER);
	        serviceClass = (Class<AdminEspectaculoService>) Class.forName(serviceClassName);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	return serviceClass;
	
	}
	
	public static AdminEspectaculoService getService() {
	
	try {
	    return (AdminEspectaculoService) getServiceClass().newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}

}
}
