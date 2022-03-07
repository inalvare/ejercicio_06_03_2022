package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Marca;

@Service
public interface MarcaService {

	public List<Marca> findAll();
	
	public Marca findById(Long id) ;
	
	public Marca save(Marca marca);
	
	public void delete(Long id);
}
