package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Vevo {

	@Persistent
	private String rovidnev;

	@Persistent
	private String nev;

	@Persistent
	private String cim;

	@Persistent
	private String elerhetoseg;

	@Persistent
	private Boolean internet;

	@Persistent
	private Boolean szinkron;

	@Persistent
	private Boolean torolt;

	public Vevo(String rovidnev, String nev, String cim, String elerhetoseg,
			Boolean internet, Boolean szinkron, Boolean torolt) {
		this.rovidnev = rovidnev;
		this.nev = nev;
		this.cim = cim;
		this.elerhetoseg = elerhetoseg;
		this.internet = internet;
		this.szinkron = szinkron;
		this.torolt = torolt;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
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

	public Boolean getInternet() {
		return internet;
	}

	public void setInternet(Boolean internet) {
		this.internet = internet;
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
