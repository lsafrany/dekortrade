package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Fizetes {

	@Persistent
	private String cedula;

	@Persistent
	private String vevo;

	@Persistent
	private String tipus;

	@Persistent
	private String megjegyzes;

	@Persistent
	private String penztaros;

	@Persistent
	private Double fizet;

	@Persistent
	private Double fizeteur;

	@Persistent
	private Double fizetusd;

	@Persistent
	private Date datum;

	@Persistent
	private boolean szamolt;

	public Fizetes(String cedula, String vevo, String tipus, String megjegyzes,String penztaros,
			Double fizet, Double fizeteur, Double fizetusd, Date datum,
			boolean szamolt) {
		this.cedula = cedula;
		this.vevo = vevo;
		this.tipus = tipus;
		this.megjegyzes = megjegyzes;
		this.penztaros = penztaros;
		this.fizet = fizet;
		this.fizeteur = fizeteur;
		this.fizetusd = fizetusd;
		this.datum = datum;
		this.szamolt = szamolt;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
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
	
	public String getMegjegyzes() {
		return megjegyzes;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
	}

	public String getPenztaros() {
		return penztaros;
	}

	public void setPenztaros(String penztaros) {
		this.penztaros = penztaros;
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

	public Double getFizetusd() {
		return fizetusd;
	}

	public void setFizetusd(Double fizetusd) {
		this.fizetusd = fizetusd;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public boolean isSzamolt() {
		return szamolt;
	}

	public void setSzamolt(boolean szamolt) {
		this.szamolt = szamolt;
	}

}
