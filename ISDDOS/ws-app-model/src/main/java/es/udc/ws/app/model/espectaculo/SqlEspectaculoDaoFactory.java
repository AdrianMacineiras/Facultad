package es.udc.ws.app.model.espectaculo;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlEspectaculoDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "SqlEspectaculoDaoFactory.className";
    private static SqlEspectaculoDao dao = null;

    private SqlEspectaculoDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlEspectaculoDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlEspectaculoDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlEspectaculoDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
