package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KosarSer implements IsSerializable {

	private String elado;

	private String vevo;

	private String tipus;	
	
	private String cikkszam;

	private String szinkod;

	private String megnevezes;

	private Float ar;

	private Float areur;

	private Float arusd;
	
	private Integer exportkarton;

	private Integer kiskarton;

	private Integer darab;

	private Float fizet;
	
	private Float fizeteur;	
	
	private Float fizetusd;	
	
	private String cedula;

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

	public String getMegnevezes() {
		return megnevezes;
	}

	public void setMegnevezes(String megnevezes) {
		this.megnevezes = megnevezes;
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
