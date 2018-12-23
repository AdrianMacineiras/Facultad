package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceEspectaculoEntradaDto;
import es.udc.ws.app.model.espectaculo.Espectaculo;



public class EspectaculoToEspectaculoEntradaDtoConversor {
	public static List<ServiceEspectaculoEntradaDto> toEspectaculoEntradaDtos(List<Espectaculo> espectaculos) {
		List<ServiceEspectaculoEntradaDto> espectaculoEntradaDtos = new ArrayList<>(espectaculos.size());
		for (int i = 0; i < espectaculos.size(); i++) {
			Espectaculo espectaculo = espectaculos.get(i);
			espectaculoEntradaDtos.add(toEspectaculoEntradaDto(espectaculo));
		}
		return espectaculoEntradaDtos;
	}

	public static ServiceEspectaculoEntradaDto toEspectaculoEntradaDto(Espectaculo espectaculo) {
		return new ServiceEspectaculoEntradaDto(espectaculo.getIdEspectaculo(), espectaculo.getNombre(), espectaculo.getDescripcion(),
				espectaculo.getFechaEspectaculo(),espectaculo.getDuracion(), espectaculo.getFechaLimReserva(), espectaculo.getMaxEntradas(), 
				espectaculo.getPrecioReal(),espectaculo.getPrecioDescontado(), espectaculo.getComisionVenta(), espectaculo.getNumEntradasRest());
	}

	public static Espectaculo toEspectaculo(ServiceEspectaculoEntradaDto espectaculo) {
		return new Espectaculo(espectaculo.getIdEspectaculo(), espectaculo.getNombre(), espectaculo.getDescripcion(),
				espectaculo.getFechaEspectaculo(),espectaculo.getDuracion(), espectaculo.getFechaLimReserva(), espectaculo.getMaxEntradas(), 
				espectaculo.getPrecioReal(),espectaculo.getPrecioDescontado(), espectaculo.getComisionVenta(), espectaculo.getNumEntradasRest());
	}
}
