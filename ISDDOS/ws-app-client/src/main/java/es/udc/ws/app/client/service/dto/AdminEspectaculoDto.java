package es.udc.ws.app.client.service.dto;

import java.util.Calendar;

public class AdminEspectaculoDto {
	private Long idEspectaculo;
	private String nombre;
	private String descripcion;
	private Calendar fechaEspectaculo;
	private int duracion;
	private Calendar fechaLimReserva;
	private int maxEntradas;
	private float precioReal;
	private float precioDescontado;
	private float comisionVenta;
	private int numEntradasRest;
	
	
	public AdminEspectaculoDto(Long idEspectaculo, String nombre, String descripcion, Calendar fechaEspectaculo, int duracion,
			Calendar fechaLimReserva, int maxEntradas, float precioReal, float precioDescontado, float comisionVenta,
			int numEntradasRest) {
		super();
		this.idEspectaculo = idEspectaculo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaEspectaculo = fechaEspectaculo;
		this.duracion = duracion;
		this.fechaLimReserva = fechaLimReserva;
		this.maxEntradas = maxEntradas;
		this.precioReal = precioReal;
		this.precioDescontado = precioDescontado;
		this.comisionVenta = comisionVenta;
		this.numEntradasRest = numEntradasRest;
		if (fechaEspectaculo != null) {
			this.fechaEspectaculo.set(Calendar.MILLISECOND, 0);
		}
		if (fechaLimReserva != null) {
			this.fechaLimReserva.set(Calendar.MILLISECOND, 0);
		}
	}

	
	public AdminEspectaculoDto(Long idEspectaculo, String nombre, String descripcion, Calendar fechaEspectaculo, int duracion,
			Calendar fechaLimReserva, int maxEntradas, float precioReal, float precioDescontado, float comisionVenta) {
		super();
		this.idEspectaculo = idEspectaculo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaEspectaculo = fechaEspectaculo;
		this.duracion = duracion;
		this.fechaLimReserva = fechaLimReserva;
		this.maxEntradas = maxEntradas;
		this.precioReal = precioReal;
		this.precioDescontado = precioDescontado;
		this.comisionVenta = comisionVenta;
		this.numEntradasRest = maxEntradas;
		if (fechaEspectaculo != null) {
			this.fechaEspectaculo.set(Calendar.MILLISECOND, 0);
		}
		if (fechaLimReserva != null) {
			this.fechaLimReserva.set(Calendar.MILLISECOND, 0);
		}
	}
	
	public AdminEspectaculoDto(String nombre, String descripcion, Calendar fechaEspectaculo, int duracion,
			Calendar fechaLimReserva, int maxEntradas, float precioReal, float precioDescontado, float comisionVenta) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaEspectaculo = fechaEspectaculo;
		this.duracion = duracion;
		this.fechaLimReserva = fechaLimReserva;
		this.maxEntradas = maxEntradas;
		this.precioReal = precioReal;
		this.precioDescontado = precioDescontado;
		this.comisionVenta = comisionVenta;
		this.numEntradasRest = maxEntradas;
		if (fechaEspectaculo != null) {
			this.fechaEspectaculo.set(Calendar.MILLISECOND, 0);
		}
		if (fechaLimReserva != null) {
			this.fechaLimReserva.set(Calendar.MILLISECOND, 0);
		}
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
		if (fechaEspectaculo != null) {
			this.fechaEspectaculo.set(Calendar.MILLISECOND, 0);
		}
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
		if (fechaLimReserva != null) {
			this.fechaLimReserva.set(Calendar.MILLISECOND, 0);
		}
		
	}
	public int getMaxEntradas() {
		return maxEntradas;
	}
	public void setMaxEntradas(int maxEntradas) {
		this.maxEntradas = maxEntradas;
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
	public float getComisionVenta() {
		return comisionVenta;
	}
	public void setComisionVenta(float comisionVenta) {
		this.comisionVenta = comisionVenta;
	}
	public int getNumEntradasRest() {
		return numEntradasRest;
	}
	public void setNumEntradasRest(int numEntradasRest) {
		this.numEntradasRest = numEntradasRest;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(comisionVenta);
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + duracion;
		result = prime * result + ((fechaEspectaculo == null) ? 0 : fechaEspectaculo.hashCode());
		result = prime * result + ((fechaLimReserva == null) ? 0 : fechaLimReserva.hashCode());
		result = prime * result + ((idEspectaculo == null) ? 0 : idEspectaculo.hashCode());
		result = prime * result + maxEntradas;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + numEntradasRest;
		result = prime * result + Float.floatToIntBits(precioDescontado);
		result = prime * result + Float.floatToIntBits(precioReal);
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
		AdminEspectaculoDto other = (AdminEspectaculoDto) obj;
		if (Float.floatToIntBits(comisionVenta) != Float.floatToIntBits(other.comisionVenta))
			return false;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (duracion != other.duracion)
			return false;
		if (fechaEspectaculo == null) {
			if (other.fechaEspectaculo != null)
				return false;
		} else if (!fechaEspectaculo.equals(other.fechaEspectaculo))
			return false;
		if (fechaLimReserva == null) {
			if (other.fechaLimReserva != null)
				return false;
		} else if (!fechaLimReserva.equals(other.fechaLimReserva))
			return false;
		if (idEspectaculo == null) {
			if (other.idEspectaculo != null)
				return false;
		} else if (!idEspectaculo.equals(other.idEspectaculo))
			return false;
		if (maxEntradas != other.maxEntradas)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (numEntradasRest != other.numEntradasRest)
			return false;
		if (Float.floatToIntBits(precioDescontado) != Float.floatToIntBits(other.precioDescontado))
			return false;
		if (Float.floatToIntBits(precioReal) != Float.floatToIntBits(other.precioReal))
			return false;
		return true;
	}


}
