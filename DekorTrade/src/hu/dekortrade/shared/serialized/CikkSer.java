package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CikkSer implements IsSerializable {

	private String cikkszam;

	private String megnevezes;

	private Double ar;

	private Integer kiskarton;

	private Integer darab;

	private Double terfogat;

	private String jel;

	private Double bsuly;

	private Double nsuly;

	private Integer kepek;

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

	public Double getAr() {
		return ar;
	}

	public void setAr(Double ar) {
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

	public Double getTerfogat() {
		return terfogat;
	}

	public void setTerfogat(Double terfogat) {
		this.terfogat = terfogat;
	}

	public String getJel() {
		return jel;
	}

	public void setJel(String jel) {
		this.jel = jel;
	}

	public Double getBsuly() {
		return bsuly;
	}

	public void setBsuly(Double bsuly) {
		this.bsuly = bsuly;
	}

	public Double getNsuly() {
		return nsuly;
	}

	public void setNsuly(Double nsuly) {
		this.nsuly = nsuly;
	}

	public Integer getKepek() {
		return kepek;
	}

	public void setKepek(Integer kepek) {
		this.kepek = kepek;
	}

}
