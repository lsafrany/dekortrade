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
	private Float kivethuf;

	@Persistent
	private Float kiveteur;

	@Persistent
	private Float kivetusd;

	@Persistent
	private Float egyenleghuf;

	@Persistent
	private Float egyenlegeur;

	@Persistent
	private Float egyenlegusd;

	@Persistent
	private Date datum;

	public Zaras(String zaras, String penztaros, Float kivethuf, Float kiveteur, Float kivetusd, Float egyenleghuf, Float egyenlegeur, Float egyenlegusd, Date datum) {
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
	
	public Float getKivetusd() {
		return kivetusd;
	}

	public void setKivetusd(Float kivetusd) {
		this.kivetusd = kivetusd;
	}

	public Float getKiveteur() {
		return kiveteur;
	}

	public void setKiveteur(Float kiveteur) {
		this.kiveteur = kiveteur;
	}

	public Float getKivethuf() {
		return kivethuf;
	}

	public void setKivethuf(Float kivethuf) {
		this.kivethuf = kivethuf;
	}

	public Float getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Float egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Float getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Float egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Float getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Float egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
