package com.ibm.academia.restapi.tarjeta.excepciones;

public class BadRequestException extends RuntimeException
{
	public BadRequestException(String mensaje)
	{
		super(mensaje);
	}
	
	private static final long serialVersionUID = 6668916681243010155L;
}