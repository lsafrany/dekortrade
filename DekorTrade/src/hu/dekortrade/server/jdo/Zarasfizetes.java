package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Zarasfizetes {

	@Persistent
	private String zaras;

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
	private Float fizet;

	@Persistent
	private Float fizeteur;

	@Persistent
	private Float fizetusd;

	@Persistent
	private Date datum;

	public Zarasfizetes(String zaras, String cedula, String vevo, String tipus, String megjegyzes,
			String penztaros, Float fizet, Float fizeteur, Float fizetusd,
			Date datum) {
		this.zaras = zaras;
		this.cedula = cedula;
		this.vevo = vevo;
		this.tipus = tipus;
		this.megjegyzes = megjegyzes;
		this.penztaros = penztaros;
		this.fizet = fizet;
		this.fizeteur = fizeteur;
		this.fizetusd = fizetusd;
		this.datum = datum;
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

}
