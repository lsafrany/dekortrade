package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Gyarto {

	@Persistent
	private String kod;

	@Persistent
	private String nev;

	@Persistent
	private String cim;

	@Persistent
	private String elerhetoseg;

	@Persistent
	private String swifkod;

	@Persistent
	private String bankadat;

	@Persistent
	private String szamlaszam;

	@Persistent
	private Double egyenleg;

	@Persistent
	private String kedvezmeny;

	@Persistent
	private String megjegyzes;

	@Persistent
	private Boolean torolt;

	public Gyarto(String kod, String nev, String cim, String elerhetoseg,
			String swifkod, String bankadat, String szamlaszam,
			Double egyenleg, String kedvezmeny, String megjegyzes,
			Boolean torolt) {
		this.kod = kod;
		this.nev = nev;
		this.cim = cim;
		this.elerhetoseg = elerhetoseg;
		this.swifkod = swifkod;
		this.bankadat = bankadat;
		this.szamlaszam = szamlaszam;	
		this.egyenleg = egyenleg;
		this.kedvezmeny = kedvezmeny;
		this.megjegyzes = megjegyzes;
		this.torolt = torolt;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}

	public String getCim() {
		return cim;
	}

	public void setCim(String cim) {
		this.cim = cim;
	}

	public String getElerhetoseg() {
		return elerhetoseg;
	}

	public void setElerhetoseg(String elerhetoseg) {
		this.elerhetoseg = elerhetoseg;
	}

	public String getSwifkod() {
		return swifkod;
	}

	public void setSwifkod(String swifkod) {
		this.swifkod = swifkod;
	}

	public String getBankadat() {
		return bankadat;
	}

	public void setBankadat(String bankadat) {
		this.bankadat = bankadat;
	}

	public String getSzamlaszam() {
		return szamlaszam;
	}

	public void setSzamlaszam(String szamlaszam) {
		this.szamlaszam = szamlaszam;
	}

	public Double getEgyenleg() {
		return egyenleg;
	}

	public void setEgyenleg(Double egyenleg) {
		this.egyenleg = egyenleg;
	}

	public String getKedvezmeny() {
		return kedvezmeny;
	}

	public void setKedvezmeny(String kedvezmeny) {
		this.kedvezmeny = kedvezmeny;
	}

	public String getMegjegyzes() {
		return megjegyzes;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
	}

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}
