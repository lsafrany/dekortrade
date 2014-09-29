package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BeszallitottcikkSer implements IsSerializable {

	private String cikkszam;

	private String szinkod;

	private Integer exportkarton;

	private Integer kiskarton;

	private Integer darab;

	private Integer megrendexportkarton;

	private Integer megrendkiskarton;

	private Integer megrenddarab;

	private String rogzito;

	private Date datum;

	private Boolean rovancs;
	
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

	public Boolean getRovancs() {
		return rovancs;
	}

	public void setRovancs(Boolean rovancs) {
		this.rovancs = rovancs;
	}

}
