package es.udc.ws.app.model.reserva;

import java.util.Calendar;

public class Reserva {
	private Long idReserva;
	private Long idEspectaculo;
	private String email;
	private String numTarjetaCredito;
	private float precio;
	private Calendar fechaReserva;
	private int numEntradasTotal;
	private boolean check;
	
	

	public Reserva(Long idEspectaculo, String email, String numTarjetaCredito, float precio,
			Calendar fechaReserva, int numEntradasTotal, boolean check) {
		this.idEspectaculo = idEspectaculo;
		this.email = email;
		this.numTarjetaCredito = numTarjetaCredito;
		this.precio = precio;
		this.fechaReserva = fechaReserva;
		if (fechaReserva != null) {
			this.fechaReserva.set(Calendar.MILLISECOND, 0);
		}
		this.numEntradasTotal = numEntradasTotal;
		this.check=check;
	}

	public Reserva(Long idReserva, Long idEspectaculo, String email, String numTarjetaCredito, float precio,
			Calendar fechaReserva, int numEntradasTotal, boolean check) {
		this(idEspectaculo, email, numTarjetaCredito, precio, fechaReserva,
                numEntradasTotal,check);
		this.idReserva = idReserva;
		
	}

	public void setCheck() {
		this.check=true;
	}
	public boolean getCheck() {
		return check;
	}
	public Long getIdReserva() {
		return idReserva;
	}
	public void setIdReserva(Long idReserva) {
		this.idReserva = idReserva;
	}
	public Long getIdEspectaculo() {
		return idEspectaculo;
	}
	public void setIdEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumTarjetaCredito() {
		return numTarjetaCredito;
	}
	public void setNumTarjetaCredito(String numTarjetaCredito) {
		this.numTarjetaCredito = numTarjetaCredito;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	public Calendar getFechaReserva() {
		return fechaReserva;
	}
	public void setFechaReserva(Calendar fechaReserva) {
		this.fechaReserva = fechaReserva;
		if (fechaReserva != null) {
			this.fechaReserva.set(Calendar.MILLISECOND, 0);
		}
	}
	public int getNumEntradasTotal() {
		return numEntradasTotal;
	}
	public void setNumEntradasRes(int numEntradasRes) {
		this.numEntradasTotal = numEntradasRes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (check ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fechaReserva == null) ? 0 : fechaReserva.hashCode());
		result = prime * result + ((idEspectaculo == null) ? 0 : idEspectaculo.hashCode());
		result = prime * result + ((idReserva == null) ? 0 : idReserva.hashCode());
		result = prime * result + numEntradasTotal;
		result = prime * result + ((numTarjetaCredito == null) ? 0 : numTarjetaCredito.hashCode());
		result = prime * result + Float.floatToIntBits(precio);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reserva other = (Reserva) obj;
		if (check != other.check)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fechaReserva == null) {
			if (other.fechaReserva != null)
				return false;
		} else if (!fechaReserva.equals(other.fechaReserva))
			return false;
		if (idEspectaculo == null) {
			if (other.idEspectaculo != null)
				return false;
		} else if (!idEspectaculo.equals(other.idEspectaculo))
			return false;
		if (idReserva == null) {
			if (other.idReserva != null)
				return false;
		} else if (!idReserva.equals(other.idReserva))
			return false;
		if (numEntradasTotal != other.numEntradasTotal)
			return false;
		if (numTarjetaCredito == null) {
			if (other.numTarjetaCredito != null)
				return false;
		} else if (!numTarjetaCredito.equals(other.numTarjetaCredito))
			return false;
		if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio))
			return false;
		return true;
	}
	
	
	
}
