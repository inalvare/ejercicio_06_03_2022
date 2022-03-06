package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CocheDao;
import com.example.demo.entity.Coche;

@Service
public class CocheServiceImpl implements CocheService{

	@Autowired
	private CocheDao cocheDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Coche> findAll() {
		
		return (List<Coche>) cocheDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Coche findById(Long id) {
		
		return cocheDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Coche save(Coche coche) {

		return cocheDao.save(coche);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		cocheDao.deleteById(id);
		
	}
}
