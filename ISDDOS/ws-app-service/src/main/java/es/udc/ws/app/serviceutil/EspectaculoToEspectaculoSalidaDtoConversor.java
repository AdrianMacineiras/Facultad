package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;
import es.udc.ws.app.dto.ServiceEspectaculoSalidaDto;
import es.udc.ws.app.model.espectaculo.Espectaculo;

public class EspectaculoToEspectaculoSalidaDtoConversor {
	public static List<ServiceEspectaculoSalidaDto> toEspectaculoSalidaDtos(List<Espectaculo> espectaculos) {
		List<ServiceEspectaculoSalidaDto> espectaculoSalidaDtos = new ArrayList<>(espectaculos.size());
		for (int i = 0; i < espectaculos.size(); i++) {
			Espectaculo espectaculo = espectaculos.get(i);
			espectaculoSalidaDtos.add(toEspectaculoSalidaDto(espectaculo));
		}
		return espectaculoSalidaDtos;
	}

	public static ServiceEspectaculoSalidaDto toEspectaculoSalidaDto(Espectaculo espectaculo) {
		return new ServiceEspectaculoSalidaDto(espectaculo.getIdEspectaculo(), espectaculo.getNombre(), espectaculo.getDescripcion(),
				espectaculo.getFechaEspectaculo(),espectaculo.getDuracion(), espectaculo.getFechaLimReserva(), 
				espectaculo.getPrecioReal(),espectaculo.getPrecioDescontado(), espectaculo.getNumEntradasRest());
	}

	public static Espectaculo toEspectaculo(ServiceEspectaculoSalidaDto espectaculo) {
		return new Espectaculo(espectaculo.getIdEspectaculo(), espectaculo.getNombre(), espectaculo.getDescripcion(),
				espectaculo.getFechaEspectaculo(),espectaculo.getDuracion(), espectaculo.getFechaLimReserva(), 
				espectaculo.getPrecioReal(),espectaculo.getPrecioDescontado(), espectaculo.getNumEntradasRest());
	}

}
