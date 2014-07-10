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
	private String penztaros;

	@Persistent
	private Float fizet;
	
	@Persistent
	private Float fizeteur;	

	@Persistent
	private Float fizetusd;	

	@Persistent
	private Date datum;

	@Persistent
	private boolean szamolt;

	public Fizetes(String cedula, String vevo, String tipus, String penztaros, Float fizet, Float fizeteur, Float fizetusd, Date datum, boolean szamolt) {
		this.cedula = cedula;
		this.vevo = vevo;
		this.tipus = tipus;
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

	public String getPenztaros() {
		return penztaros;
	}

	public void setPenztaros(String penztaros) {
		this.penztaros = penztaros;
	}

	public Float getFizet() {
		return fizet;
	}

	public void setFizet(Float fizet) {
		this.fizet = fizet;
	}

	public Float getFizeteur() {
		return fizeteur;
	}

	public void setFizeteur(Float fizeteur) {
		this.fizeteur = fizeteur;
	}

	public Float getFizetusd() {
		return fizetusd;
	}

	public void setFizetusd(Float fizetusd) {
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
