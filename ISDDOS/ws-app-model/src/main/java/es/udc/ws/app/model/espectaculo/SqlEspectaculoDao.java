package es.udc.ws.app.model.espectaculo;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;



import es.udc.ws.util.exceptions.InstanceNotFoundException;


public interface SqlEspectaculoDao {
	public Espectaculo create(Connection connection, Espectaculo espectaculo);
	
	public Espectaculo find(Connection connection, Long idEspectaculo)
            throws InstanceNotFoundException;
    
	public List<Espectaculo> findByKeywords(Connection connection,
	            String keywords, Calendar fechaInicio, Calendar fechaLimite);

	public void update(Connection connection, Espectaculo espectaculo)
	            throws InstanceNotFoundException;

    public void remove(Connection connection, Long idEspectaculo)
	            throws InstanceNotFoundException;
	
}
