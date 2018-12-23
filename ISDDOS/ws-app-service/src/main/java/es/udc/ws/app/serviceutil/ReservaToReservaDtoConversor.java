package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;


import es.udc.ws.app.dto.ServiceReservaDto;

import es.udc.ws.app.model.reserva.Reserva;

public class ReservaToReservaDtoConversor {
	public static List<ServiceReservaDto> toReservasDtos(List<Reserva> reservas) {
		List<ServiceReservaDto> reservaDtos = new ArrayList<>(reservas.size());
		for (int i = 0; i < reservas.size(); i++) {
			Reserva reserva = reservas.get(i);
			reservaDtos.add(toReservaDto(reserva));
		}
		return reservaDtos;
	}

	public static ServiceReservaDto toReservaDto(Reserva reserva) {
		return new ServiceReservaDto(reserva.getIdReserva(),reserva.getIdEspectaculo(), reserva.getEmail(), reserva.getNumTarjetaCredito(),
				reserva.getPrecio(),reserva.getFechaReserva(),reserva.getNumEntradasTotal(), reserva.getCheck());
	}
}
