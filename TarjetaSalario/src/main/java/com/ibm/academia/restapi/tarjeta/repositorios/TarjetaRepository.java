package com.ibm.academia.restapi.tarjeta.repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.academia.restapi.tarjeta.modelo.entidades.Tarjeta;

@Repository("repositorioTarjeta")
public interface TarjetaRepository extends CrudRepository<Tarjeta, Long> {

}
