package com.ibm.academia.restapi.tarjeta.servicios;

import com.ibm.academia.restapi.tarjeta.modelo.entidades.Tarjeta;

public interface TarjetaDAO extends GenericoDAO<Tarjeta>{
	public Tarjeta actualizar(Long tarjetaId, Tarjeta tarjeta);
}
