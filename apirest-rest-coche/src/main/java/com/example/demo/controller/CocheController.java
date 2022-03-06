package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Coche;
import com.example.demo.service.CocheService;


@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class CocheController {

	@Autowired
	private CocheService cocheService;
	
	
	@GetMapping("/coches")
	public List<Coche> index(){
		return cocheService.findAll();
	}
	
	/*@GetMapping("/coches/{id}")
	public Coche show(@PathVariable Long id) {
		return cocheService.findById(id);
	}*/
	@GetMapping("/coches/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Coche coche = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			coche = cocheService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar consulta en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(coche == null) {
			response.put("mensaje", "El coche ID: ".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Coche>(coche,HttpStatus.OK);
	}
	
	
	/*@PostMapping("/coches")
	@ResponseStatus(HttpStatus.CREATED)
	public Coche create(@RequestBody Coche coche) {
		return cocheService.save(coche);
	}*/
	
	@PostMapping("/coches")
	public ResponseEntity<?> create(@RequestBody Coche coche){
		Coche cocheNew = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			cocheNew = cocheService.save(coche);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar insert en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El coche ha sido creado con éxito!");
		response.put("coche", cocheNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	/*@PutMapping("/coches/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Coche update(@RequestBody Coche coche, @PathVariable Long id) {
		Coche cocheUpdate = cocheService.findById(id);
		
		cocheUpdate.setApellido(coche.getApellido());
		cocheUpdate.setNombre(coche.getNombre());
		cocheUpdate.setEmail(coche.getEmail());
		
		return cocheService.save(cocheUpdate);
	}*/

	@PutMapping("/coches/{id}")
	public ResponseEntity<?> update(@RequestBody Coche coche, @PathVariable Long id) {
		Coche cocheActual= cocheService.findById(id);
		
		Coche cocheUpdated = null;
		Map<String,Object> response = new HashMap<>();
		
		if(cocheActual == null){
			response.put("mensaje", "Error: no se pudo editar, el coche ID: ".concat(id.toString().concat("no existe el id en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			cocheActual.setMarca(coche.getMarca());
			cocheActual.setModelo(coche.getModelo());
			cocheActual.setMotor(coche.getMotor());
			cocheActual.setCilindrada(coche.getCilindrada());
			cocheActual.setVelocidad(coche.getVelocidad());
			
			cocheUpdated = cocheService.save(cocheActual);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al actualizar el coche en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El coche ha sido actualizado con éxito!");
		response.put("coche", cocheUpdated);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	/*@DeleteMapping("coches/{id}")
	public void delete(@PathVariable Long id) {
		cocheService.delete(id);
	}*/
	@DeleteMapping("coches/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		
		try {
			
			cocheService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el coche en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","El coche ha sido eliminado con éxito!");

		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		
		Resource recurso = null;
		
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if(!recurso.exists() && !recurso.isReadable()){
			throw new RuntimeException("Error no se puede cargar la imagen "+nombreFoto);
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+recurso.getFilename()+"\"");
		
		
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}
	
}
