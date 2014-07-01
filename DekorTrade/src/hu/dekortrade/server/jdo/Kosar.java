package hu.dekortrade.server.jdo;

import javax.jdo.annotations.Cacheable;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
@Cacheable("false")
public class Kosar {

	@Persistent
	private String elado;

	@Persistent
	private String vevo;

	@Persistent
	private String tipus;	
	
	@Persistent
	private Double fizet;
	
	@Persistent
	private Double fizeteur;	
	
	@Persistent
	private String cedula;	

	public Kosar(String elado, String vevo, String tipus) {

		this.elado = elado;
		this.vevo = vevo;
		this.tipus = tipus;
	}

	public String getElado() {
		return elado;
	}

	public void setElado(String elado) {
		this.elado = elado;
	}

	public String getVevo() {
		return vevo;
	}

	public void setVevo(String vevo) {
		this.vevo = vevo;
	}

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	public Double getFizet() {
		return fizet;
	}

	public void setFizet(Double fizet) {
		this.fizet = fizet;
	}

	public Double getFizeteur() {
		return fizeteur;
	}

	public void setFizeteur(Double fizeteur) {
		this.fizeteur = fizeteur;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	
}
