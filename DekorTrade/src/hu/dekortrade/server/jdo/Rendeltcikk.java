package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Rendeltcikk {

	@Persistent
	private String rovidnev;

	@Persistent
	private String rendeles;

	@Persistent
	private String cikkszam;

	@Persistent
	private String szinkod;

	@Persistent
	private String status;

	@Persistent
	private Integer exportkarton;

	@Persistent
	private Integer kiskarton;

	@Persistent
	private Integer darab;

	@Persistent
	private Double arusd;

	@Persistent
	private Double fizetusd;
	
	@Persistent
	private boolean szamolt;

	public Rendeltcikk(String rovidnev, String rendeles, String cikkszam,
			String szinkod, String status, Integer exportkarton, Integer kiskarton,
			Integer darab, boolean szamolt) {
		this.rovidnev = rovidnev;
		this.rendeles = rendeles;
		this.cikkszam = cikkszam;
		this.status = status;
		this.szinkod = szinkod;
		this.exportkarton = exportkarton;
		this.kiskarton = kiskarton;
		this.darab = darab;
		this.szamolt = szamolt;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getRendeles() {
		return rendeles;
	}

	public void setRendeles(String rendeles) {
		this.rendeles = rendeles;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public boolean isSzamolt() {
		return szamolt;
	}

	public void setSzamolt(boolean szamolt) {
		this.szamolt = szamolt;
	}

	public Double getArusd() {
		return arusd;
	}

	public void setArusd(Double arusd) {
		this.arusd = arusd;
	}

	public Double getFizetusd() {
		return fizetusd;
	}

	public void setFizetusd(Double fizetusd) {
		this.fizetusd = fizetusd;
	}

}
