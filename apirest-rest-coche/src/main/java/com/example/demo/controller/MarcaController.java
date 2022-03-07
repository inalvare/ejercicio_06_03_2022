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

import com.example.demo.entity.Marca;
import com.example.demo.service.MarcaService;


@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class MarcaController {

	@Autowired
	private MarcaService marcaService;
	
	
	@GetMapping("/marcas")
	public List<Marca> index(){
		return marcaService.findAll();
	}
	
	/*@GetMapping("/marcas/{id}")
	public Marca show(@PathVariable Long id) {
		return marcaService.findById(id);
	}*/
	@GetMapping("/marcas/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Marca marca = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			marca = marcaService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar consulta en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(marca == null) {
			response.put("mensaje", "El marca ID: ".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Marca>(marca,HttpStatus.OK);
	}
	
	
	/*@PostMapping("/marcas")
	@ResponseStatus(HttpStatus.CREATED)
	public Marca create(@RequestBody Marca marca) {
		return marcaService.save(marca);
	}*/
	
	@PostMapping("/marcas")
	public ResponseEntity<?> create(@RequestBody Marca marca){
		Marca marcaNew = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			marcaNew = marcaService.save(marca);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar insert en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El marca ha sido creado con éxito!");
		response.put("marca", marcaNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	/*@PutMapping("/marcas/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Marca update(@RequestBody Marca marca, @PathVariable Long id) {
		Marca marcaUpdate = marcaService.findById(id);
		
		marcaUpdate.setApellido(marca.getApellido());
		marcaUpdate.setNombre(marca.getNombre());
		marcaUpdate.setEmail(marca.getEmail());
		
		return marcaService.save(marcaUpdate);
	}*/

	@PutMapping("/marcas/{id}")
	public ResponseEntity<?> update(@RequestBody Marca marca, @PathVariable Long id) {
		Marca marcaActual= marcaService.findById(id);
		
		Marca marcaUpdated = null;
		Map<String,Object> response = new HashMap<>();
		
		if(marcaActual == null){
			response.put("mensaje", "Error: no se pudo editar, el marca ID: ".concat(id.toString().concat("no existe el id en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			marcaActual.setNombre(marca.getNombre());
			
			marcaUpdated = marcaService.save(marcaActual);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al actualizar el marca en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El marca ha sido actualizado con éxito!");
		response.put("marca", marcaUpdated);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	/*@DeleteMapping("marcas/{id}")
	public void delete(@PathVariable Long id) {
		marcaService.delete(id);
	}*/
	@DeleteMapping("marcas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		
		try {
			
			marcaService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el marca en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","El marca ha sido eliminado con éxito!");

		
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
