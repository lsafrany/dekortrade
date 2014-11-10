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
	private Double ar;

	@Persistent
	private Double areur;

	@Persistent
	private Double arusd;

	@Persistent
	private Integer exportkarton;

	@Persistent
	private Integer kiskarton;

	@Persistent
	private Integer darab;

	@Persistent
	private Double fizet;

	@Persistent
	private Double fizeteur;

	@Persistent
	private Double fizetusd;

	@Persistent
	private String cedula;

	public Kosarcikk(String elado, String vevo, String tipus, String cikkszam,
			String szinkod, Double ar, Double areur, Double arusd,
			Integer exportkarton, Integer kiskarton, Integer darab,
			Double fizet, Double fizeteur, Double fizetusd, String cedula) {

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

	public Double getAr() {
		return ar;
	}

	public void setAr(Double ar) {
		this.ar = ar;
	}

	public Double getAreur() {
		return areur;
	}

	public void setAreur(Double areur) {
		this.areur = areur;
	}

	public Double getArusd() {
		return arusd;
	}

	public void setArusd(Double arusd) {
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

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

}
