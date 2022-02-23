package com.ibm.academia.restapi.tarjeta.servicios;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibm.academia.restapi.tarjeta.excepciones.NotFoundException;
import com.ibm.academia.restapi.tarjeta.modelo.entidades.Tarjeta;
import com.ibm.academia.restapi.tarjeta.repositorios.TarjetaRepository;

@Service
public class TarjetaDAOImpl extends GenericoDAOImpl<Tarjeta, TarjetaRepository> implements TarjetaDAO{

	@Autowired
	public TarjetaDAOImpl(@Qualifier("repositorioTarjeta")TarjetaRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	public Tarjeta actualizar(Long tarjetaId, Tarjeta tarjeta) {

		Optional<Tarjeta> oTarjeta = repository.findById(tarjetaId);
		
		if(!oTarjeta.isPresent())
			throw new NotFoundException(String.format("La tarjeta con id: %d no existe", tarjetaId));
	
		Tarjeta tarjetaActualizada = null;
		oTarjeta.get().setEdadMinima(tarjeta.getEdadMinima());
		oTarjeta.get().setEdadMaxima(tarjeta.getEdadMaxima());
		oTarjeta.get().setSalarioMinimo(tarjeta.getSalarioMinimo());
		oTarjeta.get().setSalarioMaximo(tarjeta.getSalarioMaximo());
		oTarjeta.get().setTipoPasion(tarjeta.getTipoPasion());
		oTarjeta.get().setTipoTarjeta(tarjeta.getTipoTarjeta());
		oTarjeta.get().setNombre(tarjeta.getNombre());
		
		return tarjetaActualizada;
	}

}
