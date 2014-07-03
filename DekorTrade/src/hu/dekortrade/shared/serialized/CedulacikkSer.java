package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CedulacikkSer implements IsSerializable {

	private String cedula;

	private String status;
	
	private String cikkszam;

	private String szinkod;

	private String megnevezes;

	private Integer exportkarton;

	private Integer kiskarton;

	private Integer darab;

	private Float ar;

	private Float areur;

	private Float arusd;
	
	private Float fizet;
	
	private Float fizeteur;

	private Float fizetusd;

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
}
