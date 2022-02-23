package com.ibm.academia.restapi.tarjeta.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.academia.restapi.tarjeta.modelo.entidades.Persona;
import com.ibm.academia.restapi.tarjeta.modelo.entidades.Tarjeta;
import com.ibm.academia.restapi.tarjeta.repositorios.PersonaRepository;
import com.ibm.academia.restapi.tarjeta.excepciones.NotFoundException;

@Service
public class PersonaDAOImpl extends GenericoDAOImpl<Persona,PersonaRepository> implements PersonaDAO {
	
	@Autowired 
	private TarjetaDAO tarjetaDao;
	
	@Autowired
	public PersonaDAOImpl(@Qualifier("repositorioPersona")PersonaRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	public Persona asignarTarjetaPersona(Long personaId) {
		
		Optional<Persona> oPersona = repository.findById(personaId);
		
		if(!oPersona.isPresent())
			throw new NotFoundException(String.format("La persona con ID %d no existe", personaId));
		
		List<Tarjeta> tarjetas = (List<Tarjeta>) tarjetaDao.buscarTodos();
		
		if(tarjetas.isEmpty())
			throw new NotFoundException(String.format("No hay tarjetas"));
		
		Set<Tarjeta> tarjetasAsignadas = new HashSet<Tarjeta>();

		tarjetas.forEach(tarjeta -> {
			
			if(oPersona.get().getTipoPasion() == tarjeta.getTipoPasion()) {
				if(oPersona.get().getEdad() >= tarjeta.getEdadMinima() && oPersona.get().getEdad() <= tarjeta.getEdadMaxima()) {
					if(tarjeta.getSalarioMaximo() == null) {
						if(oPersona.get().getSalario().compareTo(tarjeta.getSalarioMinimo()) == 1) {
							tarjetasAsignadas.add(tarjeta);
						}
					} else if (tarjeta.getSalarioMaximo() != null) {
						if(oPersona.get().getSalario().compareTo(tarjeta.getSalarioMinimo()) == 1 && oPersona.get().getSalario().compareTo(tarjeta.getSalarioMaximo()) != 0) {
							tarjetasAsignadas.add(tarjeta);
						}
					}
				}
			}
			
		});
		
		oPersona.get().setTarjetas(tarjetasAsignadas);
		
		return repository.save(oPersona.get());
	}

	@Override
	@Transactional
	public Persona actualizar(Long personaId, Persona persona) {
		Optional <Persona> oPersona = repository.findById(personaId);
		
		if(!oPersona.isPresent())
			throw new NotFoundException(String.format("La persona con id: %d no existe", personaId));
	
		Persona personaActualizada = null;
		oPersona.get().setNombre(persona.getNombre());
		oPersona.get().setApellidoPaterno(persona.getApellidoPaterno());
		oPersona.get().setApellidoMaterno(persona.getApellidoMaterno());
		oPersona.get().setEdad(persona.getEdad());
		oPersona.get().setSalario(persona.getSalario());
		oPersona.get().setTipoPasion(persona.getTipoPasion());
		
		return personaActualizada;
	}
	
}
