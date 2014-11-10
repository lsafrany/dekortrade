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

	private Double ar;

	private Double areur;

	private Double arusd;

	private Double fizet;

	private Double fizeteur;

	private Double fizetusd;

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

}
