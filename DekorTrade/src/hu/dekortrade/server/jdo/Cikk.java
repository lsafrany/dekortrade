package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Cikk {

	@Persistent
	private String fotipus;

	@Persistent
	private String altipus;

	@Persistent
	private String gyarto;

	@Persistent
	private String gyartocikkszam;

	@Persistent
	private String cikkszam;

	@Persistent
	private String szinkod;

	@Persistent
	private Date felviteltol;

	@Persistent
	private Date felvitelig;
	
	@Persistent
	private Date lejarattol;

	@Persistent
	private Date lejaratig;

	@Persistent
	private String megnevezes;

	@Persistent
	private String vamtarifaszam;

	@Persistent
	private Float fob;

	@Persistent
	private Float szallitas;

	@Persistent
	private Float ddu;

	@Persistent
	private Float ersz;

	@Persistent
	private Float elorar;

	@Persistent
	private Float ujfob;

	@Persistent
	private Float ujszallitas;

	@Persistent
	private Float ujddu;

	@Persistent
	private Float ujersz;

	@Persistent
	private Float ujelorar;

	@Persistent
	private Float ar;

	@Persistent
	private Float areur;

	@Persistent
	private Float arszorzo;

	@Persistent
	private Integer kiskarton;

	@Persistent
	private Integer darab;

	@Persistent
	private Float terfogat;

	@Persistent
	private Float terfogatlab;

	@Persistent
	private Float bsuly;

	@Persistent
	private Float nsuly;

	@Persistent
	private String leiras;

	@Persistent
	private String megjegyzes;

	@Persistent
	private Boolean akcios;

	@Persistent
	private String mertekegyseg;

	@Persistent
	private Integer kepek;

	@Persistent
	private Boolean szinkron;

	@Persistent
	private Boolean torolt;

	public Cikk(String fotipus, String altipus, String gyarto,
			String gyartocikkszam, String cikkszam,
			String szinkod, Date felviteltol, Date felvitelig,
			Date lejarattol, Date lejaratig, String megnevezes, String vamtarifaszam,
			Float fob, Float szallitas, Float ddu, Float ersz, Float elorar, 
			Float ujfob, Float ujszallitas, Float ujddu, Float ujersz, Float ujelorar, 
			Float ar, Float areur, Float arszoro, Integer kiskarton, Integer darab,
			Float terfogat, Float terfogatlab, Float bsuly, Float nsuly, String leiras, String megjegyzes,
			Boolean akcios, String mertekegyseg,Integer kepek, Boolean szinkron, Boolean torolt) {
		this.fotipus = fotipus;
		this.altipus = altipus;
		this.gyarto = gyarto;
		this.gyartocikkszam = gyartocikkszam;
		this.cikkszam = cikkszam;
		this.szinkod = szinkod;
		this.felviteltol = felviteltol;
		this.felvitelig = felvitelig;
		this.lejarattol = lejarattol;
		this.lejaratig = lejaratig;
		this.megnevezes = megnevezes;
		this.vamtarifaszam = vamtarifaszam;
		this.fob = fob;
		this.szallitas = szallitas;
		this.ddu = ddu;
		this.ersz = ersz;
		this.elorar = elorar;	
		this.ujfob = ujfob;
		this.ujszallitas = ujszallitas;
		this.ujddu = ujddu;
		this.ujersz = ujersz;
		this.ujelorar = ujelorar;
		this.ar = ar;
		this.areur = areur;
		this.arszorzo = arszoro;
		this.kiskarton = kiskarton;
		this.darab = darab;
		this.terfogat = terfogat;
		this.terfogatlab = terfogatlab;
		this.bsuly = bsuly;
		this.nsuly = nsuly;
		this.leiras = leiras;
		this.megjegyzes = megjegyzes;
		this.akcios = akcios;
		this.mertekegyseg = mertekegyseg;
		this.kepek = kepek;
		this.szinkron = szinkron;
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

	public String getGyarto() {
		return gyarto;
	}

	public void setGyarto(String gyarto) {
		this.gyarto = gyarto;
	}

	public String getGyartocikkszam() {
		return gyartocikkszam;
	}

	public void setGyartocikkszam(String gyartocikkszam) {
		this.gyartocikkszam = gyartocikkszam;
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
	
	public Date getFelviteltol() {
		return felviteltol;
	}

	public void setFelviteltol(Date felviteltol) {
		this.felviteltol = felviteltol;
	}

	public Date getFelvitelig() {
		return felvitelig;
	}

	public void setFelvitelig(Date felvitelig) {
		this.felvitelig = felvitelig;
	}

	public Date getLejarattol() {
		return lejarattol;
	}

	public void setLejarattol(Date lejarattol) {
		this.lejarattol = lejarattol;
	}

	public Date getLejaratig() {
		return lejaratig;
	}

	public void setLejaratig(Date lejaratig) {
		this.lejaratig = lejaratig;
	}

	public String getMegnevezes() {
		return megnevezes;
	}

	public void setMegnevezes(String megnevezes) {
		this.megnevezes = megnevezes;
	}

	public String getVamtarifaszam() {
		return vamtarifaszam;
	}

	public void setVamtarifaszam(String vamtarifaszam) {
		this.vamtarifaszam = vamtarifaszam;
	}

	public Float getFob() {
		return fob;
	}

	public void setFob(Float fob) {
		this.fob = fob;
	}

	public Float getSzallitas() {
		return szallitas;
	}

	public void setSzallitas(Float szallitas) {
		this.szallitas = szallitas;
	}

	public Float getDdu() {
		return ddu;
	}

	public void setDdu(Float ddu) {
		this.ddu = ddu;
	}

	public Float getErsz() {
		return ersz;
	}

	public void setErsz(Float ersz) {
		this.ersz = ersz;
	}

	public Float getElorar() {
		return elorar;
	}

	public void setElorar(Float elorar) {
		this.elorar = elorar;
	}
		
	public Float getUjfob() {
		return ujfob;
	}

	public void setUjfob(Float ujfob) {
		this.ujfob = ujfob;
	}

	public Float getUjszallitas() {
		return ujszallitas;
	}

	public void setUjszallitas(Float ujszallitas) {
		this.ujszallitas = ujszallitas;
	}

	public Float getUjddu() {
		return ujddu;
	}

	public void setUjddu(Float ujddu) {
		this.ujddu = ujddu;
	}

	public Float getUjersz() {
		return ujersz;
	}

	public void setUjersz(Float ujersz) {
		this.ujersz = ujersz;
	}

	public Float getUjelorar() {
		return ujelorar;
	}

	public void setUjelorar(Float ujelorar) {
		this.ujelorar = ujelorar;
	}

	public Float getAr() {
		return ar;
	}

	public void setAr(Float ar) {
		this.ar = ar;
	}	
	
	public Float getAreur() {
		return areur;
	}

	public void setAreur(Float areur) {
		this.areur = areur;
	}

	public Float getArszorzo() {
		return arszorzo;
	}

	public void setArszorzo(Float arszorzo) {
		this.arszorzo = arszorzo;
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

	public Float getTerfogatlab() {
		return terfogatlab;
	}

	public void setTerfogatlab(Float terfogatlab) {
		this.terfogatlab = terfogatlab;
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

	public String getLeiras() {
		return leiras;
	}

	public void setLeiras(String leiras) {
		this.leiras = leiras;
	}

	public String getMegjegyzes() {
		return megjegyzes;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
	}

	public Boolean getAkcios() {
		return akcios;
	}

	public void setAkcios(Boolean akcios) {
		this.akcios = akcios;
	}

	public String getMertekegyseg() {
		return mertekegyseg;
	}

	public void setMertekegyseg(String mertekegyseg) {
		this.mertekegyseg = mertekegyseg;
	}

	public Integer getKepek() {
		return kepek;
	}

	public void setKepek(Integer kepek) {
		this.kepek = kepek;
	}

	public Boolean getSzinkron() {
		return szinkron;
	}

	public void setSzinkron(Boolean szinkron) {
		this.szinkron = szinkron;
	}

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}
