package hu.dekortrade99.server.sync;

public class CikkSzinkron {

	private String fotipus;
	
	private String altipus;
	
	private String cikkszam;

	private String megnevezes;

	private Double ar;

	private Integer kiskarton;

	private Integer darab;

	private Double terfogat;

	private Double bsuly;

	private Double nsuly;

	private Integer kepek;

	private Boolean torolt;
	
	public CikkSzinkron() {
		
	}
	
	public CikkSzinkron(String fotipus, String altipus, String cikkszam, String megnevezes, Double ar,
			Integer kiskarton, Integer darab, Double terfogat,
			Double bsuly, Double nsuly, Integer kepek, Boolean torolt) {
		this.fotipus = fotipus;
		this.altipus = altipus;
		this.megnevezes = megnevezes;
		this.ar = ar;
		this.kiskarton = kiskarton;
		this.darab = darab;
		this.terfogat = terfogat;
		this.bsuly = bsuly;
		this.nsuly = nsuly;
		this.kepek = kepek;
		this.torolt = torolt;
	}

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

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}
