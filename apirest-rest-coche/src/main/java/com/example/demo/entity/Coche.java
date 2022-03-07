package com.example.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "coches")
public class Coche implements Serializable{
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="codCliente")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Long marca;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="codCliente")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Long modelo;
	
	@Column(length=50)
	private String motor;
	
	@Column(length=50)
	private int cilindrada;
	
	@Column(length=50)
	private int velocidad;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMarca() {
		return marca;
	}

	public void setMarca(Long marca) {
		this.marca = marca;
	}

	public Long getModelo() {
		return modelo;
	}

	public void setModelo(Long modelo) {
		this.modelo = modelo;
	}

	public String getMotor() {
		return motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public int getCilindrada() {
		return cilindrada;
	}

	public void setCilindrada(int cilindrada) {
		this.cilindrada = cilindrada;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
	

}
