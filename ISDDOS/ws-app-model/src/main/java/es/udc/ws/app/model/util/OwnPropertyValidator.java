package es.udc.ws.app.model.util;

import java.util.Calendar;

import es.udc.ws.util.exceptions.InputValidationException;

public class OwnPropertyValidator {
	 public static void validateInt(String propertyName,
	            int value, int lowerValidLimit, int upperValidLimit)
	            throws InputValidationException {
		 	
	        if ( (value < lowerValidLimit) || (value > upperValidLimit) ) {
	            throw new InputValidationException("Invalid " + propertyName +
	                    " value (it must be greater than " + lowerValidLimit +
	                    " and lower than " + upperValidLimit + "): " + value);
	        }
	       

	    }
	 
	 public static void validateFechaLimite(String propertyName, Calendar fechaLimite, Calendar fechaEspectaculo) throws InputValidationException{
		
         if(fechaLimite.after(fechaEspectaculo)) {
             throw new InputValidationException("Invalid " + propertyName +
                        " value (fechaLimite it must be less than fechaEspectaculo") ;
         }
	 }
	 
	 public static void validateDouble(String propertyName, double doubleValue, double lowerValidLimit) throws InputValidationException{
		 if (doubleValue < lowerValidLimit ) {
			 throw new InputValidationException("Invalid "+propertyName+
					" value (it must be greater than "+lowerValidLimit+"): " + doubleValue );
		 }
	 }
	 

	 
}
