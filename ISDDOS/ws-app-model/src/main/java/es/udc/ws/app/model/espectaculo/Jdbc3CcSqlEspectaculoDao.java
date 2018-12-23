package es.udc.ws.app.model.espectaculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdbc3CcSqlEspectaculoDao extends AbstractSqlEspectaculoDao {

	@Override
	public Espectaculo create(Connection connection, Espectaculo espectaculo) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Espectaculo"
                + "  (nombre, descripcion,fechaEspectaculo, duracion, fechaLimReserva,"
                + "maxEntradas, precioReal, precioDescontado, comisionVenta,numEntradasRest)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, espectaculo.getNombre());
            preparedStatement.setString(i++, espectaculo.getDescripcion());
            Timestamp fechaEspectaculo = espectaculo.getFechaEspectaculo() != null ? new Timestamp(
            		espectaculo.getFechaEspectaculo().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, fechaEspectaculo);
            preparedStatement.setInt(i++, espectaculo.getDuracion());
            Timestamp fechaLimReserva = espectaculo.getFechaLimReserva() != null ? new Timestamp(
                    espectaculo.getFechaLimReserva().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, fechaLimReserva);
            preparedStatement.setInt(i++, espectaculo.getMaxEntradas());
            preparedStatement.setFloat(i++, espectaculo.getPrecioReal());
            preparedStatement.setFloat(i++, espectaculo.getPrecioDescontado());
            preparedStatement.setFloat(i++, espectaculo.getComisionVenta());
            preparedStatement.setInt(i++, espectaculo.getNumEntradasRest());
            
            
            
            

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long idEspectaculo = resultSet.getLong(1);

            /* Return Espectaculo. */
            return new Espectaculo(idEspectaculo,espectaculo.getNombre(),espectaculo.getDescripcion(),espectaculo.getFechaEspectaculo(),
            		espectaculo.getDuracion(),espectaculo.getFechaLimReserva(),espectaculo.getMaxEntradas(),espectaculo.getPrecioReal(),
            		espectaculo.getPrecioDescontado(),espectaculo.getComisionVenta(),espectaculo.getNumEntradasRest());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
