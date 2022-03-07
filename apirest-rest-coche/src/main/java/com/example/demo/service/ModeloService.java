package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Modelo;

@Service
public interface ModeloService {

	public List<Modelo> findAll();
	
	public Modelo findById(Long id) ;
	
	public Modelo save(Modelo modelo);
	
	public void delete(Long id);
}
