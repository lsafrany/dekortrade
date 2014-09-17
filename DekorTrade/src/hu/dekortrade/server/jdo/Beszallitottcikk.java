package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Beszallitottcikk {

	@Persistent
	private String cikkszam;

	@Persistent
	private String szinkod;

	@Persistent
	private Integer exportkarton;

	@Persistent
	private Integer kiskarton;

	@Persistent
	private Integer darab;

	@Persistent
	private Integer megrendexportkarton;

	@Persistent
	private Integer megrendkiskarton;

	@Persistent
	private Integer megrenddarab;

	@Persistent
	private String rogzito;

	@Persistent
	private Date datum;
	
	public Beszallitottcikk(String cikkszam, String szinkod, 
			Integer exportkarton, Integer kiskarton, Integer darab, 
			Integer megrendexportkarton, Integer megrendkiskarton, Integer megrenddarab, 
			String rogzito, Date datum) {
		this.cikkszam = cikkszam;
		this.szinkod = szinkod;
		this.exportkarton = exportkarton;
		this.kiskarton = kiskarton;
		this.darab = darab;
		this.megrendexportkarton = megrendexportkarton;
		this.megrendkiskarton = megrendkiskarton;
		this.megrenddarab = megrenddarab;
		this.rogzito = rogzito;
		this.datum = datum;
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

	public Integer getMegrendexportkarton() {
		return megrendexportkarton;
	}

	public void setMegrendexportkarton(Integer megrendexportkarton) {
		this.megrendexportkarton = megrendexportkarton;
	}

	public Integer getMegrendkiskarton() {
		return megrendkiskarton;
	}

	public void setMegrendkiskarton(Integer megrendkiskarton) {
		this.megrendkiskarton = megrendkiskarton;
	}

	public Integer getMegrenddarab() {
		return megrenddarab;
	}

	public void setMegrenddarab(Integer megrenddarab) {
		this.megrenddarab = megrenddarab;
	}

	public String getRogzito() {
		return rogzito;
	}

	public void setRogzito(String rogzito) {
		this.rogzito = rogzito;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}
}
