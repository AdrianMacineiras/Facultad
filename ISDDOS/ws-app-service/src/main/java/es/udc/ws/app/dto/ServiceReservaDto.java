package es.udc.ws.app.dto;

import java.util.Calendar;

public class ServiceReservaDto {

	private Long idReserva;
	private Long idEspectaculo;
	private String email;
	private String numTarjetaCredito;
	private float precio;
	private Calendar fechaReserva;
	private int numEntradasTotal;
	private boolean check;
	public Long getIdReserva() {
		return idReserva;
	}
	
	public ServiceReservaDto(Long idReserva, Long idEspectaculo, String email, String numTarjetaCredito, float precio,
			Calendar fechaReserva, int numEntradasTotal, boolean check) {
		super();
		this.idReserva = idReserva;
		this.idEspectaculo = idEspectaculo;
		this.email = email;
		this.numTarjetaCredito = numTarjetaCredito;
		this.precio = precio;
		this.fechaReserva = fechaReserva;
		this.numEntradasTotal = numEntradasTotal;
		this.check = check;
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
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public Calendar getFechaReserva() {
		return fechaReserva;
	}
	public void setFechaReserva(Calendar fechaReserva) {
		this.fechaReserva = fechaReserva;
	}
	public int getNumEntradasTotal() {
		return numEntradasTotal;
	}
	public void setNumEntradasTotal(int numEntradasTotal) {
		this.numEntradasTotal = numEntradasTotal;
	}
	public boolean getCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	@Override
	public String toString() {
		return "ServiceReservaDto [idReserva=" + idReserva + ", idEspectaculo=" + idEspectaculo + ", email=" + email
				+ ", numTarjetaCredito=" + numTarjetaCredito + ", precio=" + precio + ", fechaReserva=" + fechaReserva
				+ ", numEntradasTotal=" + numEntradasTotal + ", check=" + check + "]";
	}
}
