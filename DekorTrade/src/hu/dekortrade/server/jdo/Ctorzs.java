package hu.dekortrade.server.jdo;

import java.math.BigDecimal;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Ctorzs {

	@Persistent
	private String cikkszam;

	@Persistent
	private String megnevezes;

	@Persistent
	private BigDecimal ar;

	@Persistent
	private Integer kiskarton;

	@Persistent
	private Integer darab;

	@Persistent
	private BigDecimal terfogat;

	@Persistent
	private String jel;

	@Persistent
	private BigDecimal bsuly;

	@Persistent
	private BigDecimal nsuly;

	@Persistent
	private Integer kepek;

	@Persistent
	private Boolean szinkron;

	@Persistent
	private Boolean torolt;

	public Ctorzs(String cikkszam, String megnevezes, BigDecimal ar,
			Integer kiskarton, Integer darab, BigDecimal terfogat, String jel,
			BigDecimal bsuly, BigDecimal nsuly, Integer kepek,
			Boolean szinkron, Boolean torolt) {
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
		this.szinkron = szinkron;
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

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

	public Boolean getSzinkron() {
		return szinkron;
	}

	public void setSzinkron(Boolean szinkron) {
		this.szinkron = szinkron;
	}

}
