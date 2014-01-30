package hu.dekortrade99.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Vevo {

	@Persistent
	private String rovidnev;

	@Persistent
	private String jelszo;

	@Persistent
	private String nev;

	@Persistent
	private Boolean internet;
	
	@Persistent
	private Boolean torolt;

	public Vevo(String rovidnev, String jelszo, String nev, Boolean internet, Boolean torolt) {
		this.rovidnev = rovidnev;
		this.jelszo = jelszo;
		this.nev = nev;
		this.internet = internet;
		this.torolt = false;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getJelszo() {
		return jelszo;
	}

	public void setJelszo(String jelszo) {
		this.jelszo = jelszo;
	}

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}
	
	public Boolean getInternet() {
		return internet;
	}

	public void setInternet(Boolean internet) {
		this.internet = internet;
	}

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}
