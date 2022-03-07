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

import com.example.demo.entity.Modelo;
import com.example.demo.service.ModeloService;


@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ModeloController {

	@Autowired
	private ModeloService modeloService;
	
	
	@GetMapping("/modelos")
	public List<Modelo> index(){
		return modeloService.findAll();
	}
	
	/*@GetMapping("/modelos/{id}")
	public Modelo show(@PathVariable Long id) {
		return modeloService.findById(id);
	}*/
	@GetMapping("/modelos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Modelo modelo = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			modelo = modeloService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar consulta en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(modelo == null) {
			response.put("mensaje", "El modelo ID: ".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Modelo>(modelo,HttpStatus.OK);
	}
	
	
	/*@PostMapping("/modelos")
	@ResponseStatus(HttpStatus.CREATED)
	public Modelo create(@RequestBody Modelo modelo) {
		return modeloService.save(modelo);
	}*/
	
	@PostMapping("/modelos")
	public ResponseEntity<?> create(@RequestBody Modelo modelo){
		Modelo modeloNew = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			modeloNew = modeloService.save(modelo);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar insert en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El modelo ha sido creado con éxito!");
		response.put("modelo", modeloNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	/*@PutMapping("/modelos/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Modelo update(@RequestBody Modelo modelo, @PathVariable Long id) {
		Modelo modeloUpdate = modeloService.findById(id);
		
		modeloUpdate.setApellido(modelo.getApellido());
		modeloUpdate.setNombre(modelo.getNombre());
		modeloUpdate.setEmail(modelo.getEmail());
		
		return modeloService.save(modeloUpdate);
	}*/

	@PutMapping("/modelos/{id}")
	public ResponseEntity<?> update(@RequestBody Modelo modelo, @PathVariable Long id) {
		Modelo modeloActual= modeloService.findById(id);
		
		Modelo modeloUpdated = null;
		Map<String,Object> response = new HashMap<>();
		
		if(modeloActual == null){
			response.put("mensaje", "Error: no se pudo editar, el modelo ID: ".concat(id.toString().concat("no existe el id en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			modeloActual.setNombre(modelo.getNombre());
			
			modeloUpdated = modeloService.save(modeloActual);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al actualizar el modelo en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El modelo ha sido actualizado con éxito!");
		response.put("modelo", modeloUpdated);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	/*@DeleteMapping("modelos/{id}")
	public void delete(@PathVariable Long id) {
		modeloService.delete(id);
	}*/
	@DeleteMapping("modelos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		
		try {
			
			modeloService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el modelo en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","El modelo ha sido eliminado con éxito!");

		
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
