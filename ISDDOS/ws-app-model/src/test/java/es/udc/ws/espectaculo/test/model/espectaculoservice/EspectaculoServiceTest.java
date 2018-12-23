package es.udc.ws.espectaculo.test.model.espectaculoservice;



import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.app.model.espectaculo.Espectaculo;
import es.udc.ws.app.model.espectaculo.SqlEspectaculoDao;
import es.udc.ws.app.model.espectaculo.SqlEspectaculoDaoFactory;
import es.udc.ws.app.model.espectaculoservice.EspectaculoService;
import es.udc.ws.app.model.espectaculoservice.EspectaculoServiceFactory;
import es.udc.ws.app.model.espectaculoservice.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.model.espectaculoservice.exceptions.BankCardNonValidException;
import es.udc.ws.app.model.espectaculoservice.exceptions.FechaLimiteException;
import es.udc.ws.app.model.espectaculoservice.exceptions.InvalidPriceException;
import es.udc.ws.app.model.espectaculoservice.exceptions.NumEntradasException;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;

import static es.udc.ws.app.model.util.ModelConstants.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class EspectaculoServiceTest {

	private final String USER_EMAIL = "ws-user@udc.es";
	private final String INVALID_CREDIT_CARD_NUMBER = "";
	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String WRONG_CREDIT_CARD_NUMBER = "1234567890123458";
	private static EspectaculoService espectaculoService = null;
	private final long NON_EXISTENT_ESPECTACULO_ID = -1;
	private final long NON_EXISTENT_RESERVA_ID = -1;
	private static SqlReservaDao reservaDao = null;
	private static SqlEspectaculoDao espectaculoDao = null;
	@BeforeClass
	public static void init() {

		/*
		 * Create a simple data source and add it to "DataSourceLocator" (this
		 * is needed to test "es.udc.ws.movies.model.movieservice.MovieService"
		 */
		DataSource dataSource = new SimpleDataSource();

		/* Add "dataSource" to "DataSourceLocator". */
		DataSourceLocator.addDataSource(ESPECTACULO_DATA_SOURCE, dataSource);

		espectaculoService = EspectaculoServiceFactory.getService();
		espectaculoDao = SqlEspectaculoDaoFactory.getDao();
		reservaDao = SqlReservaDaoFactory.getDao();

	}
	private Espectaculo getValidEspectaculo(String title) {
		
		Calendar fechaEspectaculo = Calendar.getInstance();
		fechaEspectaculo.set(2019,Calendar.FEBRUARY,1);
		
		Calendar fechaLimReserva = Calendar.getInstance();
		fechaLimReserva.set(2019,Calendar.JANUARY,30);
		
		return new Espectaculo(title,"Espectaculo description",fechaEspectaculo,100,fechaLimReserva,100,(float)10,(float)8,
				(float)0.5,100);
	}
	private Espectaculo getValidEspectaculo2(String descripcion) {
		
		Calendar fechaEspectaculo = Calendar.getInstance();
		fechaEspectaculo.set(2019,Calendar.FEBRUARY,1);
		
		Calendar fechaLimReserva = Calendar.getInstance();
		fechaLimReserva.set(2019,Calendar.JANUARY,30);
		
		return new Espectaculo("espectaculo nombre",descripcion,fechaEspectaculo,100,fechaLimReserva,100,(float)10,(float)8,
				(float)0.5,100);
	}
	
	private Espectaculo getValidEspectaculo() {
		return getValidEspectaculo("Espectaculo title");
	}

	private Espectaculo createEspectaculo(Espectaculo espectaculo) {

		Espectaculo addedEspectaculo = null;
		try {
			addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedEspectaculo;

	}
	private void removeEspectaculo(Long idEspectaculo) throws InstanceNotFoundException {
		
		DataSource dataSource = DataSourceLocator.getDataSource(ESPECTACULO_DATA_SOURCE);
		
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				espectaculoDao.remove(connection, idEspectaculo);

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
	
	private void removeReserva(Long idReserva) {

		DataSource dataSource = DataSourceLocator.getDataSource(ESPECTACULO_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				reservaDao.remove(connection, idReserva);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
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
	
	@Test
	public void testAddEspectaculoAndFindEspectaculo() throws InputValidationException, InstanceNotFoundException {

		Espectaculo espectaculo = getValidEspectaculo();
		Espectaculo addedEspectaculo = null;

		try {
		addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
		Espectaculo foundEspectaculo = espectaculoService.findEspectaculo(addedEspectaculo.getIdEspectaculo());
	
		assertEquals(addedEspectaculo, foundEspectaculo);
		} finally {
		// Clear Database
			if (addedEspectaculo!=null) {
				removeEspectaculo(addedEspectaculo.getIdEspectaculo());
		}
	}
		
	}
	@Test
	public void testAddInvalidEspectaculo() throws InstanceNotFoundException {

		Espectaculo espectaculo = getValidEspectaculo();
		Espectaculo addedEspectaculo = null;
		boolean exceptionCatched = false;

		try {
			// Check nombre not null
			espectaculo.setNombre(null);
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check nombre not empty
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setNombre("");
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check espectaculo duracion >= 0
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setDuracion((short) -1);
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check espectaculo duracion <= MAX_RUNTIME
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setDuracion((short) (MAX_RUNTIME + 1));
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check descripcion not null
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setDescripcion(null);
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check descripcion not null
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setDescripcion("");
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check precioReal >= 0
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setPrecioReal((float) -1);
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check precioReal <= MAX_PRICE
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setPrecioReal((float) (MAX_PRICE + 1));
			try {
				addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check espectaculo precioDescontado >= 0
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setPrecioDescontado((float) -1);
			try {
			addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
			exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check espectaculo precioDescontado <= precioReal
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setPrecioDescontado(espectaculo.getPrecioReal()+1);
			try {
			addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
			exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check espectaculo maxEntradas >= 0
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setMaxEntradas(-1);
			try {
			addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
			exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check espectaculo maxEntradas <= MAX_ENTRADAS
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setMaxEntradas(MAX_ENTRADAS + 1);
			try {
			addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
			exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check espectaculo numEntradasRest >= 0
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setNumEntradasRest(-1);
			try {
			addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
			exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check espectaculo numEntradasRest <= maxEntradas
			exceptionCatched = false;
			espectaculo = getValidEspectaculo();
			espectaculo.setNumEntradasRest(espectaculo.getMaxEntradas() + 1);
			try {
			addedEspectaculo = espectaculoService.addEspectaculo(espectaculo);
			} catch (InputValidationException e) {
			exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			

	  } finally {
			if (!exceptionCatched) {
				// Clear Database
				removeEspectaculo(addedEspectaculo.getIdEspectaculo());
			}
		}

	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentEspectaculo() throws InstanceNotFoundException {

		espectaculoService.findEspectaculo(NON_EXISTENT_ESPECTACULO_ID);

	}
	
	
	@Test
	public void testUpdateEspectaculo() throws InputValidationException, InstanceNotFoundException, InvalidPriceException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		try {
			Espectaculo espectaculoToUpdate = new Espectaculo(espectaculo.getIdEspectaculo(),"new Nombre","new description",espectaculo.getFechaEspectaculo(),100,
														espectaculo.getFechaLimReserva(),100,(float)10,(float)8,(float)0.5,100);
			espectaculoService.updateEspectaculo(espectaculoToUpdate);

			Espectaculo updatedEspectaculo = espectaculoService.findEspectaculo(espectaculo.getIdEspectaculo());
			
			
			assertEquals(espectaculoToUpdate, updatedEspectaculo);

		} finally {
			// Clear Database
			removeEspectaculo(espectaculo.getIdEspectaculo());
		}

	}
	
	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidEspectaculo() throws InputValidationException, InstanceNotFoundException, InvalidPriceException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		try {
			// Check movie title not null
			espectaculo = espectaculoService.findEspectaculo(espectaculo.getIdEspectaculo());
			espectaculo.setNombre(null);
			espectaculoService.updateEspectaculo(espectaculo);
		} finally {
			// Clear Database
			removeEspectaculo(espectaculo.getIdEspectaculo());
		}

	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentEspectaculo() throws InputValidationException, InstanceNotFoundException, InvalidPriceException {

		Espectaculo espectaculo = getValidEspectaculo();
		espectaculo.setIdEspectaculo(NON_EXISTENT_ESPECTACULO_ID);
		espectaculo.setNombre("espectaculo ejemplo");
		espectaculoService.updateEspectaculo(espectaculo);

	}
	
	@Test(expected = InvalidPriceException.class)
	public void testUpdateInvalidPrecioDescontado() throws InputValidationException, InstanceNotFoundException, InvalidPriceException, NumEntradasException, FechaLimiteException {
		
		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		Reserva reserva = null;
		try {
		reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,2);
		espectaculo.setPrecioDescontado(5);
		espectaculoService.updateEspectaculo(espectaculo);
		} finally {
			if (reserva != null) {
				removeReserva(reserva.getIdReserva());
			}
			removeEspectaculo(espectaculo.getIdEspectaculo());
			
		}
	}

	
	@Test
	public void testFindEspectaculos() throws InstanceNotFoundException {

	
		List<Espectaculo> espectaculos = new LinkedList<Espectaculo>();
		Espectaculo espectaculo1 = createEspectaculo(getValidEspectaculo2("espectaculo descripcion 1"));
		espectaculos.add(espectaculo1);
		Espectaculo espectaculo2 = createEspectaculo(getValidEspectaculo2("espectaculo descripcion 2"));
		espectaculos.add(espectaculo2);
		Espectaculo espectaculo3 = createEspectaculo(getValidEspectaculo2("espectaculo descripcion 3"));
		espectaculos.add(espectaculo3);
	

		try {
			List<Espectaculo> foundEspectaculos = espectaculoService.findEspectaculoByKeywords("especTaculo descripcion",null,null);
			assertEquals(espectaculos, foundEspectaculos);

			foundEspectaculos = espectaculoService.findEspectaculoByKeywords("Es descripcion 2",null,null);
			assertEquals(1, foundEspectaculos.size());
			assertEquals(espectaculos.get(1), foundEspectaculos.get(0));
			
			foundEspectaculos = espectaculoService.findEspectaculoByKeywords("descripcion 5",null,null);
			assertEquals(0, foundEspectaculos.size());
			
			Calendar fechaEspectaculo = Calendar.getInstance();
			fechaEspectaculo.set(2019,Calendar.JANUARY,31);
			Calendar fechaEspectaculo2 = Calendar.getInstance();
			fechaEspectaculo2.set(2019,Calendar.FEBRUARY,3);
		    foundEspectaculos = espectaculoService.findEspectaculoByKeywords(null,fechaEspectaculo,fechaEspectaculo2);		
			assertEquals(espectaculos, foundEspectaculos);
			
			
			foundEspectaculos = espectaculoService.findEspectaculoByKeywords("espectaCulo descripcion",fechaEspectaculo,fechaEspectaculo2);
			assertEquals(espectaculos, foundEspectaculos);
			
			
		} finally {
			// Clear Database
			for (Espectaculo espectaculo : espectaculos) {
				removeEspectaculo(espectaculo.getIdEspectaculo());
			}
		}

	}
	@Test
	public void testBookAndFindReserva()
			throws InstanceNotFoundException, InputValidationException, NumEntradasException, FechaLimiteException{

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		Reserva reserva = null;

		try {


			reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,2);
			
			List<Reserva> foundReserva = espectaculoService.findBookByUser(USER_EMAIL);
			assertEquals(reserva.getFechaReserva(),foundReserva.get(0).getFechaReserva());
	
			assertEquals(reserva, foundReserva.get(0));
			assertEquals(VALID_CREDIT_CARD_NUMBER, foundReserva.get(0).getNumTarjetaCredito());
			assertEquals(USER_EMAIL, foundReserva.get(0).getEmail());
			assertEquals(espectaculo.getIdEspectaculo(), foundReserva.get(0).getIdEspectaculo());
			assertTrue(espectaculo.getPrecioReal()*foundReserva.get(0).getNumEntradasTotal()==foundReserva.get(0).getPrecio());
			assertTrue(Calendar.getInstance().after(foundReserva.get(0).getFechaReserva()));


		} finally {
			if (reserva != null) {
				removeReserva(reserva.getIdReserva());
			}
			removeEspectaculo(espectaculo.getIdEspectaculo());
		}

	}
	
	@Test(expected = InputValidationException.class)
	public void testBookWithInvalidCreditCard() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		try {
			Reserva reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL,INVALID_CREDIT_CARD_NUMBER,1);
			removeReserva(reserva.getIdReserva());
		} finally {
			/* Clear database. */
			removeEspectaculo(espectaculo.getIdEspectaculo());
		}

	}
	@Test(expected = InstanceNotFoundException.class)
	public void testBookNonExistentEspectaculo() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException {

		Reserva reserva = espectaculoService.book(NON_EXISTENT_ESPECTACULO_ID, USER_EMAIL, VALID_CREDIT_CARD_NUMBER,1);
		/* Clear database. */
		removeReserva(reserva.getIdReserva());

	}

	@Test(expected = NumEntradasException.class)
	public void testBookWithNotEnoughTickets() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		try {
			Reserva reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL,VALID_CREDIT_CARD_NUMBER,200);
			removeReserva(reserva.getIdReserva());
		} finally {
			/* Clear database. */
			removeEspectaculo(espectaculo.getIdEspectaculo());
		}

	}
	
    @Test(expected = FechaLimiteException.class)
	public void testBookWithFechaLimiteExceed() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException, InvalidPriceException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		Calendar fechaEspectaculo = Calendar.getInstance();
		fechaEspectaculo.set(2017,Calendar.JANUARY,31);
		try {

			espectaculo.setFechaLimReserva(fechaEspectaculo);
			espectaculoService.updateEspectaculo(espectaculo);
			Reserva reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL,VALID_CREDIT_CARD_NUMBER,200);
			removeReserva(reserva.getIdReserva());
		} finally {		
			removeEspectaculo(espectaculo.getIdEspectaculo());
		}

	}
   
    @Test
    public void testCheckBook() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException, BookAlreadyUsedException, BankCardNonValidException{
    	Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
    	Reserva reserva=null;
		try {


			reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,2);
			
			assertEquals(reserva.getCheck(),false);
			assertEquals(reserva.getNumTarjetaCredito(),VALID_CREDIT_CARD_NUMBER);
			espectaculoService.checkBook(reserva.getIdReserva(), VALID_CREDIT_CARD_NUMBER);
			List<Reserva> reservas = espectaculoService.findBookByUser(reserva.getEmail());
			assertEquals(reservas.get(0).getCheck(),true);
			
		} finally {
			if (reserva != null) {
				removeReserva(reserva.getIdReserva());
			}
			removeEspectaculo(espectaculo.getIdEspectaculo());
		}
    	
    }
	@Test(expected = InputValidationException.class)
	public void testcheckBookWithInvalidCreditCard() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException, BookAlreadyUsedException, BankCardNonValidException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		Reserva reserva=null;
		try {
			reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1);
			espectaculoService.checkBook(reserva.getIdReserva(), INVALID_CREDIT_CARD_NUMBER);
			
		} finally {
			/* Clear database. */
			if (reserva != null) {
				removeReserva(reserva.getIdReserva());
			}
			removeEspectaculo(espectaculo.getIdEspectaculo());
			
		}

	}
	

	@Test(expected = BankCardNonValidException.class)
	public void testcheckBookWithWrongCreditCard() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException, BookAlreadyUsedException, BankCardNonValidException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		Reserva reserva=null;
		try {
			reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1);
			espectaculoService.checkBook(reserva.getIdReserva(), WRONG_CREDIT_CARD_NUMBER);
				
		} finally {
				/* Clear database. */
			if (reserva != null) {
				removeReserva(reserva.getIdReserva());
			}
			removeEspectaculo(espectaculo.getIdEspectaculo());
				
		}

	}
	@Test(expected = InstanceNotFoundException.class)
	public void testcheckBookInexistent() throws InputValidationException, InstanceNotFoundException, BookAlreadyUsedException, BankCardNonValidException {
		espectaculoService.checkBook(NON_EXISTENT_RESERVA_ID, VALID_CREDIT_CARD_NUMBER);
	}
	@Test(expected = BookAlreadyUsedException.class)
	public void testcheckBookAlreadyChecked() throws InputValidationException, InstanceNotFoundException, NumEntradasException, FechaLimiteException, BookAlreadyUsedException, BankCardNonValidException {

		Espectaculo espectaculo = createEspectaculo(getValidEspectaculo());
		Reserva reserva=null;
		try {
			reserva = espectaculoService.book(espectaculo.getIdEspectaculo(), USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1);
			espectaculoService.checkBook(reserva.getIdReserva(), VALID_CREDIT_CARD_NUMBER);
			espectaculoService.checkBook(reserva.getIdReserva(), VALID_CREDIT_CARD_NUMBER);
				
		} finally {
				/* Clear database. */
			if (reserva != null) {
				removeReserva(reserva.getIdReserva());
			}
			removeEspectaculo(espectaculo.getIdEspectaculo());
				
		}

	}
	
	
	

    

	
	
}
