package es.udc.ws.app.client.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.udc.ws.app.client.service.UserEspectaculoService;
import es.udc.ws.app.client.service.UserEspectaculoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.dto.UserEspectaculoDto;
import es.udc.ws.app.client.service.exceptions.FechaLimiteException;
import es.udc.ws.app.client.service.exceptions.NumEntradasException;
import es.udc.ws.util.exceptions.InputValidationException;

public class UserEspectaculoServiceClient {

    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        UserEspectaculoService userEspectaculoService =
                UserEspectaculoServiceFactory.getService();
        if("-book".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {1,4});

            // [book] UserEspectaculoServiceClient -book <idEspectaculo>, <email>,<numTarjetaCredito>,<numEntradas>

            try {
            	validateEmail("Email",args[2]);
                ClientReservaDto reserva= userEspectaculoService.book(Long.valueOf(args[1]),args[2], args[3],
                        Integer.valueOf(args[4]));

                System.out.println("Reserva con Id: " + reserva.getIdReserva() + " creada correctamente.");

            } catch (NumberFormatException | InputValidationException | NumEntradasException | FechaLimiteException ex ) {
                ex.printStackTrace(System.err);
                
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-findE".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [find Espectaculos by keywords] UserEspectaculoServiceClient -findE <keywords>

            try {
                List<UserEspectaculoDto> espectaculos = userEspectaculoService.findEspectaculoByKeywords(args[1]);
                System.out.println("Found " + espectaculos.size() +
                        " espectaculo(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < espectaculos.size(); i++) {
                    UserEspectaculoDto espectaculoDto = espectaculos.get(i);
                    System.out.println("Id: " + espectaculoDto.getIdEspectaculo() +
                            ", Nombre: " + espectaculoDto.getNombre() +
                            ", Fecha Inicio: " + fechaConverter(espectaculoDto.getFechaEspectaculo()) +
                            ", Fecha Fin: " + fechaConverter(espectaculoDto.getFechaFin()) +
                            ", Fecha Limite de Reserva: " + fechaConverter(espectaculoDto.getFechaLimReserva()) +                   
                            ", Precio: " + espectaculoDto.getPrecioReal() +
                            ", Precio Descontado: " + espectaculoDto.getPrecioDescontado() +
							", Nº entradas disponibles: " + espectaculoDto.getNumEntradasRest()+
							", Descripcion: " + espectaculoDto.getDescripcion());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-findB".equalsIgnoreCase(args[0])) {
        	 validateArgs(args, 2, new int[] {});

            // [find Book by User] UserEspectaculoClient -findB <email>

        	   try {
                   List<ClientReservaDto> reservas = userEspectaculoService.findBookByUser(args[1]); //
                   System.out.println("Found " + reservas.size() +
                           " reserva(s) with user email '" + args[1] + "'");
                   for (int i = 0; i < reservas.size(); i++) {
                       ClientReservaDto reservaDto = reservas.get(i);
                       System.out.println("Id Reserva: " +  reservaDto.getIdReserva() +
                               ", Id Espectaculo: " +  reservaDto.getIdEspectaculo() +
                               ", Fecha Reserva: " + fechaConverter(reservaDto.getFechaReserva()) +
                               ", Nº de Entradas: " + reservaDto.getNumEntradasTotal() +
                               ", Nº Tarjeta de Credito: " + reservaDto.getNumTarjetaCredito() +
                               ", Precio: " + reservaDto.getPrecio());
                   }
               } catch (Exception ex) {
                   ex.printStackTrace(System.err);
            }

        }
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
	 public static void validateEmail(String propertyName, String email) throws InputValidationException {
		 
		 if(email.indexOf("@") == -1){
			 throw new InputValidationException("Invalid "+propertyName+
						" value" );
			 }
		 }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [book] UserEspectaculoServiceClient -book <idEspectaculo>,<email>,<numTarjetaCredito>,<numEntradas>\n" +
                "    [find Espectaculos by keywords] UserEspectaculoServiceClient -findE <keywords>\n" +
                "    [find Book by User] UserEspectaculoClient -findB <email>\n");
    }
}
