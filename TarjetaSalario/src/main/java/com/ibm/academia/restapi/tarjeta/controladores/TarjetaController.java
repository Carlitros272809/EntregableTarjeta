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
import com.ibm.academia.restapi.tarjeta.modelo.entidades.Tarjeta;
import com.ibm.academia.restapi.tarjeta.servicios.TarjetaDAO;

@RestController
@RequestMapping("/restapi")
public class TarjetaController {
	
	private final static Logger logger = LoggerFactory.getLogger(TarjetaController.class);

	@Autowired
	@Qualifier("tarjetaDAOImpl")
	private TarjetaDAO tarjetaDao;
	
	/*
	 * Endpoint para consultar todas las tarjetas
	 * @return Retorna una lista de tarjetas
	 * @NotFoundException en caso de que falle buscando las tarjetas
	 * @author CDRC - 22-02-2022
	 * */
	@GetMapping("/tarjetas/lista")
	public ResponseEntity<?> obtenerTodasTarjetas() {
		List<Tarjeta> tarjetas = (List<Tarjeta>) tarjetaDao.buscarTodos();
		
		if(tarjetas.isEmpty())
			throw new NotFoundException("No existen tarjetas");
		
		return new ResponseEntity<List<Tarjeta>>(tarjetas, HttpStatus.OK);
	}
	
	/*
	 * Endpoint para consultar una tarjeta por id
	 * @param tarjetaId Parametro de busqueda de la tarjeta
	 * @return Retorna un objeto de tipo Tarjeta
	 * @NotFoundException En caso de que falle buscando la tarjeta
	 * @author CDRC - 22-02-2022
	 * */
	@GetMapping("/tarjeta/tarjetaId/{tarjetaId}")
	public ResponseEntity<?> obtenerTarjetaPorId(@PathVariable Long tarjetaId) {
	
		Optional<Tarjeta> oTarjeta = tarjetaDao.buscarPorId(tarjetaId);
		
		if(!oTarjeta.isPresent())
			throw new NotFoundException(String.format("La tarjeta con id: %d no existe", tarjetaId));
	
		return new ResponseEntity<Tarjeta>(oTarjeta.get(), HttpStatus.OK);
	}
	
	/*
	 *Endpoint para agregar una tarjeta
	 * @Body Json de tarjeta
	 * @return tarjeta registrada
	 * @author CDRC - 22-02-2022
	 */
	@PostMapping("/tarjeta")
	public ResponseEntity<?> guardar(@Valid @RequestBody Tarjeta tarjeta, BindingResult result) {
		
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
		
		Tarjeta tarjetaGuardada = tarjetaDao.guardar(tarjeta);
		return new ResponseEntity<Tarjeta>(tarjetaGuardada, HttpStatus.CREATED);
		
	}

	/*
	 * Endpoint para eliminar una tarjeta por id
	 * @param tarjetaId Parametro de busqueda de la tarjeta
	 * @return Retorna un mensaje de eliminado correctamente
	 * @NotFoundException En caso de que falle buscando la tarjeta
	 * @author CDRC - 22-02-2022
	 * */
	@DeleteMapping("/tarjeta/eliminar/tarjetaId/{tarjetaId}")
	public ResponseEntity<?> eliminarTarjeta(@PathVariable Long tarjetaId) {
		
		Optional<Tarjeta> oTarjeta = tarjetaDao.buscarPorId(tarjetaId);
		
		if(!oTarjeta.isPresent())
			throw new NotFoundException(String.format("La tarjeta con id: %d no existe", tarjetaId));
		
		tarjetaDao.eliminarPorId(tarjetaId);
		return new ResponseEntity<String>("La tarjeta con id: " + tarjetaId + "se elimino satisfactoriamente",  HttpStatus.NO_CONTENT);
		
 	}
	
	/*
	 * Endpoint para actualizar una tarjeta por id
	 * @param tarjetaId Parametro de busqueda de la tarjeta
	 * @Body Json de tarjeta
	 * @return tarjeta actualizada correctamente
	 * @NotFoundException En caso de que falle buscando la tarjeta
	 * @author CDRC - 22-02-2022
	 * */
	@PutMapping("/tarjeta/actualizar/tarjetaId/{tarjetaId}")
	public ResponseEntity<?> actualizar(@PathVariable Long tarjetaId, @Valid @RequestBody Tarjeta tarjeta, BindingResult result) {
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
		
		Tarjeta tarjetaActualizada = null;
		
		try
		{
			tarjetaActualizada = tarjetaDao.actualizar(tarjetaId, tarjeta);
		}
		catch (Exception e) 
		{
			logger.info(e.getMessage());
			throw e;
		}
		
		return new ResponseEntity<Tarjeta>(tarjetaActualizada, HttpStatus.OK);
	}
}
