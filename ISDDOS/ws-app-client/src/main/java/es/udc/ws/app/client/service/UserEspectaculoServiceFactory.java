package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class UserEspectaculoServiceFactory {
	
	private final static String CLASS_NAME_PARAMETER = "UserEspectaculoServiceFactory.className";
	private static Class<UserEspectaculoService> serviceClass = null;
	
	private UserEspectaculoServiceFactory() {
	}
	
	@SuppressWarnings("unchecked")
	private synchronized static Class<UserEspectaculoService> getServiceClass() {
	
	if (serviceClass == null) {
	    try {
	        String serviceClassName = ConfigurationParametersManager
	                .getParameter(CLASS_NAME_PARAMETER);
	        serviceClass = (Class<UserEspectaculoService>) Class.forName(serviceClassName);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	return serviceClass;
	
	}
	
	public static UserEspectaculoService getService() {
	
	try {
	    return (UserEspectaculoService) getServiceClass().newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}

}

}
