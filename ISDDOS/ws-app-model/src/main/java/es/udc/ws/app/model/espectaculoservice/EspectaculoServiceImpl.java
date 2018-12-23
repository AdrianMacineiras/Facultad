package es.udc.ws.app.model.espectaculoservice;

import es.udc.ws.app.model.espectaculo.Espectaculo;
import es.udc.ws.app.model.espectaculo.SqlEspectaculoDao;
import es.udc.ws.app.model.espectaculo.SqlEspectaculoDaoFactory;
import es.udc.ws.app.model.espectaculoservice.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.model.espectaculoservice.exceptions.BankCardNonValidException;
import es.udc.ws.app.model.espectaculoservice.exceptions.FechaLimiteException;
import es.udc.ws.app.model.espectaculoservice.exceptions.InvalidPriceException;

import es.udc.ws.app.model.espectaculoservice.exceptions.NumEntradasException;

import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;


import static es.udc.ws.app.model.util.ModelConstants.*;
import es.udc.ws.app.model.util.OwnPropertyValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

public class EspectaculoServiceImpl  implements EspectaculoService{
	private final DataSource dataSource;
	private SqlEspectaculoDao espectaculoDao = null;
	private SqlReservaDao reservaDao = null;

	public EspectaculoServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(ESPECTACULO_DATA_SOURCE);
		espectaculoDao = SqlEspectaculoDaoFactory.getDao();
		reservaDao = SqlReservaDaoFactory.getDao();
	}

	private void validateEspectaculo(Espectaculo espectaculo) throws InputValidationException {

		PropertyValidator.validateMandatoryString("nombre", espectaculo.getNombre());
		PropertyValidator.validateLong("duracion", espectaculo.getDuracion(), 0, MAX_RUNTIME);
		PropertyValidator.validateMandatoryString("descripcion", espectaculo.getDescripcion());
		PropertyValidator.validateDouble("precioReal", espectaculo.getPrecioReal(), 0, MAX_PRICE);
		OwnPropertyValidator.validateDouble("precioReal", espectaculo.getPrecioReal(), espectaculo.getPrecioDescontado());
		PropertyValidator.validateDouble("precioDescontado", espectaculo.getPrecioDescontado(), 0, espectaculo.getPrecioReal());
		OwnPropertyValidator.validateInt("maxEntradas", espectaculo.getMaxEntradas(), 0, MAX_ENTRADAS);
		OwnPropertyValidator.validateInt("numEntradasRest", espectaculo.getNumEntradasRest(), 0, espectaculo.getMaxEntradas());
		OwnPropertyValidator.validateFechaLimite("fechaLimReserva", espectaculo.getFechaLimReserva() , espectaculo.getFechaEspectaculo());
		
	}

	
	
	@Override
	public Espectaculo addEspectaculo(Espectaculo espectaculo) throws InputValidationException {

		validateEspectaculo(espectaculo);
		espectaculo.setNumEntradasRest(espectaculo.getMaxEntradas());

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Espectaculo createdEspectaculo = espectaculoDao.create(connection, espectaculo);

				/* Commit. */
				connection.commit();

				return createdEspectaculo;

			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public void updateEspectaculo(Espectaculo espectaculo) throws InputValidationException, InstanceNotFoundException,InvalidPriceException{

		validateEspectaculo(espectaculo);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				/* Do work. */
				Espectaculo espectaculoFound = espectaculoDao.find(connection, espectaculo.getIdEspectaculo());
				if(espectaculo.getMaxEntradas()<= (espectaculoFound.getMaxEntradas()-espectaculo.getNumEntradasRest())) {
                    throw new InputValidationException("No se ha podido actualizar el espectaculo. El aforo debe ser mayor.");
                }
                
                espectaculo.setNumEntradasRest(espectaculo.getMaxEntradas()-(espectaculoFound.getMaxEntradas()-espectaculoFound.getNumEntradasRest()));

				if (espectaculoFound.getNumEntradasRest() < espectaculoFound.getMaxEntradas()) {
					if(espectaculo.getPrecioDescontado()<espectaculoFound.getPrecioDescontado()) {
						throw new InvalidPriceException(espectaculoFound.getIdEspectaculo(),espectaculo.getPrecioDescontado(),espectaculoFound.getPrecioDescontado());
					}
				}

				espectaculoDao.update(connection, espectaculo);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	public Espectaculo findEspectaculo(Long idEspectaculo) throws InstanceNotFoundException {

		try (Connection connection = dataSource.getConnection()) {
			return espectaculoDao.find(connection, idEspectaculo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	public List<Espectaculo> findEspectaculoByKeywords(String keywords, Calendar fechaInicio,Calendar fechaLimite){

		try (Connection connection = dataSource.getConnection()) {
			return espectaculoDao.findByKeywords(connection, keywords,fechaInicio,fechaLimite);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Reserva> findBookByUser(String email){

		try (Connection connection = dataSource.getConnection()) {
			return reservaDao.findByUser(connection, email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
    public Reserva book(Long idEspectaculo, String email, String numTarjetaCredito, int numEntradas)
            throws InstanceNotFoundException, InputValidationException,NumEntradasException,FechaLimiteException{

		
		PropertyValidator.validateCreditCard(numTarjetaCredito);
				
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Espectaculo espectaculo = espectaculoDao.find(connection, idEspectaculo);
				Calendar fechaReserva = Calendar.getInstance(); //coge fecha
				fechaReserva.set(Calendar.MILLISECOND, 0);

				if (fechaReserva.after(espectaculo.getFechaLimReserva())) {
					throw new FechaLimiteException(espectaculo.getIdEspectaculo(),espectaculo.getFechaLimReserva(),fechaReserva);
				}
				
			
				if (numEntradas > espectaculo.getNumEntradasRest()) {
					throw new NumEntradasException(espectaculo.getIdEspectaculo(),espectaculo.getNumEntradasRest());				
				}
				else {
					espectaculo.setNumEntradasRest(espectaculo.getNumEntradasRest()-numEntradas);
					espectaculoDao.update(connection, espectaculo);
				}
				
				
				Reserva reserva = reservaDao.create(connection, new Reserva(idEspectaculo,email,numTarjetaCredito,
						espectaculo.getPrecioReal() * numEntradas, fechaReserva, numEntradas,false));
						

				/* Commit. */
				connection.commit();

				return reserva;

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void checkBook(Long idReserva, String numTarjetaCredito) throws InputValidationException,
			InstanceNotFoundException, BookAlreadyUsedException, BankCardNonValidException {

		
		Reserva reserva;
		
		PropertyValidator.validateCreditCard(numTarjetaCredito);
		
		try (Connection connection = dataSource.getConnection()) {
			
			try {
			
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				
				reserva = reservaDao.find(connection, idReserva);

				if(!reserva.getNumTarjetaCredito().equals(numTarjetaCredito)){
					throw new BankCardNonValidException(idReserva,numTarjetaCredito);
				}
				if(reserva.getCheck()) {
					throw new BookAlreadyUsedException(idReserva);
				}
				
				reserva.setCheck();
				reservaDao.update(connection, reserva);
				connection.commit();
				
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


}

