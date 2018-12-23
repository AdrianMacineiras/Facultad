package es.udc.ws.app.dto;

import java.util.Calendar;

public class ServiceEspectaculoSalidaDto {


	private Long idEspectaculo;
	private String nombre;
	private String descripcion;
	private Calendar fechaEspectaculo;
	private int duracion;
	private Calendar fechaLimReserva;
	private float precioReal;
	private float precioDescontado;
	private int numEntradasRest;

	
	public ServiceEspectaculoSalidaDto(Long idEspectaculo, String nombre, String descripcion, Calendar fechaEspectaculo,
			int duracion, Calendar fechaLimReserva, float precioReal, float precioDescontado, int numEntradasRest) {
		super();
		this.idEspectaculo = idEspectaculo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaEspectaculo = fechaEspectaculo;
		this.duracion = duracion;
		this.fechaLimReserva = fechaLimReserva;
		this.precioReal = precioReal;
		this.precioDescontado = precioDescontado;
		this.numEntradasRest = numEntradasRest;
	}
	
	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Calendar getFechaEspectaculo() {
		return fechaEspectaculo;
	}

	public void setFechaEspectaculo(Calendar fechaEspectaculo) {
		this.fechaEspectaculo = fechaEspectaculo;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public Calendar getFechaLimReserva() {
		return fechaLimReserva;
	}

	public void setFechaLimReserva(Calendar fechaLimReserva) {
		this.fechaLimReserva = fechaLimReserva;
	}

	public float getPrecioReal() {
		return precioReal;
	}

	public void setPrecioReal(float precioReal) {
		this.precioReal = precioReal;
	}

	public float getPrecioDescontado() {
		return precioDescontado;
	}

	public void setPrecioDescontado(float precioDescontado) {
		this.precioDescontado = precioDescontado;
	}

	public int getNumEntradasRest() {
		return numEntradasRest;
	}

	public void setNumEntradasRest(int numEntradasRest) {
		this.numEntradasRest = numEntradasRest;
	}

	@Override
	public String toString() {
		return "ServiceEspectaculoSalidaDto [idEspectaculo=" + idEspectaculo + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", fechaEspectaculo=" + fechaEspectaculo + ", duracion=" + duracion
				+ ", fechaLimReserva=" + fechaLimReserva + ", precioReal=" + precioReal + ", precioDescontado="
				+ precioDescontado + ", numEntradasRest=" + numEntradasRest + "]";
	}
}
