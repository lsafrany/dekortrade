package hu.dekortrade99.shared.serialized;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CtorzsSer implements IsSerializable {
  
	private String cikkszam;

	private String megnevezes;

	private BigDecimal ar;

	private Integer kiskarton;

	private Integer darab;

	private BigDecimal terfogat;

	private String jel;

	private BigDecimal bsuly;

	private BigDecimal nsuly;

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

	public BigDecimal getAr() {
		return ar;
	}

	public void setAr(BigDecimal ar) {
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

	public BigDecimal getTerfogat() {
		return terfogat;
	}

	public void setTerfogat(BigDecimal terfogat) {
		this.terfogat = terfogat;
	}

	public String getJel() {
		return jel;
	}

	public void setJel(String jel) {
		this.jel = jel;
	}

	public BigDecimal getBsuly() {
		return bsuly;
	}

	public void setBsuly(BigDecimal bsuly) {
		this.bsuly = bsuly;
	}

	public BigDecimal getNsuly() {
		return nsuly;
	}

	public void setNsuly(BigDecimal nsuly) {
		this.nsuly = nsuly;
	}

	public Integer getKepek() {
		return kepek;
	}

	public void setKepek(Integer kepek) {
		this.kepek = kepek;
	}

}
