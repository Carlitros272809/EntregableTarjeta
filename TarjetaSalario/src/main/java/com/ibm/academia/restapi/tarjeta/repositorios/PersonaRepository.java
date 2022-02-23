package com.ibm.academia.restapi.tarjeta.repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.academia.restapi.tarjeta.modelo.entidades.Persona;

@Repository("repositorioPersona")
public interface PersonaRepository extends CrudRepository<Persona, Long> {

}
