package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdbc3CcSqlReservaDao extends AbstractSqlReservaDao{

	@Override
	public Reserva create(Connection connection, Reserva reserva) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Reserva"
                + " (idEspectaculo, email, numTarjetaCredito, precio, fechaReserva, numEntradasTotal, comprobar)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

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
            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long idReserva = resultSet.getLong(1);

            /* Return sale. */
            return new Reserva(idReserva,reserva.getIdEspectaculo(),reserva.getEmail(),
            		reserva.getNumTarjetaCredito(),reserva.getPrecio(),reserva.getFechaReserva(),reserva.getNumEntradasTotal(),reserva.getCheck());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

	}

}
