package com.ibm.academia.restapi.tarjeta.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.academia.restapi.tarjeta.excepciones.NotFoundException;
import com.ibm.academia.restapi.tarjeta.modelo.entidades.Persona;
import com.ibm.academia.restapi.tarjeta.modelo.entidades.Tarjeta;
import com.ibm.academia.restapi.tarjeta.servicios.PersonaDAO;
import com.ibm.academia.restapi.tarjeta.servicios.TarjetaDAO;

@RestController
@RequestMapping("/restapi")
public class PersonaController {
	
	private final static Logger logger = LoggerFactory.getLogger(PersonaController.class);

	@Autowired
	@Qualifier("personaDAOImpl")
	private PersonaDAO personaDao;
	
	@Autowired 
	@Qualifier("tarjetaDAOImpl")
	private TarjetaDAO tarjetaDao;
	
	/*
	 * Endpoint para consultar todas las personas
	 * @return Retorna una lista de personas
	 * @NotFoundException en caso de que falle buscando las personas
	 * @author CDRC - 22-02-2022
	 * */
	@GetMapping("/personas/lista")
	public ResponseEntity<?> obtenerTodasPersonas() {
		List<Persona> personas = (List<Persona>) personaDao.buscarTodos();
		
		if(personas.isEmpty())
			throw new NotFoundException("No existen personas");
		
		return new ResponseEntity<List<Persona>>(personas, HttpStatus.OK);
	}
	
	/*
	 * Endpoint para consultar una persona por id
	 * @param personaID Parametro de busqueda de la persona
	 * @return Retorna un objeto de tipo persona
	 * @NotFoundException En caso de que falle buscando la persona
	 * @author CDRC - 22-02-2022
	 * */
	@GetMapping("/persona/personaId/{personaId}")
	public ResponseEntity<?> obtenerPersonaPorId(@PathVariable Long personaId) {
		Optional <Persona> oPersona = personaDao.buscarPorId(personaId);
		
		if(!oPersona.isPresent())
			throw new NotFoundException(String.format("La persona con id: %d no existe", personaId));
	
		return new ResponseEntity<Persona>(oPersona.get(), HttpStatus.OK);
	
	}
	
	/*
	 *Endpoint para agregar una persona
	 * @Body Json de persona
	 * @return persona registrada
	 * @author CDRC - 22-02-2022
	 */
	@PostMapping("/persona")
	public ResponseEntity<?> guardar(@Valid @RequestBody Persona persona, BindingResult result) {
		
		Map<String, Object> validaciones = new HashMap<String, Object>();
		if(result.hasErrors())
		{
			List<String> listaErrores = result.getFieldErrors()
					.stream()
					.map(errores -> "Campo: '" + errores.getField() + "' " + errores.getDefaultMessage())
					.collect(Collectors.toList());
			validaciones.put("Lista Errores", listaErrores);
			return new ResponseEntity<Map<String, Object>>(validaciones, HttpStatus.BAD_REQUEST);
		}
		
		Persona personaGuardada = personaDao.guardar(persona);
		return new ResponseEntity<Persona>(personaGuardada, HttpStatus.CREATED);
		
	}
	
	/*
	 * Endpoint para eliminar una persona por id
	 * @param personaId Parametro de busqueda de la persona
	 * @return Retorna un mensaje de eliminado correctamente
	 * @NotFoundException En caso de que falle buscando la persona
	 * @author CDRC - 22-02-2022
	 * */
	@DeleteMapping("/persona/eliminar/personaId/{personaId}")
	public ResponseEntity<?> eliminarPersona(@PathVariable Long personaId) {
		
		Optional<Persona> oPersona = personaDao.buscarPorId(personaId);
		
		if(!oPersona.isPresent())
			throw new NotFoundException(String.format("La persona con id: %d no existe", personaId));
		
		personaDao.eliminarPorId(personaId);
		return new ResponseEntity<String>("La persona con id: " + personaId + "se elimino satisfactoriamente",  HttpStatus.NO_CONTENT);
		
 	}
	
	/*
	 * Endpoint para actualizar una persona por id
	 * @param personaId Parametro de busqueda de la persona
	 * @Body Json de persona
	 * @return persona actualizada correctamente
	 * @NotFoundException En caso de que falle buscando la persona
	 * @author CDRC - 22-02-2022
	 * */
	@PutMapping("/persona/actualizar/personaId/{personaId}")
	public ResponseEntity<?> actualizar(@PathVariable Long personaId, @Valid @RequestBody Persona persona, BindingResult result)
	{
		Map<String, Object> validaciones = new HashMap<String, Object>();
		if(result.hasErrors())
		{
			List<String> listaErrores = result.getFieldErrors()
					.stream()
					.map(errores -> "Campo: '" + errores.getField() + "' " + errores.getDefaultMessage())
					.collect(Collectors.toList());
			validaciones.put("Lista Errores", listaErrores);
			return new ResponseEntity<Map<String, Object>>(validaciones, HttpStatus.BAD_REQUEST);
		}
		
		Persona personaActualizada = null;
		
		try
		{
			personaActualizada = personaDao.actualizar(personaId, persona);
		}
		catch (Exception e) 
		{
			logger.info(e.getMessage());
			throw e;
		}
		
		return new ResponseEntity<Persona>(personaActualizada, HttpStatus.OK);
	}
	
	/*
	 * Endpoint para asignar tarjetas a una persona por el id de una persona
	 * @param personaId Parametro de busqueda de la persona
	 * @return Persona actualizada con tarjetas asignadas
	 * @NotFoundException En caso de que falle buscando la personaa
	 * @NotFoundException En caso de que falle buscando las tarjetas existentes
	 * @author CDRC - 22-02-2022
	*/
	@PutMapping("/persona/solicitar-tarjeta/{personaId}")
	public ResponseEntity<?> solicitarTarjeta(@PathVariable Long personaId){
		
		Optional<Persona> oPersona = personaDao.buscarPorId(personaId);
		
		if(!oPersona.isPresent())
			throw new NotFoundException(String.format("La persona con ID %d no existe", personaId));
		
		List<Tarjeta> tarjetas = (List<Tarjeta>) tarjetaDao.buscarTodos();
		
		if(tarjetas.isEmpty())
			throw new NotFoundException(String.format("No hay tarjetas"));
		
		Persona persona = personaDao.asignarTarjetaPersona(personaId);
		return new ResponseEntity<Persona>(persona, HttpStatus.OK);
		
	}
	
}
