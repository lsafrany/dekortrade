package hu.dekortrade.server.sync;

public class CikkSzinkron {

	private String fotipus;

	private String altipus;

	private String cikkszam;

	private String szinkod;
	
	private String megnevezes;

	private Float ar;

	private Integer kiskarton;

	private Integer darab;

	private Float terfogat;

	private Float bsuly;

	private Float nsuly;

	private Integer kepek;

	private Boolean torolt;

	public CikkSzinkron() {

	}

	public CikkSzinkron(String fotipus, String altipus, String cikkszam, String szinkod,
			String megnevezes, Float ar, Integer kiskarton, Integer darab,
			Float terfogat, Float bsuly, Float nsuly, Integer kepek,
			Boolean torolt) {
		this.fotipus = fotipus;
		this.altipus = altipus;
		this.cikkszam = cikkszam;
		this.szinkod = szinkod;
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

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}
