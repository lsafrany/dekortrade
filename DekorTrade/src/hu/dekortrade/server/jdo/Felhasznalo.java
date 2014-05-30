package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Felhasznalo {

	@Persistent
	private String rovidnev;

	@Persistent
	private String nev;

	@Persistent
	private String menu;

	@Persistent
	private String jelszo;

	@Persistent
	private Boolean torolt;

	public Felhasznalo(String rovidnev, String nev, String menu, String jelszo,
			Boolean torolt) {
		this.rovidnev = rovidnev;
		this.nev = nev;
		this.menu = menu;
		this.jelszo = jelszo;
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

	public String getJelszo() {
		return jelszo;
	}

	public void setJelszo(String jelszo) {
		this.jelszo = jelszo;
	}

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

}
