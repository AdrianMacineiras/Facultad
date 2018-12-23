package es.udc.ws.app.client.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.udc.ws.app.client.service.AdminEspectaculoService;
import es.udc.ws.app.client.service.AdminEspectaculoServiceFactory;
import es.udc.ws.app.client.service.dto.AdminEspectaculoDto;
import es.udc.ws.app.client.service.exceptions.BankCardNonValidException;
import es.udc.ws.app.client.service.exceptions.BookAlreadyUsedException;
import es.udc.ws.app.client.service.exceptions.InvalidPriceException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class AdminEspectaculoServiceClient {
	public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        AdminEspectaculoService adminEspectaculoService =
               AdminEspectaculoServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 10, new int[] {4,6,7,8,9});
            
            // [add] AdminEspectaculoServiceClient -a <nombre> <descripcion> <fechaEspectaculo> <duracion>
            //<fechaLimReserva> <maxEntradas> <precioReal> <precioDescontado> <comisionVenta>

            try {
                Long espectaculoId = adminEspectaculoService.addEspectaculo(new AdminEspectaculoDto(
                        args[1], args[2], stringConverter(args[3]), Integer.valueOf(args[4]),
                        stringConverter(args[5]), Integer.valueOf(args[6]), Float.valueOf(args[7]),
                        Float.valueOf(args[8]),Float.valueOf(args[9])));

                System.out.println("Espectaculo con ID:  " + espectaculoId + " creado correctamente.");

            } catch (NumberFormatException | InputValidationException ex ) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-u".equalsIgnoreCase(args[0])) {
        	validateArgs(args, 11, new int[] {1,5,7,8,9,10});
           
        	// [update] AdminEspectaculoServiceClient -u <idEspectaculo> <nombre> <descripcion> <fechaEspectaculo> 
        	// <duracion> <fechaLimReserva> <maxEntradas> <precioReal> <precioDescontado> <comisionVenta>

           try {
        	   adminEspectaculoService.updateEspectaculo(new AdminEspectaculoDto(Long.valueOf(args[1]),
                       args[2], args[3], stringConverter(args[4]), Integer.valueOf(args[5]),
                       stringConverter(args[6]), Integer.valueOf(args[7]), Float.valueOf(args[8]),
                       Float.valueOf(args[9]),Float.valueOf(args[10])));

                System.out.println("Espectaculo con ID: " + args[1] + " actualizado correctamente");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException | InvalidPriceException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-g".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [get] AdminEspectaculoServiceClient -g <espectaculoId>

            try {                
                AdminEspectaculoDto espectaculoDto = adminEspectaculoService.getEspectaculo(Long.parseLong(args[1]));
                System.out.println("Id: " + espectaculoDto.getIdEspectaculo() +
                        ", Nombre: " + espectaculoDto.getNombre() +
                        ", Fecha Inicio: " + fechaConverter(espectaculoDto.getFechaEspectaculo()) +
                        ", Duracion: " + espectaculoDto.getDuracion() +
                        ", Fecha Limite de Reserva: " + fechaConverter(espectaculoDto.getFechaLimReserva()) +                   
                        ", Precio: " + espectaculoDto.getPrecioReal() +
                        ", Precio Descontado: " + espectaculoDto.getPrecioDescontado() +
						", Nº entradas disponibles: " + espectaculoDto.getNumEntradasRest()+
						", Descripcion: " + espectaculoDto.getDescripcion());
                
            } catch (NumberFormatException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-c".equalsIgnoreCase(args[0])) {
        	validateArgs(args, 3, new int[] {1});
        	
        	 // [checkbook] AdminEspectaculoServiceClient -c <idReserva> <creditCardNumber>
        	
        	try {
        		
        		adminEspectaculoService.checkBook(Long.valueOf(args[1]), args[2]);
        		System.out.println("Reserva con ID: " + args[1] + " se ha marcado correctamente");
        		
        	}catch (NumberFormatException | InstanceNotFoundException | BookAlreadyUsedException | BankCardNonValidException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }

    }
	
    public static Calendar stringConverter(String fecha) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        Date date = sdf.parse(fecha);
        Calendar fechaConvertida = Calendar.getInstance();
        fechaConvertida.setTime(date);
        return fechaConvertida;
    }

    public static String fechaConverter(Calendar fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        String fechaConvertida = sdf.format(fecha.getTime());
        return fechaConvertida;
    }
    
    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]    MovieServiceClient -a <title> <hours> <minutes> <description> <price>\n" +
                "    [remove] MovieServiceClient -r <movieId>\n" +
                "    [update] MovieServiceClient -u <movieId> <title> <hours> <minutes> <description> <price>\n" +
                "    [find]   MovieServiceClient -f <keywords>\n" +
                "    [buy]    MovieServiceClient -b <movieId> <userId> <creditCardNumber>\n" +
                "    [get]    MovieServiceClient -g <saleId>\n");
    }

}
