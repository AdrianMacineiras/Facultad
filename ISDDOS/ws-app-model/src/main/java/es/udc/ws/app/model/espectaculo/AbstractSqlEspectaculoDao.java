package es.udc.ws.app.model.espectaculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlEspectaculoDao implements SqlEspectaculoDao {
	protected AbstractSqlEspectaculoDao() {
    }
    
	
	@Override
	public Espectaculo find(Connection connection, Long idEspectaculo)
           throws InstanceNotFoundException{

        /* Create "queryString". */
        String queryString = "SELECT nombre, descripcion, "
                + " fechaEspectaculo, duracion, fechaLimReserva, "
                + "maxEntradas, precioReal, precioDescontado, comisionVenta,"
                + "numEntradasRest FROM Espectaculo WHERE idEspectaculo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, idEspectaculo.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(idEspectaculo,
                        Espectaculo.class.getName());
            }

            /* Get results. */
            i = 1;
            String nombre = resultSet.getString(i++);
            String descripcion = resultSet.getString(i++);
            Calendar fechaEspectaculo = Calendar.getInstance();
            fechaEspectaculo.setTime(resultSet.getTimestamp(i++));
            int duracion = resultSet.getInt(i++);
            Calendar fechaLimReserva = Calendar.getInstance();
            fechaLimReserva.setTime(resultSet.getTimestamp(i++));
            int maxEntradas = resultSet.getInt(i++);
            float precioReal = resultSet.getFloat(i++);
            float precioDescontado = resultSet.getFloat(i++);
            float comisionVenta = resultSet.getFloat(i++);
            int numEntradasRest = resultSet.getInt(i++);
            
            

            /* Return espectaculo. */
            return new Espectaculo(idEspectaculo, nombre,descripcion,fechaEspectaculo,duracion,fechaLimReserva,
            		maxEntradas,precioReal,precioDescontado,comisionVenta,numEntradasRest);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
	
	
    @Override
    public List<Espectaculo> findByKeywords(Connection connection, String keywords, Calendar fechaInicio, Calendar fechaLimite) {

        /* Create "queryString". */
        String[] words = keywords != null ? keywords.split(" ") : null;
        String queryString = "SELECT idEspectaculo,nombre, descripcion, "
                + " fechaEspectaculo, duracion, fechaLimReserva, "
                + "maxEntradas, precioReal, precioDescontado, comisionVenta,"
                + "numEntradasRest FROM Espectaculo";

        //keywords != null
        if (words != null && words.length > 0) {
            queryString += " WHERE";
            for (int i = 0; i < words.length; i++) {
                if (i > 0) {
                    queryString += " AND";
                }
                queryString += " LOWER(descripcion) LIKE LOWER(?)";
            }
        }
        //fechas !=null
        if(fechaInicio !=null && fechaLimite != null) {
        	String pattern = "yyyyMMdd";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	
        	//keywords y fechas != nulll
        	 if (words != null && words.length > 0) {
        		 queryString += " AND (fechaEspectaculo BETWEEN "+simpleDateFormat.format(fechaInicio.getTime())+" AND "+simpleDateFormat.format(fechaLimite.getTime())+")";
        	 }
        	 else {  //keywords null y fechas !=null	
        		 queryString += " WHERE (fechaEspectaculo BETWEEN "+simpleDateFormat.format(fechaInicio.getTime())+" AND "+simpleDateFormat.format(fechaLimite.getTime())+")"; 
        	 }
        }
        
        queryString += " ORDER BY nombre";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            if (words != null) {
                /* Fill "preparedStatement". */
                for (int i = 0; i < words.length; i++) {
                    preparedStatement.setString(i + 1, "%" + words[i] + "%");
                }
            }

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read espectaculo. */
            List<Espectaculo> espectaculos = new ArrayList<Espectaculo>();

            while (resultSet.next()) {

            	 int i = 1;
            	 Long idEspectaculo = new Long(resultSet.getLong(i++));
                 String nombre = resultSet.getString(i++);
                 String descripcion = resultSet.getString(i++);
                 Calendar fechaEspectaculo = Calendar.getInstance();
                 fechaEspectaculo.setTime(resultSet.getTimestamp(i++));
                 int duracion = resultSet.getInt(i++);
                 Calendar fechaLimReserva = Calendar.getInstance();
                 fechaLimReserva.setTime(resultSet.getTimestamp(i++));
                 int maxEntradas = resultSet.getInt(i++);
                 float precioReal = resultSet.getFloat(i++);
                 float precioDescontado = resultSet.getFloat(i++);
                 float comisionVenta = resultSet.getFloat(i++);
                 int numEntradasRest = resultSet.getInt(i++);
                

                espectaculos.add(new Espectaculo(idEspectaculo, nombre,descripcion,fechaEspectaculo,duracion,fechaLimReserva,
                		maxEntradas,precioReal,precioDescontado,comisionVenta,numEntradasRest));

            }

            /* Return movies. */
            return espectaculos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }	

    @Override
    public void update(Connection connection, Espectaculo espectaculo)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Espectaculo"
                + " SET nombre = ? , descripcion = ?," + 
                "fechaEspectaculo = ?, duracion = ?, fechaLimReserva = ?," + 
                "maxEntradas = ?, precioReal = ?, precioDescontado = ?, comisionVenta = ?," + 
                "numEntradasRest = ? WHERE idEspectaculo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

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
            preparedStatement.setLong(i++, espectaculo.getIdEspectaculo());
            


            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(espectaculo.getIdEspectaculo(),
                        Espectaculo.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }	

    @Override
    public void remove(Connection connection, Long idEspectaculo)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Espectaculo WHERE" + " idEspectaculo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, idEspectaculo);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(idEspectaculo,
                        Espectaculo.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
	
}
