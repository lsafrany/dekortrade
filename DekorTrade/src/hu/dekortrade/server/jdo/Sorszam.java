package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Sorszam {

	@Persistent
	private String cedula;

	@Persistent
	private String tipus;

	public Sorszam(String cedula, String tipus) {
		this.cedula = cedula;
		this.tipus = tipus;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

}
