package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Jog {

	@Persistent
	private String rovidnev;

	@Persistent
	private String nev;

	public Jog(String rovidnev, String nev) {
		this.rovidnev = rovidnev;
		this.nev = nev;
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

}
