package hu.dekortrade.server.jdo;

import javax.jdo.annotations.Cacheable;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
@Cacheable("false")
public class Kosar {

	@Persistent
	private String elado;

	@Persistent
	private String vevo;

	@Persistent
	private String tipus;

	@Persistent
	private String cedula;

	public Kosar(String elado, String vevo, String tipus, String cedula) {

		this.elado = elado;
		this.vevo = vevo;
		this.tipus = tipus;
		this.cedula = cedula;
	}

	public String getElado() {
		return elado;
	}

	public void setElado(String elado) {
		this.elado = elado;
	}

	public String getVevo() {
		return vevo;
	}

	public void setVevo(String vevo) {
		this.vevo = vevo;
	}

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

}
