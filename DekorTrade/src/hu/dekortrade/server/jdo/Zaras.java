package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Zaras {

	@Persistent
	private String zaras;

	@Persistent
	private String penztaros;

	@Persistent
	private Double kivethuf;

	@Persistent
	private Double kiveteur;

	@Persistent
	private Double kivetusd;

	@Persistent
	private Double egyenleghuf;

	@Persistent
	private Double egyenlegeur;

	@Persistent
	private Double egyenlegusd;

	@Persistent
	private Date datum;

	public Zaras(String zaras, String penztaros, Double kivethuf, Double kiveteur, Double kivetusd, Double egyenleghuf, Double egyenlegeur, Double egyenlegusd, Date datum) {
		this.zaras = zaras;
		this.penztaros = penztaros;
		this.kivetusd = kivetusd ;
		this.kiveteur = kiveteur;
		this.kivethuf = kivethuf;
		this.egyenlegusd = egyenlegusd;
		this.egyenlegeur = egyenlegeur;
		this.egyenleghuf = egyenleghuf;
		this.datum = datum;
	}

	public String getZaras() {
		return zaras;
	}

	public void setZaras(String zaras) {
		this.zaras = zaras;
	}

	public String getPenztaros() {
		return penztaros;
	}

	public void setPenztaros(String penztaros) {
		this.penztaros = penztaros;
	}
	
	public Double getKivetusd() {
		return kivetusd;
	}

	public void setKivetusd(Double kivetusd) {
		this.kivetusd = kivetusd;
	}

	public Double getKiveteur() {
		return kiveteur;
	}

	public void setKiveteur(Double kiveteur) {
		this.kiveteur = kiveteur;
	}

	public Double getKivethuf() {
		return kivethuf;
	}

	public void setKivethuf(Double kivethuf) {
		this.kivethuf = kivethuf;
	}

	public Double getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Double egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Double getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Double egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Double getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Double egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
