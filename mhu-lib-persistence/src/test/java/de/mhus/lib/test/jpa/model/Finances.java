package de.mhus.lib.test.jpa.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.mhus.lib.jpa.JpaComfortable;

public class Finances extends JpaComfortable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private long store;
	private double activa;
	private double passiva;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public double getActiva() {
		return activa;
	}
	public void setActiva(double activa) {
		this.activa = activa;
	}
	
	public double getPassiva() {
		return passiva;
	}
	public void setPassiva(double passiva) {
		this.passiva = passiva;
	}
	
	public void setStore(long shop) {
		this.store = shop;
	}
	public long getStore() {
		return store;
	}
		
}
