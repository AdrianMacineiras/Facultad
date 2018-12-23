package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlReservaDao implements SqlReservaDao {
    
	protected AbstractSqlReservaDao() {
    }

    @Override
    public Reserva find(Connection connection, Long idReserva)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT idEspectaculo, email,"
                + "numTarjetaCredito, precio, fechaReserva, numEntradasTotal,comprobar FROM Reserva WHERE idReserva = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, idReserva.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(idReserva,
                        Reserva.class.getName());
            }

            /* Get results. */
            i = 1;
            Long idEspectaculo = resultSet.getLong(i++);
            String email = resultSet.getString(i++);
            String numTarjetaCredito = resultSet.getString(i++);
            float precio = resultSet.getFloat(i++);
            Calendar fechaReserva = Calendar.getInstance();
            fechaReserva.setTime(resultSet.getTimestamp(i++));
            int numEntradasTotal = resultSet.getInt(i++);
            boolean check = resultSet.getBoolean(i++);

            /* Return sale. */
            return new Reserva(idReserva, idEspectaculo, email,
            		numTarjetaCredito, precio, fechaReserva, numEntradasTotal,check);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public void update(Connection connection, Reserva reserva)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Reserva"
                + " SET idEspectaculo = ?, email = ?,"
                + " numTarjetaCredito = ?, precio = ?, fechaReserva = ?, numEntradasTotal = ?, comprobar = ? WHERE idReserva = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserva.getIdEspectaculo());
            preparedStatement.setString(i++, reserva.getEmail());
            preparedStatement.setString(i++, reserva.getNumTarjetaCredito());
            preparedStatement.setFloat(i++, reserva.getPrecio());
            Timestamp fechaReserva = reserva.getFechaReserva() != null ? new Timestamp( 
                    reserva.getFechaReserva().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, fechaReserva);
            preparedStatement.setInt(i++, reserva.getNumEntradasTotal());

            preparedStatement.setBoolean(i++, reserva.getCheck());
            preparedStatement.setLong(i++, reserva.getIdReserva());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(reserva.getIdEspectaculo(),
                        Reserva.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void remove(Connection connection, Long idReserva)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Reserva WHERE" + " idReserva = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, idReserva);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(idReserva,
                        Reserva.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public List<Reserva> findByUser(Connection connection, String email) {

        /* Create "queryString". */
        
        String queryString = "SELECT idEspectaculo, idReserva,"
                + "numTarjetaCredito, precio, fechaReserva, numEntradasTotal, comprobar, email FROM Reserva WHERE email = ?";
        


        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

        	 /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, email);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

          
            /* Read reserva. */
            List<Reserva> reservas = new ArrayList<Reserva>();

            while (resultSet.next()) {

                i = 1;
                
                Long idEspectaculo = resultSet.getLong(i++);
                Long idReserva = resultSet.getLong(i++);
                
                String numTarjetaCredito = resultSet.getString(i++);
                float precio = resultSet.getFloat(i++);
                Calendar fechaReserva = Calendar.getInstance();
                fechaReserva.setTime(resultSet.getTimestamp(i++));
                int numEntradasTotal = resultSet.getInt(i++);
                boolean check = resultSet.getBoolean(i++);
                

                reservas.add(new Reserva(idReserva, idEspectaculo, email,
                		numTarjetaCredito, precio, fechaReserva, numEntradasTotal,check));

            }

            /* Return movies. */
            return reservas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }	
}
