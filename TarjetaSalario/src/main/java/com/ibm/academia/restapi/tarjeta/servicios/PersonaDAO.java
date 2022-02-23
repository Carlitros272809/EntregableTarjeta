package com.ibm.academia.restapi.tarjeta.servicios;

import com.ibm.academia.restapi.tarjeta.modelo.entidades.Persona;

public interface PersonaDAO extends GenericoDAO<Persona>{
	public Persona actualizar(Long personaId, Persona persona);
	public Persona asignarTarjetaPersona(Long personaId);
}
