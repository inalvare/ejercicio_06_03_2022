package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Coche;

public interface CocheDao extends JpaRepository<Coche, Long>{

}
