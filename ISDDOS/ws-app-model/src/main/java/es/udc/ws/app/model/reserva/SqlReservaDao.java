package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;


public interface SqlReservaDao {
	public Reserva create(Connection connection, Reserva reserva);
	
	public Reserva find(Connection connection, Long idReserva)
            throws InstanceNotFoundException;
    
	public List<Reserva> findByUser(Connection connection,
			String email);

	public void update(Connection connection, Reserva reserva)
	            throws InstanceNotFoundException;

    public void remove(Connection connection, Long idReserva )
	            throws InstanceNotFoundException;
}
