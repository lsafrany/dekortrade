package hu.dekortrade99.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Cikk {

	@Persistent
	private String cikkszam;

	@Persistent
	private String megnevezes;

	@Persistent
	private Float ar;

	@Persistent
	private Integer kiskarton;

	@Persistent
	private Integer darab;

	@Persistent
	private Float terfogat;

	@Persistent
	private String jel;

	@Persistent
	private Float bsuly;

	@Persistent
	private Float nsuly;

	@Persistent
	private Integer kepek;

	@Persistent
	private Boolean torolt;

	public Cikk(String cikkszam, String megnevezes, Float ar,
			Integer kiskarton, Integer darab, Float terfogat, String jel,
			Float bsuly, Float nsuly, Integer kepek, Boolean torolt) {
		this.cikkszam = cikkszam;
		this.megnevezes = megnevezes;
		this.ar = ar;
		this.kiskarton = kiskarton;
		this.darab = darab;
		this.terfogat = terfogat;
		this.jel = jel;
		this.bsuly = bsuly;
		this.nsuly = nsuly;
		this.kepek = kepek;
		this.torolt = torolt;
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

	public String getJel() {
		return jel;
	}

	public void setJel(String jel) {
		this.jel = jel;
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
