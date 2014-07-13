package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Kosarcikk {

	@Persistent
	private String elado;

	@Persistent
	private String vevo;

	@Persistent
	private String tipus;

	@Persistent
	private String cikkszam;

	@Persistent
	private String szinkod;

	@Persistent
	private Float ar;

	@Persistent
	private Float areur;

	@Persistent
	private Float arusd;

	@Persistent
	private Integer exportkarton;

	@Persistent
	private Integer kiskarton;

	@Persistent
	private Integer darab;

	@Persistent
	private Float fizet;

	@Persistent
	private Float fizeteur;

	@Persistent
	private Float fizetusd;

	@Persistent
	private String cedula;

	public Kosarcikk(String elado, String vevo, String tipus, String cikkszam,
			String szinkod, Float ar, Float areur, Float arusd,
			Integer exportkarton, Integer kiskarton, Integer darab,
			Float fizet, Float fizeteur, Float fizetusd, String cedula) {

		this.elado = elado;
		this.vevo = vevo;
		this.tipus = tipus;
		this.cikkszam = cikkszam;
		this.szinkod = szinkod;
		this.ar = ar;
		this.areur = areur;
		this.arusd = arusd;
		this.exportkarton = exportkarton;
		this.kiskarton = kiskarton;
		this.darab = darab;
		this.fizet = fizet;
		this.fizeteur = fizeteur;
		this.fizetusd = fizetusd;
		this.cedula = cedula;
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

	public String getCikkszam() {
		return cikkszam;
	}

	public void setCikkszam(String cikkszam) {
		this.cikkszam = cikkszam;
	}

	public String getSzinkod() {
		return szinkod;
	}

	public void setSzinkod(String szinkod) {
		this.szinkod = szinkod;
	}

	public Float getAr() {
		return ar;
	}

	public void setAr(Float ar) {
		this.ar = ar;
	}

	public Float getAreur() {
		return areur;
	}

	public void setAreur(Float areur) {
		this.areur = areur;
	}

	public Float getArusd() {
		return arusd;
	}

	public void setArusd(Float arusd) {
		this.arusd = arusd;
	}

	public Integer getExportkarton() {
		return exportkarton;
	}

	public void setExportkarton(Integer exportkarton) {
		this.exportkarton = exportkarton;
	}

	public Integer getKiskarton() {
		return kiskarton;
	}

	public void setKiskarton(Integer kiskarton) {
		this.kiskarton = kiskarton;
	}

	public Integer getDarab() {
		return darab;
	}

	public void setDarab(Integer darab) {
		this.darab = darab;
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

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

}
