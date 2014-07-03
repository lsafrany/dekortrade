package hu.dekortrade99.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CikkSer implements IsSerializable {

	private String fotipus;
	
	private String altipus;
	
	private String cikkszam;

	private String megnevezes;

	private Float ar;

	private Integer kiskarton;

	private Integer darab;

	private Float terfogat;

	private Float bsuly;

	private Float nsuly;

	private Integer kepek;

	public String getFotipus() {
		return fotipus;
	}

	public void setFotipus(String fotipus) {
		this.fotipus = fotipus;
	}

	public String getAltipus() {
		return altipus;
	}

	public void setAltipus(String altipus) {
		this.altipus = altipus;
	}

	public String getCikkszam() {
		return cikkszam;
	}

	public void setCikkszam(String cikkszam) {
		this.cikkszam = cikkszam;
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

	public Float getTerfogat() {
		return terfogat;
	}

	public void setTerfogat(Float terfogat) {
		this.terfogat = terfogat;
	}

	public Float getBsuly() {
		return bsuly;
	}

	public void setBsuly(Float bsuly) {
		this.bsuly = bsuly;
	}

	public Float getNsuly() {
		return nsuly;
	}

	public void setNsuly(Float nsuly) {
		this.nsuly = nsuly;
	}

	public Integer getKepek() {
		return kepek;
	}

	public void setKepek(Integer kepek) {
		this.kepek = kepek;
	}

}