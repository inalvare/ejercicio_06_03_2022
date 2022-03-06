package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Coche;

@Service
public interface CocheService {

	public List<Coche> findAll();
	
	public Coche findById(Long id) ;
	
	public Coche save(Coche coche);
	
	public void delete(Long id);
}
